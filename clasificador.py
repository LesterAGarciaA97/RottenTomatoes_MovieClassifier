from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
import os
import shutil
from collections import defaultdict
import math
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression

app = Flask(__name__)

def clean_csv(filepath):
    chunksize = 10000
    cleaned_chunks = []

    # Leer el archivo CSV en bloques y procesar cada bloque
    for chunk in pd.read_csv(filepath, chunksize=chunksize, on_bad_lines='skip'):
        # Verificar que las columnas esperadas estén presentes
        expected_columns = ['rotten_tomatoes_link', 'critic_name', 'top_critic', 'publisher_name', 'review_type', 'review_score', 'review_date', 'review_content']
        if not all(column in chunk.columns for column in expected_columns):
            print(f"Skipping chunk due to missing columns")
            continue

        # Limpieza de datos específica:
        # Eliminar comas dentro de campos de texto y reemplazarlas por otro carácter
        chunk['review_content'] = chunk['review_content'].str.replace(',', ';', regex=False)

        # Eliminar comillas dobles que puedan confundir al parser
        chunk['review_content'] = chunk['review_content'].str.replace('"', '', regex=False)

        # Asegurarse de que no haya espacios en blanco extra al inicio o al final de las cadenas
        chunk = chunk.applymap(lambda x: x.strip() if isinstance(x, str) else x)

        # Normalizar fechas si es necesario
        chunk['review_date'] = pd.to_datetime(chunk['review_date'], errors='coerce').dt.strftime('%Y-%m-%d')

        cleaned_chunks.append(chunk)

    if not cleaned_chunks:
        print("No chunks to concatenate. Please check the CSV file format and contents.")
    else:
        # Concatenar todos los bloques limpios
        df = pd.concat(cleaned_chunks, ignore_index=True)

        # Guardar el archivo CSV sobrescribiendo el original
        df.to_csv(filepath, index=False)

        # Crear la carpeta src/main/resources si no existe
        resources_path = 'src/main/resources'
        if not os.path.exists(resources_path):
            os.makedirs(resources_path)

        # Crear una copia del archivo CSV en la carpeta src/main/resources
        resources_file_path = os.path.join(resources_path, 'rotten_tomatoes_critic_reviews.csv')
        shutil.copy(filepath, resources_file_path)

def tokenize(text):
    # Conversión a minúsculas de una cadena de texto en un listado de palabras.
    return text.lower().split()

def train_naive_bayes(df):
    # Entrenamiento para el modelo de Naive.
    positive_reviews = df[df['review_type'] == 'Fresh']
    negative_reviews = df[df['review_type'] == 'Rotten']

    positive_word_count = defaultdict(int)
    negative_word_count = defaultdict(int)
    positive_total_words = 0
    negative_total_words = 0

    # Contar palabras en críticas positivas
    for review in positive_reviews['review_content']:
        words = tokenize(review)
        positive_total_words += len(words)
        for word in words:
            positive_word_count[word] += 1

    # Contar palabras en críticas negativas
    for review in negative_reviews['review_content']:
        words = tokenize(review)
        negative_total_words += len(words)
        for word in words:
            negative_word_count[word] += 1

    # Calcular probabilidades a priori
    total_reviews = len(df)
    positive_prior = len(positive_reviews) / total_reviews
    negative_prior = len(negative_reviews) / total_reviews

    vocabulary = set(positive_word_count.keys()).union(set(negative_word_count.keys()))

    model = {
        'positive_word_count': positive_word_count,
        'negative_word_count': negative_word_count,
        'positive_total_words': positive_total_words,
        'negative_total_words': negative_total_words,
        'positive_prior': positive_prior,
        'negative_prior': negative_prior,
        'vocabulary': vocabulary
    }

    return model

def predict_naive_bayes(model, text):
    words = tokenize(text)
    positive_prob = math.log(model['positive_prior'])
    negative_prob = math.log(model['negative_prior'])
    vocab_size = len(model['vocabulary'])

    for word in words:
        positive_word_likelihood = (model['positive_word_count'].get(word, 0) + 1) / (model['positive_total_words'] + vocab_size)
        negative_word_likelihood = (model['negative_word_count'].get(word, 0) + 1) / (model['negative_total_words'] + vocab_size)

        positive_prob += math.log(positive_word_likelihood)
        negative_prob += math.log(negative_word_likelihood)

    return 'Fresh' if positive_prob > negative_prob else 'Rotten'

def load_data():
    filepath = 'rotten_tomatoes_critic_reviews.csv'
    clean_csv(filepath)

    try:
        df = pd.read_csv(filepath)
        df['review_content'] = df['review_content'].fillna('')
        naive_bayes_model = train_naive_bayes(df)

        # Vectorizer and Logistic Regression model for additional classification
        vectorizer = CountVectorizer(max_features=1000)
        X = vectorizer.fit_transform(df['review_content'])
        y = df['review_type'].apply(lambda x: 1 if x == 'Fresh' else 0)

        logistic_regression_model = LogisticRegression()
        logistic_regression_model.fit(X, y)

        return naive_bayes_model, vectorizer, logistic_regression_model
    except Exception as e:
        print(f"Error al cargar y procesar los datos: {e}")
        return None, None, None

naive_bayes_model, vectorizer, logistic_regression_model = load_data()

@app.route('/clasificar', methods=['GET'])
def clasificar():
    frase = request.args.get('frase')
    if not frase:
        return jsonify({'error': 'Frase no proporcionada'}), 400

    try:
        # Clasificar con Naive Bayes
        clasificacion_nb = predict_naive_bayes(naive_bayes_model, frase)

        # Clasificar con Logistic Regression
        X_test = vectorizer.transform([frase])
        prediccion_lr = logistic_regression_model.predict(X_test)[0]
        clasificacion_lr = 'Fresh' if prediccion_lr == 1 else 'Rotten'

        return jsonify({'clasificacion_naive_bayes': clasificacion_nb, 'clasificacion_logistic_regression': clasificacion_lr})
    except Exception as e:
        return jsonify({'error': f'Error al clasificar la frase: {e}'}), 500

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
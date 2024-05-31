package com.proyecto.clasificador.service;

import com.proyecto.clasificador.model.Movie;
import com.proyecto.clasificador.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class DataLoaderService {

    @Autowired
    private MovieRepository movieRepository;

    @PostConstruct
    public void init() {
        loadMovieData();
    }

    private void loadMovieData() {
        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource("rotten_tomatoes_critic_reviews.csv").getFile()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (fields.length >= 8) {
                    String movieId = fields[0];
                    String rottenTomatoesLink = fields[1];
                    String criticName = fields[2];
                    boolean topCritic = Boolean.parseBoolean(fields[3]);
                    String publisherName = fields[4];
                    String reviewType = fields[5];
                    String reviewScore = fields[6];
                    String reviewDate = fields[7];
                    String reviewContent = fields[8];

                    Movie movie = new Movie(movieId, rottenTomatoesLink, criticName, topCritic, publisherName, reviewType, reviewScore, reviewDate, reviewContent);
                    movieRepository.save(movie);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading movie data: " + e.getMessage());
        }
    }
}
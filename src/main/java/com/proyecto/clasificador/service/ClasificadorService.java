package com.proyecto.clasificador.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ClasificadorService {

    public String clasificarFrase(String frase) {
        String url = "http://localhost:5000/clasificar";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("frase", frase);

        RestTemplate restTemplate = new RestTemplate();
        String resultado = restTemplate.getForObject(builder.toUriString(), String.class);
        return resultado;
    }
}
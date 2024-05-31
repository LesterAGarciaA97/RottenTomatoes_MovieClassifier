package com.proyecto.clasificador.controller;

import com.proyecto.clasificador.service.ClasificadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClasificadorController {

    @Autowired
    private ClasificadorService clasificadorService;

    @GetMapping("/clasificar")
    public ResponseEntity<String> clasificar(@RequestParam String frase) {
        String resultado = clasificadorService.clasificarFrase(frase);
        return ResponseEntity.ok(resultado);
    }
}
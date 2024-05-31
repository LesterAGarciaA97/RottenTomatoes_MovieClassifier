package com.proyecto.clasificador.controller;

import com.proyecto.clasificador.model.Movie;
import com.proyecto.clasificador.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies")
    public String listMovies(Model model) {
        try {
            List<Movie> movies = movieRepository.findAll();
            model.addAttribute("movies", movies);
            return "movies";  // Nombre de la vista HTML Thymeleaf para mostrar las películas
        } catch (DataAccessException e) {
            logger.error("Error al acceder a la base de datos", e);
            return "error";  // Vista de error genérica en caso de fallo en acceso a datos
        } catch (Exception e) {
            logger.error("Error inesperado", e);
            return "error";  // Vista de error genérica para cualquier otro error
        }
    }
}

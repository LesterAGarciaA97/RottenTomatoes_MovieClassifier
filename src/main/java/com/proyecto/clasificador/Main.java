package com.proyecto.clasificador;

import com.proyecto.clasificador.service.DataLoaderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.proyecto.clasificador", "com.proyecto.clasificador.service"})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//        ApplicationContext ctx = SpringApplication.run(Main.class, args);
//        DataLoaderService dataLoader = ctx.getBean(DataLoaderService.class);
//        dataLoader.loadMovieData();  // Solo para pruebas, remover después de verificar que funciona
    }
}
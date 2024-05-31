package com.proyecto.clasificador.controller;

import com.proyecto.clasificador.model.User;
import com.proyecto.clasificador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller  // Asegúrate de usar @Controller para soportar vistas
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Método para mostrar el formulario de registro
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Método para procesar el formulario de registro
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            User savedUser = userService.registerUser(user);
            model.addAttribute("user", savedUser);
            return "result";  // Nombre de la vista a mostrar después del registro
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al registrar al usuario: " + e.getMessage());
            return "register";  // Vuelve a la vista de registro en caso de error
        }
    }

    // Método opcional para listar usuarios (ya lo tenías)
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }
}
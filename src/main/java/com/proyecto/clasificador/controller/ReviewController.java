package com.proyecto.clasificador.controller;

import com.proyecto.clasificador.model.Review;
import com.proyecto.clasificador.model.User;
import com.proyecto.clasificador.service.ReviewService;
import com.proyecto.clasificador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    // Método para mostrar el formulario de ingreso de reviews
    @GetMapping
    public String showReviewForm(Model model) {
        model.addAttribute("review", new Review());
        return "review";
    }

    // Método para procesar el formulario de reviews
    @PostMapping
    public String createReview(@ModelAttribute Review review, Model model) {
        Review savedReview = reviewService.saveReview(review);
        model.addAttribute("review", savedReview);
        return "reviewResult";  // Asume que tienes una vista para mostrar el resultado
    }

    // Método para obtener reviews por userId utilizando un parámetro de consulta
    @GetMapping("/user")
    public String getReviewsByUserId(@RequestParam("userId") Long userId, Model model) {
        List<Review> reviews = reviewService.findReviewsByUserId(userId);
        model.addAttribute("reviews", reviews);
        return "reviewsList";  // Asegúrate de tener una vista adecuada para esto
    }

    // Método para listar todos los usuarios con un enlace a sus reviews
    @GetMapping("/user/list")
    public String listUsersWithReviews(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "userReviewsList";  // Esta vista mostrará una lista de usuarios y enlaces a sus reviews
    }
}
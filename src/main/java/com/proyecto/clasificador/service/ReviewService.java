package com.proyecto.clasificador.service;

import com.proyecto.clasificador.model.Review;
import com.proyecto.clasificador.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        if (review.getRating() >= 3) {  // Calificación de 3 o más es "Fresh"
            review.setStatus("Fresh");
        } else {
            review.setStatus("Rotten");
        }
        return reviewRepository.save(review);
    }

    // Método para encontrar reviews por el ID de usuario
    public List<Review> findReviewsByUserId(Long userId) {
        return reviewRepository.findByUser_Id(userId);
    }
}
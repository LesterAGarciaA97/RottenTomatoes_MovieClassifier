package com.proyecto.clasificador.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    private String movieId;
    private String rottenTomatoesLink;
    private String criticName;
    private boolean topCritic;
    private String publisherName;
    private String reviewType;
    private String reviewScore;
    private String reviewDate;
    private String reviewContent;

    public Movie() {}

    // Constructor necesario para DataLoaderService
    public Movie(String movieId, String rottenTomatoesLink, String criticName, boolean topCritic, String publisherName, String reviewType, String reviewScore, String reviewDate, String reviewContent) {
        this.movieId = movieId;
        this.rottenTomatoesLink = rottenTomatoesLink;
        this.criticName = criticName;
        this.topCritic = topCritic;
        this.publisherName = publisherName;
        this.reviewType = reviewType;
        this.reviewScore = reviewScore;
        this.reviewDate = reviewDate;
        this.reviewContent = reviewContent;
    }

    // Getters and Setters
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getRottenTomatoesLink() { return rottenTomatoesLink; }
    public void setRottenTomatoesLink(String rottenTomatoesLink) { this.rottenTomatoesLink = rottenTomatoesLink; }

    public String getCriticName() { return criticName; }
    public void setCriticName(String criticName) { this.criticName = criticName; }

    public boolean isTopCritic() { return topCritic; }
    public void setTopCritic(boolean topCritic) { this.topCritic = topCritic; }

    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }

    public String getReviewType() { return reviewType; }
    public void setReviewType(String reviewType) { this.reviewType = reviewType; }

    public String getReviewScore() { return reviewScore; }
    public void setReviewScore(String reviewScore) { this.reviewScore = reviewScore; }

    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }

    public String getReviewContent() { return reviewContent; }
    public void setReviewContent(String reviewContent) { this.reviewContent = reviewContent; }
}
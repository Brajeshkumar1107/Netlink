package com.lumenore2.model2;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @NotNull @Email
    private String email;

    @NotBlank @NotNull
    private String ratingStar;

    private String ratingDetails;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRatingStar(String rating) {
        return ratingStar;
    }

    public void setRatingStar(String ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getRatingDetails() {
        return ratingDetails;
    }

    public void setRatingDetails(String ratingDetails) {
        this.ratingDetails = ratingDetails;
    }

    public Rating() {
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", ratingStar='" + ratingStar + '\'' +
                ", ratingDetails='" + ratingDetails + '\'' +
                '}';
    }
}

package com.lumenore2.service;

import com.lumenore2.exceptionhandler.DatabaseEmptyException;
import com.lumenore2.model2.Rating;
import com.lumenore2.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository repository;

    public Rating save(Rating rating) {
        repository.save(rating);
        return rating;
    }

    public List<Rating> getRatingByEmail(String email) {
        return repository.findAllByEmail(email);
    }

    public Rating getRatingById(Long id) {
        Rating rating =  repository.findById(id).orElseThrow(() -> new NullPointerException("No record found"));

        return rating;
    }

    public List<Rating> getAllRating() {
        List<Rating> ratingList= repository.findAll();
        return ratingList;
    }

    public Rating updateRating(Long id, Rating rating) {
        List<Rating> ratingList = repository.findAllByEmail(rating.getEmail());
        if (ratingList.isEmpty()) {
            repository.save(rating);
            return rating;
        }

        Rating updateRating = ratingList.get(0);
        if (updateRating == null) {
            throw new DatabaseEmptyException("No record in db to be deleted");
        }

        updateRating.setEmail(rating.getEmail());
        updateRating.setRatingStar(rating.getRatingStar());
        updateRating.setRatingDetails(rating.getRatingDetails());
        return repository.save(updateRating);
    }

    public void deletedById(Long id) {
        Rating rating = repository.findById(id).orElseThrow(() -> new NullPointerException("No record found"));
        repository.deleteById(id);
    }

    public void deleteAll() {
        if (repository.count() == 0)
            throw new DatabaseEmptyException("No record in db to be deleted");
        repository.deleteAll();
    }

}

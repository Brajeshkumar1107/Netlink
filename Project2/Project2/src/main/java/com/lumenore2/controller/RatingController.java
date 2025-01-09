package com.lumenore2.controller;

import com.lumenore2.model2.Rating;
import com.lumenore2.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RatingController {
    @Autowired
    private RatingService service;

    @PostMapping("/rating")
    public ResponseEntity<Rating> saveRating(@Valid @RequestBody Rating rating) {
        Rating r =  service.save(rating);
        return new ResponseEntity<>(r, HttpStatus.CREATED);
    }

    @GetMapping("/rating")
    public ResponseEntity<?> getAllRating() {
        List<Rating> ratingList = service.getAllRating();
        System.out.println(ratingList);
        return ratingList.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record in db") : ResponseEntity.of(Optional.of(ratingList));
    }

    @GetMapping("/rating/{id}")
    public ResponseEntity<?> getRatingBYId( @PathVariable Long id) {
        Rating rating = service.getRatingById(id);
        return rating == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record in db") : ResponseEntity.of(Optional.of(rating));
    }

    @GetMapping("/rating/email/{email}")
    public ResponseEntity<?> getRatingByEmail(@PathVariable String email) {
        List<Rating> rating = service.getRatingByEmail(email);
        return rating.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record in db") : ResponseEntity.of(Optional.of(rating));
    }

    @DeleteMapping("/rating/{id}")
    public ResponseEntity<String> deleteRatingById(@Valid @PathVariable Long id) {
        service.deletedById(id);
        return new ResponseEntity<>("Record deleted.", HttpStatus.OK);
    }

    @DeleteMapping("/rating")
    public ResponseEntity<String> deleteAll() {
        service.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Deleted all");
    }
}

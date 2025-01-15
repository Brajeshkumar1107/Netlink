package com.lumenore2.repository;

import com.lumenore2.model2.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByEmail(String email);

}

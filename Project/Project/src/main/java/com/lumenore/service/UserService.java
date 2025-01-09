package com.lumenore.service;

import java.util.*;

import com.lumenore.globalexception.DatabaseEmptyException;
import com.lumenore.model1.User;
import com.lumenore.repository.UserRepository;
import org.springframework.http.*;
import com.lumenore2.model2.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    public User addUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        repository.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer your_access_token"); // Example header
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Rating> userRating = user.getRating();
        System.out.println(userRating);
        Rating rating = userRating.get(0);
//        rating.setEmail(String.valueOf(userRating.get(0)));
//        rating.getRatingStar(String.valueOf(userRating.get(1)));
//        rating.setRatingDetails(String.valueOf(userRating.get(2)));

        HttpEntity<Rating> entity = new HttpEntity<>(rating, headers);

        ResponseEntity<Rating> response = restTemplate.exchange(
                "http://localhost:8081/rating",
                HttpMethod.POST,
                entity,
                Rating.class
                );
        return user;
    }

    public List<User> getAllUser() {
        List<User> userList = repository.findAll();

       for (User user: userList) {
           HttpHeaders headers = new HttpHeaders();
           headers.set("Authorization", "Bearer your_access_token"); // Example header
           headers.setContentType(MediaType.APPLICATION_JSON);

           // Create an HttpEntity with headers
           HttpEntity<String> entity = new HttpEntity<>(headers);

           // Call the external service using GET method
           ResponseEntity<?> response = restTemplate.exchange(
                   "http://localhost:8081/rating/email/" + user.getEmail(),
                   HttpMethod.GET,
                   entity,
                   ArrayList.class
           );

           // Extract the Rating object from the response
           List<Rating> rating = (List<Rating>) response.getBody();
           if (rating != null) {
               user.setRating(rating);
           }
       }

        return userList;
    }

    public User getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NullPointerException("User not found: " + id));

        // Prepare headers for external service call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer your_access_token"); // Example header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call the external service using GET method
        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:8081/rating/email/" + user.getEmail(),
                HttpMethod.GET,
                entity,
                ArrayList.class
        );

        // Extract the Rating object from the response
        List<Rating> rating = (List<Rating>) response.getBody();
        if (rating != null) {
            user.setRating(rating);
        }
        return user;
    }

    public User updateUser(Long id, User user) {
        User existingUser = repository.findById(id).orElseThrow(() -> new NullPointerException("User not found: " + id));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        repository.save(existingUser);

        // Prepare headers for external service call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer your_access_token"); // Example header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers
        HttpEntity<User> entity = new HttpEntity<>(existingUser, headers);

        // Call the external service using PUT method
        restTemplate.exchange(
                "http://localhost:8081/rating/" + id,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        return existingUser;
    }

    public void deleteUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NullPointerException("User not found: " + id));
        repository.delete(user);

        // Prepare headers for external service call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer your_access_token"); // Example header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call the external service using DELETE method
        restTemplate.exchange(
                "http://localhost:8081/rating/" + id,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }

    public void deleteAll() {
        if (repository.count() == 0) {
            throw new DatabaseEmptyException("No record in db to be deleted");
        }
        repository.deleteAll();

        // Call external API to notify about deletion
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer your_access_token"); // Example header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call the external service using DELETE method
        restTemplate.exchange(
                "http://localhost:8081/rating",
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }
}

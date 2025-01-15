package com.lumenore.service;

import java.util.*;
import java.util.stream.Collectors;

import com.lumenore.globalexception.DatabaseEmptyException;
import com.lumenore.model1.User;
import com.lumenore.repository.UserRepository;
import org.springframework.core.ParameterizedTypeReference;
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

    //POST METHOD
    public User addUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        repository.save(user);
        List<Rating> ratingList = user.getRatings();
        if (!ratingList.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            String token = "your_access_token";
            headers.set("Authorization", "Bearer " + token); // Example header
            headers.setContentType(MediaType.APPLICATION_JSON);

            List<Rating> userRating = user.getRatings();
            Rating rating = userRating.get(0);
            System.out.println(rating);

            HttpEntity<Rating> entity = new HttpEntity<>(rating, headers);
            ResponseEntity<Rating> response = restTemplate.exchange(
                    "http://localhost:8081/rating",
                    HttpMethod.POST,
                    entity,
                    Rating.class
            );
        }
        return user;
    }

    //    GET METHOD FOR ALL USER
    public List<User> getAllUser() {
        List<User> userList = repository.findAll();

        HttpHeaders headers = new HttpHeaders();
        String token = "your_access_token"; // Replace with actual token
        headers.set("Authorization", "Bearer " + token); // Authorization header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Use ParameterizedTypeReference to specify the response type
        ResponseEntity<List<Rating>> responseEntity = restTemplate.exchange(
                "http://localhost:8081/rating",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Rating>>() {}
        );

        // Getting all ratings from the response
        List<Rating> ratingList = responseEntity.getBody();

        // Ensure ratingList is not null before proceeding
        if (ratingList == null) {
            throw new IllegalStateException("Failed to fetch ratings");
        }

        // Group ratings by email
        Map<String, List<Rating>> emailToRatingsMap = ratingList.stream()
                .collect(Collectors.groupingBy(Rating::getEmail));

        // Map ratings to users
        userList.forEach(user -> {
            List<Rating> userRatings = emailToRatingsMap.getOrDefault(user.getEmail(), null);
            user.setRatings(userRatings); // Assign ratings to the user
        });

        return userList;
    }

    // GET METHOD FOR SINGLE USER BY ID
    public User getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NullPointerException("User not found: " + id));

        // Prepare headers for external service call
        HttpHeaders headers = new HttpHeaders();
        String token = "your_access_token";
        headers.set("Authorization", "Bearer "+ token); // Example header
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
            user.setRatings(rating);
        }
        return user;
    }

    //GET METHOD FOR SINGLE USER BY EMAIL
    public User getUserByEmail(String email) {
        User user = repository.findByEmail(email);
        if (user == null) return user;
        HttpHeaders headers = new HttpHeaders();
        String token = "your_access_token";
        headers.set("Authorization", "Bearer "+ token); // Example header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call the external service using GET method
        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:8081/rating/email/" + email,
                HttpMethod.GET,
                entity,
                ArrayList.class
        );

        // Extract the Rating object from the response
        List<Rating> rating = (List<Rating>) response.getBody();
        if (rating != null) {
            user.setRatings(rating);
        }
        return user;
    }

    //  PUT METHOD
    public User updateUser(Long id, User user) {
        User existingUser = repository.findById(id).orElseThrow(() -> new NullPointerException("User not found: " + id));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setId(id);

        List<Rating> ratings  = user.getRatings();
        Rating userRating = ratings.get(0);
        if (userRating != null) {
            // Prepare headers for external service call
            HttpHeaders headers = new HttpHeaders();
            String token = "your_access_token";
            headers.set("Authorization", "Bearer " + token); // Example header
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create an HttpEntity with headers
            HttpEntity<Rating> entity = new HttpEntity<>(userRating, headers);

            // Call the external service using PUT method
            Rating updatedUserRating = restTemplate.exchange(
                    "http://localhost:8081/rating/" + id,
                    HttpMethod.PUT,
                    entity,
                    Rating.class).getBody();
            user.setRatings((List<Rating>) updatedUserRating);
        }
        repository.save(existingUser);
        return user;
    }

    //    DELETE METHOD FOR SINGLE USER
    public void deleteUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NullPointerException("User not found: " + id));

        // Prepare headers for external service call
        HttpHeaders headers = new HttpHeaders();
        String token = "your_access_token";
        headers.set("Authorization", "Bearer " + token); // Example header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call the external service using DELETE method
        String response  = String.valueOf(restTemplate.exchange(
                "http://localhost:8081/rating/" + id,
                HttpMethod.DELETE,
                entity,
                String.class
        ));
        System.out.println(response);
        repository.delete(user);
    }

    //    DELETE ALL METHOD
    public void deleteAll() {
        if (repository.count() == 0) {
            throw new DatabaseEmptyException("No record in db to be deleted");
        }
        repository.deleteAll();

        // Call external API to notify about deletion
        HttpHeaders headers = new HttpHeaders();
        String token = "your_access_token";
        headers.set("Authorization", "Bearer " + token); // Example header
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

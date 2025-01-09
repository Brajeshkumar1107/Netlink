package com.lumenore.controller;

import com.lumenore.model1.User;
import com.lumenore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User u = service.addUser(user);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User u = service.getUserById(id);
        return new ResponseEntity<>(u, HttpStatus.FOUND);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUser(){
        List<User> userList = service.getAllUser();
        return userList.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No record in db") : ResponseEntity.of(Optional.of(userList));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User u = service.updateUser(id, user);
        return u == null? ResponseEntity.status(HttpStatus.NOT_FOUND).build(): ResponseEntity.of(Optional.of(u));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return new ResponseEntity<>("Record deleted.", HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteAll() {
        service.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Deleted all");
    }


}

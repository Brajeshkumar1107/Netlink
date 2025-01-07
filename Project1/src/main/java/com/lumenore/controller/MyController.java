package com.lumenore.controller;

import com.lumenore.entity.Users;
import com.lumenore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MyController {
    @Autowired
    private UserService service;

    @PostMapping("/users")
    public ResponseEntity<Users> createUser(@Valid @RequestBody Users users) {
         return new ResponseEntity<>(service.createUser(users), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable int id) throws NotFoundException {
        Users user = service.getUserById(id);
        return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.of(Optional.of(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUser(){
        List<Users> usersList = service.getAllUser();
        return usersList.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.of(Optional.of(usersList));

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Users> updateUsers(@Valid @RequestBody Users user, @PathVariable int id) throws NotFoundException {
        Users u =  service.updateUsers(user, id);
        System.out.println("User is null");
        if (u == null) {
            return ResponseEntity.of(Optional.of(u));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUsers(@PathVariable int id) {
        service.deleteUser(id);
        return new ResponseEntity<String>("User deleted.", HttpStatus.OK);

    }


}

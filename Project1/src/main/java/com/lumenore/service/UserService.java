package com.lumenore.service;

import com.lumenore.entity.Users;
import com.lumenore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Users createUser(Users users) {
        repository.save(users);
        return users;
    }

    public List<Users> getAllUser() {
        return repository.findAll();
    }

    public Users getUserById(int id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }
    public Users updateUsers(Users user, int id) throws NotFoundException {
        Users u = repository.findById(id).orElseThrow(() -> new NotFoundException());
        u.setEmail(user.getEmail());
        u.setName(user.getName());
        return repository.save(u);
    }

    public String deleteUser(int id) {
        Users u = repository.findById(id).orElse(null);

        if (u != null) {
            repository.delete(u);
            return "User deleted.";
        }

        return "User not found";
    }



}

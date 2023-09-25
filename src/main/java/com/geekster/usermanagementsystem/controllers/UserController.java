package com.geekster.usermanagementsystem.controllers;

import com.geekster.usermanagementsystem.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    List<User> userList;

    @Autowired
    public UserController(@Qualifier("listProvider") List<User> userList) {
        this.userList = userList;
    }

    // endpoint to add user (parses json)
    @PostMapping("/user/add")
    public String addUser(@RequestBody User user) {
        for(User current: userList) {
            if(current.getUserId().equals(user.getUserId()))
                return String.format("UserId %s already exists", user.getUserId());
        }


        userList.add(user);
        return String.format("User (id=%s) was added", user.getUserId());
    }

    @GetMapping("/user/get/{id}")
    public User getUserById(@PathVariable String id) {
        User user = getUser(id);

        if(user != null)
            return user;

        System.out.println("User with " + id + " does not exist");
        return null;
    }

    @GetMapping("/user/get/all")
    public List<User> getAllUsers() {
        return userList;
    }

    @PutMapping("/user/{id}/update")
    public String updateUser(
            @PathVariable String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phoneNumber
    ) {
        User updatedUser = getUser(id);

        if(updatedUser == null)
            return "invalid id";

        if(name != null)
            updatedUser.setName(name);

        if(userName != null)
            updatedUser.setUserName(userName);

        if(address != null)
            updatedUser.setAddress(address);

        if(phoneNumber != null)
            updatedUser.setPhoneNumber(phoneNumber);

        return "User info updated";
    }

    @PutMapping("/user/{id}/delete")
    public String deleteUserById(@PathVariable String id) {
        User deletedUser = getUser(id);

        if(deletedUser == null)
            return String.format("UserID %s does not exist", id);

        userList.remove(deletedUser);
        return String.format("UserID %s deleted successfully", id);
    }

    private User getUser(String id) {
        for(User user: userList) {
            if(user.getUserId().equals(id))
                return user;
        }

        return null;
    }
}

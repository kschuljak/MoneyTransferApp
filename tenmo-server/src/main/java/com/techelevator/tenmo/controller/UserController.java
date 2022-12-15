package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> list() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int userId) {
        return userDao.getUserById(userId);
    }

    @RequestMapping(path = "/?username={username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username) {
        return userDao.findByUsername(username);
    }
}

package org.axteel.chat.controller;

import org.axteel.chat.domain.Session;
import org.axteel.chat.domain.User;
import org.axteel.chat.repository.SessionRepository;
import org.axteel.chat.repository.UserRepository;
import org.axteel.chat.service.SessionService;
import org.axteel.chat.service.UserService;
import org.axteel.chat.util.ETO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ETO createUser(@RequestBody ETO encryptedUser) {
        System.out.println("[UserController.class]: createUser()");

        return userService.create(encryptedUser);
    }
    @PostMapping("/login")
    public ETO loginUser(@RequestBody ETO encryptedUser) {
        System.out.println("[UserController.class]: loginUser()");
        return userService.login(encryptedUser);
    }
}

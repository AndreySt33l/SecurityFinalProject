package org.axteel.chat.controller;

import org.axteel.chat.domain.Session;
import org.axteel.chat.repository.SessionRepository;
import org.axteel.chat.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping
    public Session getRandomSession() {
        System.out.println("[SessionController.class]: getRandomSession()");
        Session response = sessionService.getRandom();
        System.out.println("[SessionController.class]: " + response.getPublicId() + " to response");
        return response;
    }
}

package org.axteel.chat.service;

import org.axteel.chat.domain.Session;
import org.axteel.chat.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public Session getRandom() {
        System.out.println("[SessionServiceImpl.class]: getRandom()");
        List<Session> sessionList= sessionRepository.findAll();
        return sessionList.get( (int)(Math.random() * sessionList.size()) );
    }
}

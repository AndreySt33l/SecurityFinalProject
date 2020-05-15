package org.axteel.chat.component;

import org.axteel.chat.domain.Session;
import org.axteel.chat.repository.SessionRepository;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PublicSessionCreator implements CommandLineRunner {
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public void run(String...args) throws Exception {
        int size = 2;

        System.out.println("[PublicSessionCreator.class]: Creating " + size + " public sessions");


        for (int i = 0; i < size; i++) {
            Session session = new Session();
            sessionRepository.save(session);
        }
        System.out.println("[PublicSessionCreator.class]: Public sessions have been created");
    }
}
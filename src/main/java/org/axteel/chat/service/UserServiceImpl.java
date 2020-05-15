package org.axteel.chat.service;

import org.axteel.chat.domain.Session;
import org.axteel.chat.domain.User;
import org.axteel.chat.repository.SessionRepository;
import org.axteel.chat.repository.UserRepository;
import org.axteel.chat.util.ETO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ETO create(ETO encryptedUser) {
        String sessionId = encryptedUser.getPublicSessionId();
        Session session = sessionRepository.findByPublicId(sessionId);
        String privateKey = session.getPrivateKey();

        User user = (User) encryptedUser.decrypt(User.class, privateKey);
        System.out.println("Received User:\n" + user);
        User savedUser = userRepository.save(user);


        ETO response = ETO.encrypt(savedUser, encryptedUser.getPublicResponseKey());
        response.setPublicSessionId(session.getPublicId());
        return response;
    }

    @Override
    public ETO login(ETO encryptedUser) {
        String sessionId = encryptedUser.getPublicSessionId();
        Session session = sessionRepository.findByPublicId(sessionId);
        String privateKey = session.getPrivateKey();

        User user = (User) encryptedUser.decrypt(User.class, privateKey);
        User userInDb = userRepository.findByUserName(user.getUserName());

        if (userInDb == null) {
            User response = new User();
            response.setUserName("Access Denied");
            response.setPassword("Access Denied");
            return ETO.encrypt(response, encryptedUser.getPublicResponseKey());
        }

        if (!user.getPassword().equals(userInDb.getPassword())) {
            User response = new User();
            response.setUserName("Access Denied");
            response.setPassword("Access Denied");
            return ETO.encrypt(response, encryptedUser.getPublicResponseKey());
        }

        System.out.println("Access Granted to user:" + user.getUserName());
        return ETO.encrypt(userInDb, encryptedUser.getPublicResponseKey());
    }
}

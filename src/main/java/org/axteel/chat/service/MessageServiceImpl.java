package org.axteel.chat.service;

import org.axteel.chat.domain.Message;
import org.axteel.chat.domain.Session;
import org.axteel.chat.domain.User;
import org.axteel.chat.repository.MessageRepository;
import org.axteel.chat.repository.SessionRepository;
import org.axteel.chat.repository.UserRepository;
import org.axteel.chat.util.ETO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ETO createMessage(ETO eto) {
        String sessionId = eto.getPublicSessionId();
        Session session = sessionRepository.findByPublicId(sessionId);
        String privateKey = session.getPrivateKey();

        Message message = (Message) eto.decrypt(Message.class, privateKey);
        System.out.println("Received message: \n" + message);


        User sender = userRepository.findByUserName(message.getSender().getUserName());
        User receiver = userRepository.findByUserName(message.getReceiver().getUserName());

        message.setSender(sender);
        message.setReceiver(receiver);

        Message createdMessage  = messageRepository.save(message);

        ETO response = ETO.encrypt(createdMessage, eto.getPublicResponseKey());
        response.setPublicSessionId(eto.getPublicSessionId());

        System.out.println(response);
        return response;
    }

    public ETO getAllInbox(ETO eto) {
        String sessionId = eto.getPublicSessionId();
        Session session = sessionRepository.findByPublicId(sessionId);
        String privateKey = session.getPrivateKey();

        User user = (User)eto.decrypt(User.class, privateKey);

        User inDBUser = userRepository.findByUserName(user.getUserName());

        List<Message> inbox = messageRepository.findByReceiver(inDBUser);

        ETO response = ETO.encrypt(inbox, eto.getPublicResponseKey());

        return response;
    }

    @Override
    public ETO getAllSent(ETO eto) {
        String sessionId = eto.getPublicSessionId();
        Session session = sessionRepository.findByPublicId(sessionId);
        String privateKey = session.getPrivateKey();

        User user = (User)eto.decrypt(User.class, privateKey);

        User inDBUser = userRepository.findByUserName(user.getUserName());

        List<Message> inbox = messageRepository.findBySender(inDBUser);

        ETO response = ETO.encrypt(inbox, eto.getPublicResponseKey());

        return response;
    }
}

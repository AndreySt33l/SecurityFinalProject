package org.axteel.chat.tests;

import org.axteel.chat.domain.Message;
import org.axteel.chat.domain.Session;
import org.axteel.chat.domain.User;
import org.axteel.chat.util.ETO;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

public class SendMessage {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        sendMessage("Hi! Whats up? ;)","Andrey", "Alice");
        sendMessage("Hi. Nothing much", "Alice", "Andrey");
        sendMessage("How about to go to the club with us tonight?", "Andrey", "Alice");
        sendMessage("Sure", "Alice", "Andrey");
        sendMessage("Will be in your place at 10 :*", "Alice", "Andrey");
    }

    public static void sendMessage(String messageContent, String senderName, String receiverName) throws NoSuchAlgorithmException  {
        Session clientSession = new Session();
        clientSession.generate();
        String publicClientKey = clientSession.getPublicKey();

        RestTemplate template = new RestTemplate();
        Session serverSession = template.getForObject("http://localhost:8080/api/session", Session.class);
        System.out.println(serverSession);

        User sender = new User();
        sender.setUserName(senderName);

        User receiver = new User();
        receiver.setUserName(receiverName);

        Message message = new Message();
        message.setContent(messageContent);
        message.setSender(sender);
        message.setReceiver(receiver);

        ETO requestBody = ETO.encrypt(message, serverSession.getPublicKey());
        requestBody.setPublicSessionId(serverSession.getPublicId());
        requestBody.setPublicResponseKey(publicClientKey);

        ETO responseEto = template.postForObject("http://localhost:8080/api/message/send", requestBody, ETO.class);

        Message response = (Message) responseEto.decrypt(Message.class, clientSession.getPrivateKey());

        System.out.println(response);
    }
}

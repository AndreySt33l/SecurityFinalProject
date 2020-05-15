package org.axteel.chat.tests;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.axteel.chat.domain.Message;
import org.axteel.chat.domain.Session;
import org.axteel.chat.domain.User;
import org.axteel.chat.util.ETO;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ReceiveAllMessages {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Session clientSession = new Session();
        clientSession.generate();

        RestTemplate template = new RestTemplate();
        Session serverSession = template.getForObject("http://localhost:8080/api/session", Session.class);
        System.out.println(serverSession);

        User user = new User();
        user.setUserName("Andrey");

        ETO eto = ETO.encrypt(user, serverSession.getPublicKey());
        eto.setPublicResponseKey(clientSession.getPublicKey());
        eto.setPublicSessionId(serverSession.getPublicId());


        System.out.println(eto);

        ETO response = template.postForObject("http://localhost:8080/api/message/inbox", eto, ETO.class);
        ArrayList messages = (ArrayList) response.decrypt(ArrayList.class, clientSession.getPrivateKey());

        for (Object m : messages) {
            System.out.println(m);
            LinkedHashMap map = (LinkedHashMap) m;
            LinkedHashMap sender = (LinkedHashMap) map.get("sender");
            System.out.println("Content: " + map.get("content") + " : " +sender.get("userName"));
        }

    }
}

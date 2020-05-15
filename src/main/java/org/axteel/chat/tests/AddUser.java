package org.axteel.chat.tests;

import org.axteel.chat.domain.Session;
import org.axteel.chat.domain.User;
import org.axteel.chat.util.ETO;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

public class AddUser {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        create("Andrey", "qwerty");
        create("Alice", "qwerty");
        create("Bob", "qwerty");
    }

    public static void create (String username, String password ) throws NoSuchAlgorithmException {
        Session session = new Session();
        session.generate();
        String publicClientKey = session.getPublicKey();

        RestTemplate template = new RestTemplate();
        Session serverSession = template.getForObject("http://localhost:8080/api/session", Session.class);
        System.out.println(serverSession);

        User user = new User();
        user.setUserName(username);
        user.setPassword(password);

        System.out.println(user);

        ETO userEto = ETO.encrypt(user, serverSession.getPublicKey());
        userEto.setPublicSessionId(serverSession.getPublicId());
        userEto.setPublicResponseKey(publicClientKey);

        System.out.println(userEto);

        ETO response = template.postForObject("http://localhost:8080/api/user/register", userEto, ETO.class);

        User newUser = (User) response.decrypt(User.class, session.getPrivateKey());
        System.out.println(newUser);
    }
}

package org.axteel.chat.client.controller;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.axteel.chat.domain.Session;
import org.axteel.chat.domain.User;
import org.axteel.chat.util.ETO;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WebUnit {
    private RestTemplate restTemplate;
    private Session clientSession;
    private Session serverSession;

    public WebUnit(String url) throws NoSuchAlgorithmException {
        this.restTemplate = new RestTemplate();
        this.clientSession = new Session();
        this.clientSession.generate();
        this.serverSession = restTemplate.getForObject(url, Session.class);
    }

    public Object post(String url, Object body, Class cls) {
        return restTemplate.postForObject(url, body, cls);
    }

    public Object postAsEto(String url, Object body, Class responseClass) {
        ETO eto = ETO.encrypt(body, serverSession.getPublicKey());
        eto.setPublicSessionId(serverSession.getPublicId());
        eto.setPublicResponseKey(clientSession.getPublicKey());

        ETO response  = (ETO) post(url, eto, ETO.class);

        return response.decrypt(responseClass, clientSession.getPrivateKey());
    }

    public Object get(String url) {
        return restTemplate.getForObject(url, Object.class);
    }

    @Override
    public String toString() {
        return "WebUnit{" +
                "restTemplate=" + restTemplate +
                ", clientSession=" + clientSession +
                ", serverSession=" + serverSession +
                '}';
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        WebUnit unit = new WebUnit("http://localhost:8080/api/session");
        System.out.println(unit);

        User user = new User();
        user.setUserName("Alice");

        ArrayList response = (ArrayList) unit.postAsEto("http://localhost:8080/api/message/inbox", user, ArrayList.class);
        System.out.println(JSONArray.toJSONString(response));
    }
}

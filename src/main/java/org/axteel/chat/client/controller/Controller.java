package org.axteel.chat.client.controller;


import javafx.scene.Scene;
import javafx.stage.Stage;
import org.axteel.chat.client.scene.view.ControllableView;
import org.axteel.chat.domain.User;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Controller {
    private HashMap<String, ControllableView> views;
    private Stage stage;
    private WebUnit webUnit;
    private User currentUser;

    public Controller(Stage stage, String sessionUrl) throws NoSuchAlgorithmException {
        this.stage = stage;
        this.views = new LinkedHashMap<>();
        this.webUnit = new WebUnit(sessionUrl);
    }

    public WebUnit getWebUnit() {
        return webUnit;
    }

    public void setWebUnit(WebUnit webUnit) {
        this.webUnit = webUnit;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void addScene(String name, ControllableView view) {
        views.put(name, view);
    }


    public void render(String name) {
//        if (session == null) {
//            name = "no-connection";
//        }
        ControllableView view = views.get(name);
        Scene scene = view.render();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}

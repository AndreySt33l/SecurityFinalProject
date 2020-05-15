package org.axteel.chat.client;


import javafx.application.Application;
import javafx.stage.Stage;
import org.axteel.chat.client.controller.Controller;
import org.axteel.chat.client.scene.*;

public class AppMain extends Application {
    private final String url = "http://localhost:8080/api/session";

    @Override
    public void start(Stage stage) throws Exception {
        Controller controller = new Controller(stage, url);

        controller.addScene("login", new LoginView(controller));
        controller.addScene("registration", new RegistrationView(controller));
        controller.addScene("no-connection", new NoConnectionView(controller));
        controller.addScene("write-message", new WriteMessageView(controller));
        controller.addScene("inbox", new InboxView(controller));
        controller.addScene("sent", new SentView(controller));

        controller.render("login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

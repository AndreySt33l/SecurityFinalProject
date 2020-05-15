package org.axteel.chat.client.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.axteel.chat.client.controller.Controller;
import org.axteel.chat.client.scene.view.ControllableView;
import org.axteel.chat.domain.User;

public class RegistrationView extends ControllableView {
    private String registrationUrl = "http://localhost:8080/api/user/register";

    private Button login;
    private Button registration;

    public RegistrationView(Controller controller) {
        super(controller);
    }

    public Scene render() {
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text welcome = new Text("Registration Form");
        welcome.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        grid.add(welcome, 0, 0, 2, 1);

        Label userName = new Label("Enter username:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Enter password:");
        grid.add(pw, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        this.login = new Button("Log In");
        this.registration = new Button("Register Me!");

        registration.setOnAction(actionEvent -> {
            User user = new User();
            user.setUserName(userTextField.getText());
            user.setPassword(passwordField.getText());

            User regUser = (User)getController().getWebUnit().postAsEto(registrationUrl, user, User.class);

            System.out.println(regUser);
            //stage.setScene(new LoginScene(stage).getScene());
            getController().render("login");
        });

        login.setOnAction(actionEvent -> {
            System.out.println("Clicked");
            getController().render("login");
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(registration, login);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, 400, 275);

        return scene;
    }
}

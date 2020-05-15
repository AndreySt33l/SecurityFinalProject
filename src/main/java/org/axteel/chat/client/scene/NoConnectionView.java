package org.axteel.chat.client.scene;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.axteel.chat.client.controller.Controller;
import org.axteel.chat.client.scene.view.ControllableView;


public class NoConnectionView extends ControllableView {
    public NoConnectionView(Controller controller) {
        super(controller);
    }

    public Scene render() {
        Label label = new Label("Cannot establish secure connection with server :(");
        label.setAlignment(Pos.CENTER);
        Button retryBtn = new Button("Retry");
        retryBtn.setOnAction(action -> {
            //getController().getSecureConnection();
            getController().render("login");
        });
        retryBtn.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(label, retryBtn);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);

        AnchorPane root = new AnchorPane(vBox);

        // anchor to the label
        AnchorPane.setTopAnchor(vBox, 10.0);
        AnchorPane.setLeftAnchor(vBox, 10.0);
        AnchorPane.setRightAnchor(vBox, 10.0);
        AnchorPane.setBottomAnchor(vBox, 10.0);


        return new Scene(root, 800, 600);
    }
}

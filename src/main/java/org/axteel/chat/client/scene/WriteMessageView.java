package org.axteel.chat.client.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.axteel.chat.client.controller.Controller;
import org.axteel.chat.client.scene.view.ControllableView;
import org.axteel.chat.domain.Message;
import org.axteel.chat.domain.User;


public class WriteMessageView extends ControllableView {
    private String url = "http://localhost:8080/api/message/send";

    public WriteMessageView(Controller controller) {
        super(controller);
    }

    @Override
    public Scene render() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 20, 10));

        Label receiverLabel = new Label("Receiver:");
        gridPane.add(receiverLabel, 0, 0);

        TextField receiverText = new TextField();
        gridPane.add(receiverText, 1, 0);

        Label messageLabel = new Label("Message text:");
        gridPane.add(messageLabel, 0,1);

        TextArea messageText = new TextArea();
        gridPane.add(messageText, 0, 2, 2, 1);

        Button sendMessage = new Button("Send message");
        sendMessage.setOnAction(event -> {
            String receiverName = receiverText.getText();
            String messageContent = messageText.getText();
            User sender = getController().getCurrentUser();

            User receiver = new User();
            receiver.setUserName(receiverName);

            Message message = new Message();
            message.setReceiver(receiver);
            message.setSender(sender);
            message.setContent(messageContent);

            Message response = (Message)getController().getWebUnit().postAsEto(url, message, Message.class);
            System.out.println(response);
            getController().render("inbox");
        });

        Button goBack = new Button("Go Back");
        goBack.setOnAction(event -> {
            getController().render("inbox");
        });
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(goBack, sendMessage);

        gridPane.add(hBox, 0, 4, 2, 2);
        /*HBox hBox = new HBox(button);
        hBox.setAlignment(Pos.CENTER_LEFT);

        VBox vBox = new VBox(field, textArea, hBox);
        vBox.setSpacing(10);*/

        return new Scene(gridPane, 300, 300);
    }

}

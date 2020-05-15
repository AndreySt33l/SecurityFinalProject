package org.axteel.chat.client.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.axteel.chat.client.controller.Controller;
import org.axteel.chat.client.scene.view.ControllableView;
import org.axteel.chat.domain.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SentView extends ControllableView {
    String url = "http://localhost:8080/api/message/sent";

    public SentView(Controller controller) {
        super(controller);
    }

    @Override
    public Scene render() {
        ObservableList<String> list = FXCollections.observableArrayList(getMessages());
        ListView<String> listView = new ListView<String>(list);

        Button selectedValue = new Button("Read Selected Value");

        Button writeMessage = new Button("New message");

        Button toInbox = new Button("Inbox");
        toInbox.setOnAction(e -> {
            getController().render("inbox");
        });

        writeMessage.setOnAction(event -> {
            getController().render("write-message");
        });

        selectedValue.setOnAction(event -> {
            ObservableList selectedIndices = listView.getSelectionModel().getSelectedIndices();
            for(Object o : selectedIndices){
                System.out.println("o = " + o + " (" + o.getClass() + ")");
                System.out.println(listView.getItems().get((int)o)); }
        });

        VBox vBox = new VBox(listView);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        Button logOut = new Button("Log Out");
        logOut.setOnAction(event -> {
            getController().render("login");
        });

        HBox hBox = new HBox(logOut, selectedValue, writeMessage, toInbox);
        hBox.setSpacing(10);

        vBox.getChildren().add(hBox);

        return new Scene(vBox, 600, 300);
    }

    private List<String> getMessages() {
        User user = getController().getCurrentUser();

        ArrayList messages = (ArrayList) super.getController()
                .getWebUnit()
                .postAsEto(url, user, ArrayList.class);

        ArrayList<String> result = new ArrayList<>();

        for (Object m : messages) {
            LinkedHashMap message = (LinkedHashMap) m;
            LinkedHashMap receiver = (LinkedHashMap) message.get("receiver");
            String output = message.get("content").toString()
                    +" (To:" + receiver.get("userName") + ") [" + message.get("createdAt") +"]";
            result.add(output);
        }
        return result;
    }
}

package org.axteel.chat.client.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.minidev.json.JSONArray;
import org.axteel.chat.client.controller.Controller;
import org.axteel.chat.client.scene.view.ControllableView;
import org.axteel.chat.domain.Message;
import org.axteel.chat.domain.User;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class InboxView extends ControllableView {
    private String url = "http://localhost:8080/api/message/inbox";

    public InboxView(Controller controller) {
        super(controller);
    }

    @Override
    public Scene render() {
        ObservableList<String> list = FXCollections.observableArrayList(getMessages());
        ListView<String> listView = new ListView<String>(list);

        Button selectedValue = new Button("Read Selected Value");

        Button writeMessage = new Button("New message");

        Button toSent = new Button("Sent");
        toSent.setOnAction(e -> {
            getController().render("sent");
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

        HBox hBox = new HBox(logOut, selectedValue, writeMessage, toSent);
        hBox.setSpacing(10);

        vBox.getChildren().add(hBox);

        return new Scene(vBox, 600, 300);
    }

    private ArrayList<String> getMessages() {
        User user = getController().getCurrentUser();

        ArrayList<Message> messages = (ArrayList) getController().getWebUnit().postAsEto(url, user, ArrayList.class);

        ArrayList<String> mAsStrings = new ArrayList<>();

        for (Object m : messages) {
            LinkedHashMap message = (LinkedHashMap) m;
            LinkedHashMap sender = (LinkedHashMap) message.get("sender");

            Date time = new Date((Long)message.get("createdAt"));
            String s = message.get("content").toString() + " (From: "
                    + sender.get("userName") + ") [" + time + "]";
            System.out.println(s);
            mAsStrings.add(s);
        }

        return mAsStrings;
    }
}

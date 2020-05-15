package org.axteel.chat.client.scene.view;


import org.axteel.chat.client.controller.Controller;

public abstract class ControllableView implements Renderer {
    Controller controller;

    public ControllableView(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}

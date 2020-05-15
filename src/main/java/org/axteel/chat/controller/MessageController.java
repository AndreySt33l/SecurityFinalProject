package org.axteel.chat.controller;

import org.axteel.chat.service.MessageService;
import org.axteel.chat.util.ETO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ETO sendMessage(@RequestBody ETO message) {
        return messageService.createMessage(message);
    }

    @PostMapping("/inbox")
    public ETO getInbox(@RequestBody ETO etoUser) {
        return messageService.getAllInbox(etoUser);
    }

    @PostMapping("/sent")
    public ETO getSent(@RequestBody ETO etoUser) {return messageService.getAllSent(etoUser);}
}

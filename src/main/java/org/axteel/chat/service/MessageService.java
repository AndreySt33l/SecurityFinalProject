package org.axteel.chat.service;

import org.axteel.chat.util.ETO;

public interface MessageService {
    ETO createMessage(ETO eto);
    ETO getAllInbox(ETO eto);
    ETO getAllSent(ETO eto);
}

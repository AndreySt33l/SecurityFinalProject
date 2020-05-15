package org.axteel.chat.service;

import org.axteel.chat.util.ETO;

public interface UserService {
    ETO create(ETO user);
    ETO login(ETO user);
}

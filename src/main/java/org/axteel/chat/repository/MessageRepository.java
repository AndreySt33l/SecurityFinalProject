package org.axteel.chat.repository;

import org.axteel.chat.domain.Message;
import org.axteel.chat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiver(User user);
    List<Message> findBySender(User user);
}

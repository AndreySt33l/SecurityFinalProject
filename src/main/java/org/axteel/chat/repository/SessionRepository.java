package org.axteel.chat.repository;

import org.axteel.chat.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByPublicId(String publicId);
}

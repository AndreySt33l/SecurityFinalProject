package org.axteel.chat.domain;

import lombok.Data;
import org.axteel.chat.util.IdGenerator;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    private String publicId;

    @Column (nullable = false)
    private String content;

    @CreationTimestamp
    private Date createdAt;

    @ManyToOne (cascade = CascadeType.ALL)
    private User receiver;

    @ManyToOne (cascade = CascadeType.ALL)
    private User sender;

    @PrePersist
    public void onCreate() {
        this.publicId = IdGenerator.generateId();
    }
}

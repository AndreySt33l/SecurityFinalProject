package org.axteel.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.axteel.chat.util.IdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column (unique = true)
    private String publicId;

    @Column (unique = true, nullable = false)
    private String userName;

    @Column (nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany (mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> inbox;

    @JsonIgnore
    @OneToMany (mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> sent;

    @PrePersist
    public void onCreate() {
       this.publicId = IdGenerator.generateId();
    }
}

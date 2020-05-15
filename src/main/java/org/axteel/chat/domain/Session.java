package org.axteel.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.axteel.chat.util.IdGenerator;

import javax.persistence.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Data
@Entity
public class Session {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column (unique = true)
    private String publicId ;

    @Lob
    private String publicKey;

    @Lob
    @JsonIgnore
    private String privateKey;

    @PrePersist
    public void generate() throws NoSuchAlgorithmException {
        System.out.println("[Session.class]: Persisting new Session");

        int keySize = 2048;
        this.publicId = IdGenerator.generateId();

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(keySize, SecureRandom.getInstanceStrong());

        KeyPair keyPair = generator.generateKeyPair();

        this.privateKey = Base64.getUrlEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        this.publicKey = Base64.getUrlEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println("[Session.class]: Session (publicId = " + publicId + ") has been generated");
    }


}

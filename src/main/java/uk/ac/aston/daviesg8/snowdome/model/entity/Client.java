package uk.ac.aston.daviesg8.snowdome.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "clients")
@Data
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clientid")
    private Integer clientid;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}

package uk.ac.aston.daviesg8.snowdome.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

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

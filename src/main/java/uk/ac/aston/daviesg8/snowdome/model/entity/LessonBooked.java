package uk.ac.aston.daviesg8.snowdome.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "lessons_booked")
@IdClass(LessonBookedId.class)
@Data
public class LessonBooked implements Serializable {

  @Id
  @Column(name = "clientid")
  private Integer clientid;

  @Id
  @Column(name = "lessonid")
  private String lessonid;

}

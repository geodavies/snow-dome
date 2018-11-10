package uk.ac.aston.daviesg8.snowdome.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

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

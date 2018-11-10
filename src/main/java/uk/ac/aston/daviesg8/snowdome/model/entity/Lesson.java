package uk.ac.aston.daviesg8.snowdome.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "lessons")
@Data
public class Lesson implements Serializable {

    @Id
    @Column(name = "lessonid")
    private String lessonid;

    @Column(name = "description")
    private String description;

    @Column(name = "level")
    private Integer level;

    @Column(name = "startDateTime")
    private Timestamp startDateTime;

    @Column(name = "endDateTime")
    private Timestamp endDateTime;

}

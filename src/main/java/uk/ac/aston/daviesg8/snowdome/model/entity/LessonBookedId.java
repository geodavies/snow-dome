package uk.ac.aston.daviesg8.snowdome.model.entity;

import java.io.Serializable;
import lombok.Data;

@Data
class LessonBookedId implements Serializable {

  private Integer clientid;

  private String lessonid;

}

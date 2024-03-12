package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import swtp12.modulecrediting.model.CourseLeipzig;

/**
 * DTO for creating or updating an {@link CourseLeipzig}
 */
@Getter
@Setter
@NoArgsConstructor
public class CourseLeipzigDTO {
    private String courseName;
}

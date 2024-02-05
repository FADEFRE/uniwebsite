package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import swtp12.modulecrediting.dto.CourseLeipzigDTO;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CourseLeipzigServiceTest {

    @Mock
    CourseLeipzigRepository courseLeipzigRepository;

    @InjectMocks
    CourseLeipzigService courseLeipzigService;


    @Test
    void shouldGetAllCoursesLeipzig() {
        List<CourseLeipzig> coursesLeipzig = new ArrayList<>();
        coursesLeipzig.add(new CourseLeipzig());

        Mockito.when(courseLeipzigRepository.findAll()).thenReturn(coursesLeipzig);

        List<CourseLeipzig> expectedCoursesLeipzig = courseLeipzigService.getAllCoursesLeipzig();

        assertEquals(expectedCoursesLeipzig, coursesLeipzig);
    }

    @Test
    void shouldGetCourseLeipzigByName() {
        Optional<CourseLeipzig> coursesLeipzig = Optional.of(new CourseLeipzig("test course"));


        Mockito.when(courseLeipzigRepository.findByName("test course")).thenReturn(coursesLeipzig);

        CourseLeipzig expectedCoursesLeipzig = courseLeipzigService.getCourseLeipzigByName("test course");

        assertEquals(expectedCoursesLeipzig.getName(), coursesLeipzig.get().getName());


        Mockito.when(courseLeipzigRepository.findByName("test course fails")).thenReturn(Optional.empty());

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> { courseLeipzigService.getCourseLeipzigByName("test course fails"); });
        assertTrue(e.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldCreateCourseLeipzig() {
        String testName = "test course";
        CourseLeipzigDTO courseLeipzigDTO = new CourseLeipzigDTO();
        courseLeipzigDTO.setCourseName(testName);

        // Mock the behavior when saving a new course
        CourseLeipzig courseLeipzig = new CourseLeipzig(testName);
        Mockito.when(courseLeipzigRepository.save(any())).thenReturn(courseLeipzig);

        // First attempt to create the course should succeed
        String resultCreate = courseLeipzigService.createCourseLeipzig(courseLeipzigDTO);
        assertEquals(testName, resultCreate);

        // Mock the behavior when finding an existing course
        Optional<CourseLeipzig> optionalCourseLeipzig = Optional.of(courseLeipzig);
        Mockito.when(courseLeipzigRepository.findByName(testName)).thenReturn(optionalCourseLeipzig);

        // Second attempt to create the same course should return "exists"
        String resultDuplicate = courseLeipzigService.createCourseLeipzig(courseLeipzigDTO);
        assertEquals("exists", resultDuplicate);
    }


    @Test
    void shouldDeleteCourseLeipzig() {

    }

    @Test
    void testEditCourse() {

    }

    

    
}

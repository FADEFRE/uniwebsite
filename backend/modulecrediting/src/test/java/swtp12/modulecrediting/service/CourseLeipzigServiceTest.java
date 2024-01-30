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

import swtp12.modulecrediting.dto.CourseLeipzigEditDTO;
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
    void shouldCreateCourseLeipzig() {
        String testName = "test course";
        CourseLeipzigEditDTO courseLeipzigDTO = new CourseLeipzigEditDTO();

        ResponseStatusException e1 = assertThrows(ResponseStatusException.class, () -> { courseLeipzigService.createCourseLeipzig(null); });
        assertTrue(e1.getStatusCode().equals(HttpStatus.BAD_REQUEST));

        ResponseStatusException e2 = assertThrows(ResponseStatusException.class, () -> { courseLeipzigService.createCourseLeipzig(courseLeipzigDTO); });
        assertTrue(e2.getStatusCode().equals(HttpStatus.BAD_REQUEST));


        courseLeipzigDTO.setCourseName(testName);

        CourseLeipzig courseLeipzig = new CourseLeipzig(testName);
        Mockito.when(courseLeipzigRepository.save(any())).thenReturn(courseLeipzig);
        String expectedCourseLeipzigName = courseLeipzigService.createCourseLeipzig(courseLeipzigDTO);
        assertEquals(expectedCourseLeipzigName, testName);
        
        
        Optional<CourseLeipzig> optionalCourseLeipzig = Optional.of(new CourseLeipzig(testName));
        Mockito.when(courseLeipzigRepository.findByName(testName)).thenReturn(optionalCourseLeipzig);
        ResponseStatusException e3 = assertThrows(ResponseStatusException.class, () -> { courseLeipzigService.createCourseLeipzig(courseLeipzigDTO); });
        assertTrue(e3.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldDeleteCourseLeipzig() {

    }

    @Test
    void testEditCourse() {

    }

    

    
}

package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(courseLeipzigRepository.findAll()).thenReturn(coursesLeipzig);

        List<CourseLeipzig> expectedCoursesLeipzig = courseLeipzigService.getAllCoursesLeipzig();

        assertEquals(expectedCoursesLeipzig, coursesLeipzig);
    }

    @Test
    void shouldGetCourseLeipzigByName() {
        Optional<CourseLeipzig> coursesLeipzig = Optional.of(new CourseLeipzig("test course"));


        when(courseLeipzigRepository.findByName("test course")).thenReturn(coursesLeipzig);

        CourseLeipzig expectedCoursesLeipzig = courseLeipzigService.getCourseLeipzigByName("test course");

        assertEquals(expectedCoursesLeipzig.getName(), coursesLeipzig.get().getName());


        when(courseLeipzigRepository.findByName("test course fails")).thenReturn(Optional.empty());

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> { courseLeipzigService.getCourseLeipzigByName("test course fails"); });
        assertTrue(e.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldCreateCourseLeipzig() {
        String courseName = "test course";
        CourseLeipzigDTO courseLeipzigDTO = new CourseLeipzigDTO();
        courseLeipzigDTO.setCourseName(courseName);

        // Mock the behavior when the course does not exist
        when(courseLeipzigRepository.findByName(courseName)).thenReturn(Optional.empty());

        // Mock the behavior when saving a new course
        CourseLeipzig courseLeipzig = new CourseLeipzig(courseName);
        when(courseLeipzigRepository.save(any(CourseLeipzig.class))).thenReturn(courseLeipzig);

        // First attempt to create the course should succeed
        String resultCreate = courseLeipzigService.createCourseLeipzig(courseLeipzigDTO);
        assertEquals(courseName, resultCreate);

        // Mock the behavior when the course already exists and is active
        courseLeipzig.setIsActive(true);
        when(courseLeipzigRepository.findByName(courseName)).thenReturn(Optional.of(courseLeipzig));

        // Attempt to create the course when it already exists should throw a conflict exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseLeipzigService.createCourseLeipzig(courseLeipzigDTO);
        });

        // Assert the details of the exception
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("The Course already exists:" + courseName));

        // Mock the behavior when the course exists but is inactive
        courseLeipzig.setIsActive(false);
        when(courseLeipzigRepository.findByName(courseName)).thenReturn(Optional.of(courseLeipzig));

        // Attempt to create the course when it exists but is inactive should succeed (reactivate the course)
        String resultReactivate = courseLeipzigService.createCourseLeipzig(courseLeipzigDTO);
        assertEquals(courseName, resultReactivate);
    }
    
    
    @Test
    void shouldUpdateCourseLeipzig_Successfully() {
        CourseLeipzigRepository courseLeipzigRepository = mock(CourseLeipzigRepository.class);

        CourseLeipzig existingCourse = new CourseLeipzig();
        existingCourse.setIsActive(true);

        when(courseLeipzigRepository.findByName("OldCourseName")).thenReturn(Optional.of(existingCourse));

        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();
        courseLeipzigService.courseLeipzigRepository = courseLeipzigRepository;

        CourseLeipzigDTO courseLeipzigDTO = new CourseLeipzigDTO();
        courseLeipzigDTO.setCourseName("NewCourseName");

        String result = courseLeipzigService.updateCourseLeipzig("OldCourseName", courseLeipzigDTO);

        assertEquals("NewCourseName", result);
    }

    @Test
    void shouldUpdateCourseLeipzig_WithExistingName_ThrowsConflictException() {
        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();
        CourseLeipzigDTO courseLeipzigDTO = new CourseLeipzigDTO();
        CourseLeipzig existingCourse = new CourseLeipzig();
        existingCourse.setIsActive(true);
        courseLeipzigService.courseLeipzigRepository = mock(CourseLeipzigRepository.class);
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            courseLeipzigService.updateCourseLeipzig("OldCourseName", courseLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }

    @Test
    void shouldUpdateCourseLeipzig_WithInactiveCourse_ThrowsBadRequestException() {
        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();
        CourseLeipzigDTO courseLeipzigDTO = new CourseLeipzigDTO();
        CourseLeipzig inactiveCourse = new CourseLeipzig();
        inactiveCourse.setIsActive(false);
        courseLeipzigService.courseLeipzigRepository = mock(CourseLeipzigRepository.class);

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            courseLeipzigService.updateCourseLeipzig("OldCourseName", courseLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }

    @Test
    void shouldUpdateCourseLeipzig_WithBlankCourseName_ThrowsBadRequestException() {
        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();
        CourseLeipzigDTO courseLeipzigDTO = new CourseLeipzigDTO();
        courseLeipzigService.courseLeipzigRepository = mock(CourseLeipzigRepository.class);

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            courseLeipzigService.updateCourseLeipzig("OldCourseName", courseLeipzigDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }

    @Test
    void shouldUpdateCourseLeipzig_WithNullDTO_ThrowsBadRequestException() {
        
        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();
        courseLeipzigService.courseLeipzigRepository = mock(CourseLeipzigRepository.class);

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            courseLeipzigService.updateCourseLeipzig("OldCourseName", null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }

    @Test
    void shouldDeleteCourseLeipzig_Successfully() {
        CourseLeipzigRepository courseLeipzigRepository = mock(CourseLeipzigRepository.class);

        CourseLeipzig courseLeipzig = new CourseLeipzig();
        courseLeipzig.setId(1L);
        courseLeipzig.setIsActive(true);
        when(courseLeipzigRepository.findByName(anyString())).thenReturn(Optional.of(courseLeipzig));


        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();

        String result = courseLeipzigService.deleteCourseLeipzig("CourseName");

        assertEquals("DELETED", result);

        verify(courseLeipzigRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldDeactivateCourseLeipzig_Successfully() {
        CourseLeipzigRepository courseLeipzigRepository = mock(CourseLeipzigRepository.class);

        CourseLeipzig courseLeipzig = new CourseLeipzig();
        courseLeipzig.setId(1L);
        courseLeipzig.setIsActive(true);

        when(courseLeipzigRepository.findByName("CourseName")).thenReturn(Optional.of(courseLeipzig));
        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();
        courseLeipzigService.courseLeipzigRepository = courseLeipzigRepository;

        String result = courseLeipzigService.deleteCourseLeipzig("CourseName");

        assertEquals("DEACTIVATED", result);

        verify(courseLeipzigRepository, times(1)).save(courseLeipzig);
    }

    @Test
    void shouldThrowBadRequestException_WhenCourseIsAlreadyInactive() {
        CourseLeipzigService courseLeipzigService = new CourseLeipzigService();

        CourseLeipzig courseLeipzig = new CourseLeipzig();
        courseLeipzig.setIsActive(false);

        Throwable exception = assertThrows(NullPointerException.class, () -> {
            courseLeipzigService.deleteCourseLeipzig("CourseName");
        });
        assertEquals("Cannot invoke \"swtp12.modulecrediting.repository.CourseLeipzigRepository.findByName(String)\" because \"this.courseLeipzigRepository\" is null", exception.getMessage());
    }

    @Test
    void testEditCourse() {

    }

    

    
}

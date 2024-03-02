package swtp12.modulecrediting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import swtp12.modulecrediting.dto.LeipzigDataCourseDTO;
import swtp12.modulecrediting.dto.LeipzigDataDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JsonLeipzigDataServiceTest {

    @Mock
    private CourseLeipzigService courseLeipzigService;

    @Mock
    private ModuleLeipzigService moduleLeipzigService;

    @Test
    public void testGetAllLeipzigDataWithCourses() {
        JsonLeipzigDataService jsonLeipzigDataService = mock( JsonLeipzigDataService.class);
        LeipzigDataDTO leipzigDataDTO = mock(LeipzigDataDTO.class);

        when(jsonLeipzigDataService.getAllLeipzigData()).thenReturn(leipzigDataDTO);

        List<LeipzigDataCourseDTO> courses = new ArrayList<>();
        LeipzigDataCourseDTO courseDTO1 = new LeipzigDataCourseDTO();
        courseDTO1.setName("Course 1");
        LeipzigDataCourseDTO courseDTO2 = new LeipzigDataCourseDTO();
        courseDTO2.setName("Course 2");
        courses.add(courseDTO1);
        courses.add(courseDTO2);
        when(leipzigDataDTO.getCourses()).thenReturn(courses);

        LeipzigDataDTO result = jsonLeipzigDataService.getAllLeipzigData();

        assertEquals(2, result.getCourses().size());
        assertEquals("Course 1", result.getCourses().get(0).getName());
        assertEquals("Course 2", result.getCourses().get(1).getName());
    }

    @Test
    public void testGetAllLeipzigDataWithNoCourses() {
        LeipzigDataDTO leipzigDataDTO = mock(LeipzigDataDTO.class);
        when(leipzigDataDTO.getCourses()).thenReturn(new ArrayList<>());

        JsonLeipzigDataService jsonLeipzigDataService = mock(JsonLeipzigDataService.class);
        when(jsonLeipzigDataService.getAllLeipzigData()).thenReturn(leipzigDataDTO);

        LeipzigDataDTO result = jsonLeipzigDataService.getAllLeipzigData();

        assertNotNull(result);
        assertEquals(0, result.getCourses().size());
    }

    @Test
    public void testGetAllLeipzigDataWithEmptyModules() {
        LeipzigDataDTO leipzigDataDTO = mock(LeipzigDataDTO.class);
        when(leipzigDataDTO.getCourses()).thenReturn(new ArrayList<>());

        JsonLeipzigDataService jsonLeipzigDataService = mock(JsonLeipzigDataService.class);
        when(jsonLeipzigDataService.getAllLeipzigData()).thenReturn(leipzigDataDTO);

        LeipzigDataDTO result = jsonLeipzigDataService.getAllLeipzigData();

        assertNotNull(result);
        assertEquals(0, result.getCourses().size());
        if (!result.getCourses().isEmpty()) {
            assertEquals(0, result.getCourses().get(0).getModules().size());
        }
    }
}


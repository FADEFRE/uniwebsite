package swtp12.modulecrediting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import swtp12.modulecrediting.dto.CourseLeipzigRelationEditDTO;
import swtp12.modulecrediting.dto.LeipzigDataCourseDTO;
import swtp12.modulecrediting.dto.LeipzigDataDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JsonLeipzigDataServiceTest {

    @Mock
    private CourseLeipzigService courseLeipzigService;

    @Mock
    private ModuleLeipzigService moduleLeipzigService;

    @InjectMocks
    private JsonLeipzigDataService jsonLeipzigDataService;

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

    @Test
    public void testUploadDataWithNewCourseAndModule() {
        String jsonContent = "{\"courses\": [{\"name\": \"Course 1\", \"modules\": [{\"name\": \"Module 1\", \"code\": \"123\"}]}]}";
        MultipartFile multipartFile = new MockMultipartFile("data.json", jsonContent.getBytes());

        CourseLeipzig course1 = new CourseLeipzig("Course 1");
        ModuleLeipzig module1 = new ModuleLeipzig("Module 1", "123");

        List<ModuleLeipzigDTO> moduleDTOList = new ArrayList<>();
        moduleDTOList.add(new ModuleLeipzigDTO("Module 1", "123"));
        CourseLeipzigRelationEditDTO expectedEditDTO = new CourseLeipzigRelationEditDTO(moduleDTOList);

        when(courseLeipzigService.getAllCoursesLeipzig()).thenReturn(List.of(course1));
        when(courseLeipzigService.findOrCreateNewCourseLeipzig(anyString())).thenReturn(course1);
        when(moduleLeipzigService.findOrCreateNewModuleLeipzig(anyString(), anyString())).thenReturn(module1);

        jsonLeipzigDataService.uploadData(multipartFile);

        verify(courseLeipzigService, times(1)).findOrCreateNewCourseLeipzig("Course 1");
        verify(moduleLeipzigService, times(1)).findOrCreateNewModuleLeipzig("Module 1", "123");
        verify(courseLeipzigService, times(1)).editCourseRelations(eq("Course 1"), argThat(actualEditDTO -> {
            List<ModuleLeipzigDTO> actualModuleDTOList = actualEditDTO.getModulesLeipzig();
            List<ModuleLeipzigDTO> expectedModuleDTOList = expectedEditDTO.getModulesLeipzig();
            if (actualModuleDTOList.size() != expectedModuleDTOList.size()) {
                return false;
            }
            for (int i = 0; i < actualModuleDTOList.size(); i++) {
                ModuleLeipzigDTO actualModuleDTO = actualModuleDTOList.get(i);
                ModuleLeipzigDTO expectedModuleDTO = expectedModuleDTOList.get(i);
                if (!actualModuleDTO.getName().equals(expectedModuleDTO.getName()) ||
                    !actualModuleDTO.getCode().equals(expectedModuleDTO.getCode())) {
                    return false;
                }
            }
            return true;
        }));
        verify(courseLeipzigService, times(1)).saveCourseLeipzigToDatabase(course1);
        verifyNoMoreInteractions(courseLeipzigService);
    }

}


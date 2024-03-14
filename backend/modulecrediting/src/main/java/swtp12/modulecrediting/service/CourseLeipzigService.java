package swtp12.modulecrediting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import swtp12.modulecrediting.dto.CourseLeipzigDTO;
import swtp12.modulecrediting.dto.CourseLeipzigRelationEditDTO;
import swtp12.modulecrediting.dto.ModuleLeipzigDTO;
import swtp12.modulecrediting.model.Application;
import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.util.LogUtil;

/**
 * This is a {@code Service} for {@link CourseLeipzig}
 * @author Frederik Kluge
 * @author Luca Kippe
 * @see #getAllCoursesLeipzig
 * @see #getCourseLeipzigByName
 * @see #getCourseLeipzigNameById
 * @see #findOrCreateNewCourseLeipzig
 * @see #saveCourseLeipzigToDatabase
 * @see #createCourseLeipzig
 * @see #updateCourseLeipzig
 * @see #deleteCourseLeipzig
 * @see #editCourseRelations
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Service.html">Spring Service</a>
 */
@Service
public class CourseLeipzigService {
    private ModuleLeipzigService moduleLeipzigService;
    private ApplicationService applicationService;
    CourseLeipzigRepository courseLeipzigRepository;

    @Autowired
    public CourseLeipzigService(CourseLeipzigRepository courseLeipzigRepository, @Lazy ModuleLeipzigService moduleLeipzigService, @Lazy ApplicationService applicationService) {
        this.courseLeipzigRepository = courseLeipzigRepository;
        this.moduleLeipzigService = moduleLeipzigService;
        this.applicationService = applicationService;
    }
    public CourseLeipzigService() { } // This cannot!! be a lombok @NoArgsConstructor due to the way @Autowired works in Springboot


    /**
     * This method returns all {@link CourseLeipzig} in the database
     * @return {@code List} of {@link CourseLeipzig} 
     * @see CourseLeipzig
     */
    public List<CourseLeipzig> getAllCoursesLeipzig() {
        return courseLeipzigRepository.findAll();
    }

    /**
     * This method returns the {@link CourseLeipzig} with the given {@code name}
     * @param name {@code String}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if a {@link CourseLeipzig} with the given {@code name} could not be found
     * @return {@link CourseLeipzig} with the given {@code name}
     * @see CourseLeipzig
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public CourseLeipzig getCourseLeipzigByName(String name) {
        CourseLeipzig courseLeipzig = courseLeipzigRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Leipzig not found with given name: " + name));
        return courseLeipzig;
    }

    /**
     * This method returns the {@code name} of the {@link CourseLeipzig} with the given {@code id}
     * @param id {@code Long}
     * @throws ResponseStatusException with {@code HttpStatus.NOT_FOUND: 404} if a {@link CourseLeipzig} with the given {@code id} could not be found
     * @return {@code name} of the {@link CourseLeipzig} with the given {@code id}
     * @see CourseLeipzig
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String getCourseLeipzigNameById(Long id) {
        CourseLeipzig courseLeipzig = courseLeipzigRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Leipzig not found with given id: " + id));
        return courseLeipzig.getName();
    }

    /**
     * This method gets the {@link CourseLeipzig} with the given {@code name} or creates it
     * <p> If found, {@link CourseLeipzig} will be set as {@code isActive}
     * @param name {@code String}
     * @return {@link CourseLeipzig} that has been found or created
     * @see CourseLeipzig
     */
    public CourseLeipzig findOrCreateNewCourseLeipzig(String name) {
        CourseLeipzig courseLeipzig = courseLeipzigRepository.findByName(name).orElseGet(() -> {
            return courseLeipzigRepository.save(new CourseLeipzig(name));
        });
        LogUtil.printCourseLog(LogUtil.CourseType.CREATED, name, null);
        courseLeipzig.setIsActive(true);
        return courseLeipzig;
    }

    /**
     * This method saves the given {@link CourseLeipzig} to the database
     * @param courseLeipzig {@link CourseLeipzig}
     * @return {@link CourseLeipzig} that has been saved
     * @see CourseLeipzig
     */
    public CourseLeipzig saveCourseLeipzigToDatabase(CourseLeipzig courseLeipzig) {
        return courseLeipzigRepository.save(courseLeipzig);
    }

    /**
     * This method creates a new {@link CourseLeipzig} definied in the given {@link CourseLeipzigDTO}
     * @param courseLeipzigDTO {@link CourseLeipzigDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link CourseLeipzigDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code courseName} field in the {@link CourseLeipzigDTO} is {@code null} or {@code blank}
     * @throws ResponseStatusException with {@code HttpStatus.CONFLICT: 409} if a {@link CourseLeipzig} with the given {@code name} already exists and is active
     * @return {@code name} of the created {@link CourseLeipzig}
     * @see CourseLeipzig
     * @see CourseLeipzigDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String createCourseLeipzig(CourseLeipzigDTO courseLeipzigDTO) {
        if (courseLeipzigDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        Optional<CourseLeipzig> courseLeipzigOptional = courseLeipzigRepository.findByName(courseLeipzigDTO.getCourseName());
        if (courseLeipzigOptional.isPresent()) {
            CourseLeipzig courseLeipzig = courseLeipzigOptional.get();
            if(courseLeipzig.getIsActive()) 
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The Course already exists:" + courseLeipzigDTO.getCourseName() );

            courseLeipzig.setIsActive(true);
            LogUtil.printCourseLog(LogUtil.CourseType.REACTIVATED, courseLeipzig.getName(), null);
            courseLeipzigRepository.save(courseLeipzig);
            return courseLeipzig.getName();
        }

        CourseLeipzig courseLeipzig = new CourseLeipzig(courseLeipzigDTO.getCourseName());
        LogUtil.printCourseLog(LogUtil.CourseType.CREATED, courseLeipzig.getName(), null);
        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }

    /**
     * This method updates the {@link CourseLeipzig} with the given {@code name} with data definied in the given {@link CourseLeipzigDTO}
     * @param courseName {@code String}
     * @param courseLeipzigDTO {@link CourseLeipzigDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link CourseLeipzigDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code courseName} field in the {@link CourseLeipzigDTO} is {@code null} or {@code blank}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the to be updated {@link CourseLeipzig} is inactive
     * @throws ResponseStatusException with {@code HttpStatus.CONFLICT: 409} if a {@link CourseLeipzig} with the possible new {@code name} already exists
     * @return the (possible new) {@code name} of the updated {@link CourseLeipzig}
     * @see CourseLeipzig
     * @see CourseLeipzigDTO
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String updateCourseLeipzig(String courseName, CourseLeipzigDTO courseLeipzigDTO) {
        if (courseLeipzigDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if (courseLeipzigDTO.getCourseName() == null || courseLeipzigDTO.getCourseName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course name given");

        CourseLeipzig courseLeipzig = getCourseLeipzigByName(courseName);

        if(!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is inactive");

        Optional<CourseLeipzig> possibleConflictCourse = courseLeipzigRepository.findByName(courseLeipzigDTO.getCourseName());
        if (possibleConflictCourse.isPresent() && !courseName.equals(courseLeipzigDTO.getCourseName()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course with this name already exists");

        courseLeipzig.setName(courseLeipzigDTO.getCourseName());
        LogUtil.printCourseLog(LogUtil.CourseType.RENAMED, courseName, courseLeipzig.getName());
        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }

    /**
     * This method deletes or deactivates the {@link CourseLeipzig} with the given {@code name}. Deactivation happens if the {@link CourseLeipzig} has been used in a {@link Application}
     * @param name {@code String}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link CourseLeipzig} is already deactivated
     * @return {@code String} of the operation executed
     * @see Application
     * @see CourseLeipzig
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String deleteCourseLeipzig(String name) {
        CourseLeipzig courseLeipzig = getCourseLeipzigByName(name);
        if (!courseLeipzig.getIsActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig is already deactivated with name: " + name);

        courseLeipzig.setIsActive(false);

        if (!checkIfCourseIsUsedInApplications(courseLeipzig)) {
            LogUtil.printCourseLog(LogUtil.CourseType.DELETED, courseLeipzig.getName(), null);
            courseLeipzigRepository.deleteById(courseLeipzig.getId());
            return "DELETED";
        }

        LogUtil.printCourseLog(LogUtil.CourseType.DEACTIVATED, courseLeipzig.getName(), null);
        courseLeipzigRepository.save(courseLeipzig);
        return "DEACTIVATED";
    }

    /**
     * This method edits which {@link ModuleLeipzig} are part of the {@link CourseLeipzig} with the given {@code courseName}
     * @param courseName {@code String}
     * @param editCourseRelationsDTO {@link CourseLeipzigRelationEditDTO}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@link CourseLeipzigRelationEditDTO} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code courseName} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the to be updated {@link CourseLeipzig} is inactive
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code name} or {@code code} of any of the to be updated {@link ModuleLeipzig} is {@code null}
     * @throws ResponseStatusException with {@code HttpStatus.BAD_REQUEST: 400} if the {@code name} and {@code code} of any of the to be updated {@link ModuleLeipzig} do not correspond to the same {@link ModuleLeipzig}
     * @return {@code name} of the edited {@link CourseLeipzig}
     * @see CourseLeipzig
     * @see CourseLeipzigRelationEditDTO
     * @see ModuleLeipzig
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">Spring HttpStatus</a>
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html">Spring ResponseStatusException</a>
     */
    public String editCourseRelations(String courseName, CourseLeipzigRelationEditDTO editCourseRelationsDTO) {
        if(editCourseRelationsDTO == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data given");
        if(courseName == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Course Name sent");

        CourseLeipzig courseLeipzig = getCourseLeipzigByName(courseName);
        if(!courseLeipzig.getIsActive()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Leipzig with this name is deactivated: " + courseLeipzig.getName());

        List<ModuleLeipzig> modulesLeipzigToDelete = new ArrayList<>(courseLeipzig.getModulesLeipzigCourse());
        List<ModuleLeipzig> modulesLeipzigToAdd = new ArrayList<>();

        // add new modules
        if(editCourseRelationsDTO.getModulesLeipzig() != null) {
            for(ModuleLeipzigDTO mLDTO : editCourseRelationsDTO.getModulesLeipzig()) {
                if(mLDTO.getName() == null)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Name sent");
                if(mLDTO.getCode() == null)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Module Code sent");

                ModuleLeipzig moduleLeipzig = moduleLeipzigService.getModuleLeipzigByName(mLDTO.getName());

                if(!moduleLeipzig.getCode().equals(mLDTO.getCode()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Module Code doesn't match: " + mLDTO.getName() + " <-> " + mLDTO.getCode());

                modulesLeipzigToDelete = moduleLeipzigService.removeModulesFromList(modulesLeipzigToDelete, moduleLeipzig);
                modulesLeipzigToAdd.add(moduleLeipzig);
            }
        }

        for (ModuleLeipzig moduleLeipzig : modulesLeipzigToDelete) {
            LogUtil.printCourseLog(LogUtil.CourseType.REMOVED, courseLeipzig.getName(), moduleLeipzig.getName());
            courseLeipzig.removeModuleLeipzig(moduleLeipzig);
        }
        
        for (ModuleLeipzig moduleLeipzig : courseLeipzig.getModulesLeipzigCourse()) {
            modulesLeipzigToAdd = moduleLeipzigService.removeModulesFromList(modulesLeipzigToAdd, moduleLeipzig);
        }

        for (ModuleLeipzig moduleLeipzig : modulesLeipzigToAdd) {
            LogUtil.printCourseLog(LogUtil.CourseType.ADDED, courseLeipzig.getName(), moduleLeipzig.getName());
            courseLeipzig.addModulesLeipzig(moduleLeipzig);
        }

        courseLeipzigRepository.save(courseLeipzig);
        return courseLeipzig.getName();
    }

    
    // ------- Private Methods -------

    /**
     * This method checks if the given {@link CourseLeipzig} is used in any {@link Application}
     * @param courseLeipzig {@link CourseLeipzig}
     * @return {@code True} if the given {@link CourseLeipzig} is used in an {@link Application}
     * @see Application
     * @see CourseLeipzig
     */
    private Boolean checkIfCourseIsUsedInApplications(CourseLeipzig courseLeipzig) {
        List<Application> applications = applicationService.getAllApplciations();

        if(applications.isEmpty()) return false;

        for(Application application : applications) {
            if(courseLeipzig.equals(application.getCourseLeipzig())) return true;
        }

        return false;
    }

}

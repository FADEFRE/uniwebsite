
package swtp12.modulecrediting.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swtp12.modulecrediting.model.CourseLeipzig;
import swtp12.modulecrediting.model.ModuleLeipzig;
import swtp12.modulecrediting.repository.CourseLeipzigRepository;
import swtp12.modulecrediting.repository.ModuleLeipzigRepository;

@RestController
@RequestMapping("/modules-leipzig")
@CrossOrigin
public class ModuleLeipzigController {

    @Autowired
    private ModuleLeipzigRepository modulLeipzigRepository;

    @Autowired
    private CourseLeipzigRepository courseLeipzigRepository;


    @GetMapping
    List<ModuleLeipzig> getModulesLeipzig() {
        return modulLeipzigRepository.findAll();
    }





    /* 
    @GetMapping("/{id}")
    public ResponseEntity<ModuleLeipzig> getModuleLeipzigById(@PathVariable("id") Long id) {
        Optional<ModuleLeipzig> moduleLeipzig = modulLeipzigRepository.findById(id);
        if(moduleLeipzig.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(moduleLeipzig.get(), HttpStatus.OK);
    }

    @PostMapping("/{courseLeipzigId}/modules-leipzig")
    public ResponseEntity<ModuleLeipzig> createModuleLeipzig(@PathVariable("courseLeipzigId") Long id, @RequestBody ModuleLeipzig reqModuleLeipzig) {
        Optional<CourseLeipzig> optionalCourse = courseLeipzigRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CourseLeipzig courseLeipzig = optionalCourse.get();
        Optional<ModuleLeipzig> resModuleLeipzig = modulLeipzigRepository.findById(id);
        ModuleLeipzig moduleLeipzig;
        if (resModuleLeipzig.isEmpty()) {
            moduleLeipzig = modulLeipzigRepository.save(new ModuleLeipzig(reqModuleLeipzig.getModuleName(), reqModuleLeipzig.getModuleCode(), courseLeipzig.getName()));
            courseLeipzig.addCourseToModulesLeipzig(moduleLeipzig);
            courseLeipzigRepository.save(courseLeipzig);
            return new ResponseEntity<>(moduleLeipzig, HttpStatus.CREATED);
        }
        else {
            courseLeipzig.addCourseToModulesLeipzig(resModuleLeipzig.get());
            courseLeipzigRepository.save(courseLeipzig);
            return new ResponseEntity<>(resModuleLeipzig.get(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteModuleLeipzigById(@PathVariable("id") Long id) {
        modulLeipzigRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    */
}

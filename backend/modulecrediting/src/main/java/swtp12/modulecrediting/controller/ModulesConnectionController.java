package swtp12.modulecrediting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ModulesConnectionService;

@RestController
@RequestMapping("/api/modules-connection")
public class ModulesConnectionController {
    @Autowired
    ModulesConnectionService modulesConnectionService;

    @GetMapping("/{id}/related")
    @JsonView(Views.RelatedModulesConnection.class)
    @PreAuthorize("hasRole('ROLE_STUDY') or hasRole('ROLE_CHAIR')")
    ResponseEntity<List<ModulesConnection>> getRelatedModulesConnections(@PathVariable Long id) {
        return ResponseEntity.ok(modulesConnectionService.getRelatedModulesConnections(id));
    }
}

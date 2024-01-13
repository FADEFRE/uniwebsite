package swtp12.modulecrediting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp12.modulecrediting.model.ModulesConnection;
import swtp12.modulecrediting.model.Views;
import swtp12.modulecrediting.service.ModulesConnectionService;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/modules-connection")
public class ModulesConnectionController {
    @Autowired
    ModulesConnectionService modulesConnectionService;

    @GetMapping("/{id}/related")
    @JsonView(Views.RelatedModulesConnection.class)
    ResponseEntity<List<ModulesConnection>> getRelatedModulesConnections(@PathVariable Long id) {
        return ResponseEntity.ok(modulesConnectionService.getRelatedModulesConnections(id));
    }
}

package swtp12.modulecrediting.service;

import org.springframework.stereotype.Service;
import swtp12.modulecrediting.dto.ModulesConnectionStudentDTO;
import swtp12.modulecrediting.model.ModulesConnection;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModulesConnectionService {

    public List<ModulesConnectionStudentDTO> mapToModulesConnectionStudentDTOList(List<ModulesConnection> modulesConnections) {
        List<ModulesConnectionStudentDTO> modulesConnectionsStudentDTO = new ArrayList<>();
        for(ModulesConnection m : modulesConnections) {
            modulesConnectionsStudentDTO.add(new ModulesConnectionStudentDTO(m.getId(),m.getDecision(),m.getModuleApplication(),m.getModulesLeipzig()));
        }
        return modulesConnectionsStudentDTO;
    }

    public ModulesConnectionStudentDTO mapToModulesConnectionStudentDTO(ModulesConnection modulesConnection) {
        return new ModulesConnectionStudentDTO(modulesConnection.getId(),modulesConnection.getDecision(),modulesConnection.getModuleApplication(), modulesConnection.getModulesLeipzig());
    }
}

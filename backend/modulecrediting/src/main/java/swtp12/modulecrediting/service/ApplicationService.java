package swtp12.modulecrediting.service;

import org.springframework.stereotype.Service;
import swtp12.modulecrediting.dto.ModuleBlockDTO;

import java.util.List;

@Service
public class ApplicationService {
    public Integer createApplication(List<ModuleBlockDTO> moduleBlockDTOList) {
        for(ModuleBlockDTO m : moduleBlockDTOList) {
            System.out.println(m.toString());
        }

        return 1;
    }
}

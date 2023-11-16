package swtp12.modulecrediting.service;

import org.springframework.stereotype.Service;
import swtp12.modulecrediting.dto.ModuleBlockDTO;

import java.util.ArrayList;

@Service
public class ApplicationService {
    public Integer createApplication(ArrayList<ModuleBlockDTO> moduleBlockDTOList) {
        for(ModuleBlockDTO m : moduleBlockDTOList) {
            System.out.println(m.toString());
        }

        return 1;
    }
}

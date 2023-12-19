package swtp12.modulecrediting.service;

import org.springframework.stereotype.Service;

import swtp12.modulecrediting.dto.SignUpDTO;
import swtp12.modulecrediting.dto.UserDTO;

@Service
public class UserService {
    

    public UserDTO signUp(SignUpDTO user) {
        return new UserDTO(1L, "admin", "token");
    }

    public void signOut(UserDTO user) {
        // nothing to do at the moment
    }

}

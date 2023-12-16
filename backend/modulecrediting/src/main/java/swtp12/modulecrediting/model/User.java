package swtp12.modulecrediting.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
@Table(name = "app-user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private EnumUserRole role;


    public User(String username, String password, EnumUserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}

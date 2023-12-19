package swtp12.modulecrediting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String role; //enum?


    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}

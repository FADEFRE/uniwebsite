package swtp12.modulecrediting.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import swtp12.modulecrediting.dto.UserSummary;

@Entity
@Data
@Table(name = "app-user")
@NoArgsConstructor
public class User implements Serializable{
    @Id
    @GeneratedValue()
    private Long userId;

    private String username;

    private String password;

    private Boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
        inverseJoinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "roleId")}
    )
    private Role role;

    public User(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public UserSummary toUserSummary() {
        UserSummary userSummary = new UserSummary();
        userSummary.setUsername(this.username);
        userSummary.setUserId(this.userId);
        return userSummary;
    }

}

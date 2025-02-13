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

/**
 * User {@link Entity}. Implements {@link Serializable}.
 * 
 * @see UserSummary
 */
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "user_roles",
        joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
        inverseJoinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "roleId")}
    )
    private Role role;


    /**
     * Constructor for {@link User}.
     * <p>Creates {@link User} with given {@link String roleName}, {@link String password} and {@link Boolean enabled}.
     * 
     * @see User
     */
    public User(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    /**
     * Creates {@link UserSummary} with only the {@link Long userId} of this {@link User}.
     * 
     * @see User
     * @see UserSummary
     */
    public UserSummary toUserSummaryId() {
        UserSummary userSummary = new UserSummary();
        userSummary.setUserId(this.userId);
        return userSummary;
    }

    /**
     * Creates {@link UserSummary} with only the {@link String username} of this {@link User}.
     * 
     * @see User
     * @see UserSummary
     */
    public UserSummary toUserSummaryName() {
        UserSummary userSummary = new UserSummary();
        userSummary.setUsername(this.username);
        return userSummary;
    }

    /**
     * Creates {@link UserSummary} with only the {@link String username} of this {@link User}.
     * 
     * @see User
     * @see UserSummary
     */
    public UserSummary toUserSummary() {
        UserSummary userSummary = new UserSummary();
        userSummary.setUserId(this.userId);
        userSummary.setUsername(this.username);
        return userSummary;
    }


}

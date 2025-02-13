package swtp12.modulecrediting.model;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Role {@link Entity}. Implements {@link Serializable}.
 * 
 * @see GrantedAuthority
 */
@Entity
@NoArgsConstructor
@Data
public class Role implements Serializable {
    @Id
    @GeneratedValue()
    private Long roleId;

    private String roleName;

    /**
     * Returns {@link GrantedAuthority} of this {@link Role}.
     * 
     * @see Role
     * @see GrantedAuthority
     */
    public GrantedAuthority grantedAuthority() {
        return new SimpleGrantedAuthority(this.roleName);
    }


    /**
     * Constructor for {@link Role}.
     * <p>Creates {@link Role} with given {@link String roleName}.
     * 
     * @see Role
     */
    public Role(String roleName) {
        this.roleName = roleName;
    }
}

package swtp12.modulecrediting.model;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Role implements Serializable {
    @Id
    @GeneratedValue()
    private Long roleId;

    private String roleName;

    public GrantedAuthority grantedAuthority() {
        return new SimpleGrantedAuthority(this.roleName);
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }
}

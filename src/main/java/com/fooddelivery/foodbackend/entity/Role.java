package com.fooddelivery.foodbackend.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name="role_name", nullable = false,unique = true)
    private AppRole roleName;

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}

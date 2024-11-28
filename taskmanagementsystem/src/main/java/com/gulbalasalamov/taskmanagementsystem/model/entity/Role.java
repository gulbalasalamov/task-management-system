package com.gulbalasalamov.taskmanagementsystem.model.entity;

import com.gulbalasalamov.taskmanagementsystem.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

//    @OneToOne(mappedBy = "role")
//    @ToString.Exclude
//    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName ="id")
    private User user;


    @Override
    public String getAuthority() {
        return roleType.name();
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleType=" + roleType +
                '}';
    }
}

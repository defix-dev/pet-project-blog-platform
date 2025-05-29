package ru.defix.blog.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles", schema = "public", catalog = "blog-db")
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}

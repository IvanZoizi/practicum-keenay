package org.example.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.EmailValid;

import java.util.List;

@Entity
@Table(name = "teachers")
@NoArgsConstructor
@Data
public class Teachers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "email")
    @EmailValid
    private String email;

    @OneToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users user;

    @ManyToMany
    @JoinTable(
            name = "teachers_groups",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    private List<Groups> groups;
}

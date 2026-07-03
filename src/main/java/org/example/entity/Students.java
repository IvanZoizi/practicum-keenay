package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.EmailValid;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Data
public class Students {
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

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Groups group;

}

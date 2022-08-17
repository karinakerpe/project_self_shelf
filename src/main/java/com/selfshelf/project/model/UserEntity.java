package com.selfshelf.project.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@Table(name="user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true, length = 45)
    @Email(message = "Wrong e-mail")
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole userRole;


    @OneToMany(
            mappedBy = "userEntity",
            fetch = FetchType.LAZY
    )
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(
            mappedBy = "userEntity",
            fetch = FetchType.LAZY
    )
    private List<IssuedBook> issuedBooks = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }

}

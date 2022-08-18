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

    public UserEntity(Long id, String email, String password, String firstName, String lastName, UserRole userRole, List<Reservation> reservations, List<IssuedBook> issuedBooks) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.reservations = reservations;
        this.issuedBooks = issuedBooks;
    }
    public UserEntity() {
    }
}

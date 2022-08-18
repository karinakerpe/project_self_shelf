package com.selfshelf.project.model.DAO;

import com.selfshelf.project.model.IssuedBook;
import com.selfshelf.project.model.Reservation;
import com.selfshelf.project.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
public class UserEntityDao {

    private Long id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole userRole;

    private List<Reservation> reservations = new ArrayList<>();


    private List<IssuedBook> issuedBooks = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public UserEntityDao(Long id, String email, String password, String firstName, String lastName, UserRole userRole, List<Reservation> reservations, List<IssuedBook> issuedBooks) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.reservations = reservations;
        this.issuedBooks = issuedBooks;
    }
}

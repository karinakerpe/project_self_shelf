package com.selfshelf.project.service;

import com.selfshelf.project.model.*;
import com.selfshelf.project.repository.HistoryRepository;
import com.selfshelf.project.repository.ReservationRepository;
import com.selfshelf.project.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {
    private ArrayList<UserEntity> usersList = new ArrayList<>();
    private BookEntity book1 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);

    private Reservation reservation = new Reservation();
    private UserEntity userEntity2 = new UserEntity();
    private UserEntity userEntity3 = new UserEntity();

    @BeforeEach
    public void setUp() {
        book1.setId(1L);
        userEntity2.setEmail("cits@email.lv");
        userEntity2.setFirstName("Tests");
        userEntity2.setId(4L);
        userEntity2.setIssuedBooks(new ArrayList<>());
        userEntity2.setLastName("Testere");
        userEntity2.setPassword("password");
        userEntity2.setReservations(new ArrayList<>());
        userEntity2.setUserRole(UserRole.USER);

        userEntity3.setEmail("cits@email.lv");
        userEntity3.setFirstName("Tests");
        userEntity3.setId(4L);
        userEntity3.setIssuedBooks(new ArrayList<>());
        userEntity3.setLastName("Testere");
        userEntity3.setPassword("password");
        userEntity3.setReservations(new ArrayList<>());
        userEntity3.setUserRole(UserRole.USER);

        reservation.setBookEntity(book1);
        reservation.setHistoriesList(new ArrayList<>());
        reservation.setId(123L);
        reservation.setReservationEndDate(LocalDate.ofEpochDay(1L));
        reservation.setReservationStartDate(LocalDate.ofEpochDay(1L));
        reservation.setStatus(Status.ACTIVE);
        reservation.setUserEntity(userEntity2);

        usersList.add(userEntity2);


    }

    @After
    public void clearSetUp() {
        userEntity2.setId(4L);
        reservation.setBookEntity(book1);
        reservation.setStatus(Status.ACTIVE);
        usersList.clear();

    }
    @MockBean
    private BookService bookService;

    @MockBean
    private HistoryRepository historyRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;


    @Test
    void testSaveUser() {
        when(this.userRepository.save((UserEntity) any())).thenReturn(userEntity2);
        assertSame(userEntity2, this.userServiceImpl.saveUser(userEntity3));
        verify(this.userRepository).save((UserEntity) any());
        assertEquals(UserRole.USER, userEntity2.getUserRole());
    }

    @Test
    void testGetUserByEmail() {

        when(this.userRepository.findByEmail((String) any())).thenReturn(userEntity2);
        assertSame(userEntity2, this.userServiceImpl.getUserByEmail("cits@email.lv"));
        verify(this.userRepository).findByEmail((String) any());
    }


    @Test
    void getUserByEmail2() {
        when(this.userRepository.findByEmail((String) any())).thenThrow(new RuntimeException("Error!"));
        assertThrows(RuntimeException.class, () -> this.userServiceImpl.getUserByEmail("cits@email.lv"));
        verify(this.userRepository).findByEmail((String) any());
    }

    @Test
    void getUserByEmailEmpty() {
        when(this.userRepository.findByEmail((String) any())).thenReturn(userEntity3);
        assertNotSame(userEntity2, this.userServiceImpl.getUserByEmail("cits@email.ll"));
        verify(this.userRepository).findByEmail((String) any());
    }


    @Test
    void getUsersEmpty() {
        usersList.clear();
        when(this.userRepository.findAll()).thenReturn(usersList);
        List<UserEntity> actualUsers = this.userServiceImpl.getUsers();
        assertSame(usersList, actualUsers);
        assertTrue(actualUsers.isEmpty());
        verify(this.userRepository).findAll();
    }

    @Test
    void getUsers() {
        when(this.userRepository.findAll()).thenReturn(usersList);
        assertEquals(1,this.userServiceImpl.getUsers().size());
        verify(this.userRepository).findAll();
    }


    @Test
    void getUsersError() {
        when(this.userRepository.findAll()).thenThrow(new RuntimeException("Error!"));
        assertThrows(RuntimeException.class, () -> this.userServiceImpl.getUsers());
        verify(this.userRepository).findAll();
    }


    @Test
    void testGetById() {

        Optional<UserEntity> ofResult = Optional.of(userEntity2);
        when(this.userRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(userEntity2, this.userServiceImpl.getById(1L));
        verify(this.userRepository).findById((Long) any());
    }


    @Test
    void testGetById3() {
        when(this.userRepository.findById((Long) any())).thenThrow(new RuntimeException("Error!"));
        assertThrows(RuntimeException.class, () -> this.userServiceImpl.getById(123L));
        verify(this.userRepository).findById((Long) any());
    }


    @Test
    void update() {

        Optional<UserEntity> ofResult = Optional.of(userEntity2);


        when(this.userRepository.save((UserEntity) any())).thenReturn(userEntity3);
        when(this.userRepository.findById((Long) any())).thenReturn(ofResult);

        UserEntity userEntityTest = new UserEntity();

        userEntityTest.setEmail("cits@email.lv");
        userEntityTest.setFirstName("Tests");
        userEntityTest.setId(4L);
        userEntityTest.setIssuedBooks(new ArrayList<>());
        userEntityTest.setLastName("Testere");
        userEntityTest.setPassword("password");
        userEntityTest.setReservations(new ArrayList<>());
        userEntityTest.setUserRole(UserRole.USER);

        assertSame(userEntity3, this.userServiceImpl.update(123L, userEntityTest));
        verify(this.userRepository).save((UserEntity) any());
        verify(this.userRepository).findById((Long) any());
    }

    @Test
    void testUpdate4() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setIssuedBooks(new ArrayList<>());
        userEntity.setLastName("Doe");
        userEntity.setPassword("iloveyou");
        userEntity.setReservations(new ArrayList<>());
        userEntity.setUserRole(UserRole.ADMIN);
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setFirstName("Jane");
        userEntity1.setId(123L);
        userEntity1.setIssuedBooks(new ArrayList<>());
        userEntity1.setLastName("Doe");
        userEntity1.setPassword("iloveyou");
        userEntity1.setReservations(new ArrayList<>());
        userEntity1.setUserRole(UserRole.ADMIN);
        when(this.userRepository.save((UserEntity) any())).thenReturn(userEntity1);
        when(this.userRepository.findById((Long) any())).thenReturn(ofResult);
        UserEntity userEntity2 = mock(UserEntity.class);
        when(userEntity2.getUserRole()).thenThrow(new RuntimeException("An error occurred"));
        when(userEntity2.getFirstName()).thenThrow(new RuntimeException("An error occurred"));
        when(userEntity2.getLastName()).thenThrow(new RuntimeException("An error occurred"));
        when(userEntity2.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity2.getPassword()).thenReturn("iloveyou");
        doNothing().when(userEntity2).setEmail((String) any());
        doNothing().when(userEntity2).setFirstName((String) any());
        doNothing().when(userEntity2).setId((Long) any());
        doNothing().when(userEntity2).setIssuedBooks((java.util.List<IssuedBook>) any());
        doNothing().when(userEntity2).setLastName((String) any());
        doNothing().when(userEntity2).setPassword((String) any());
        doNothing().when(userEntity2).setReservations((java.util.List<Reservation>) any());
        doNothing().when(userEntity2).setUserRole((UserRole) any());
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setFirstName("Jane");
        userEntity2.setId(123L);
        userEntity2.setIssuedBooks(new ArrayList<>());
        userEntity2.setLastName("Doe");
        userEntity2.setPassword("iloveyou");
        userEntity2.setReservations(new ArrayList<>());
        userEntity2.setUserRole(UserRole.ADMIN);
        assertThrows(RuntimeException.class, () -> this.userServiceImpl.update(123L, userEntity2));
        verify(this.userRepository).findById((Long) any());
        verify(userEntity2).getEmail();
        verify(userEntity2).getFirstName();
        verify(userEntity2).getPassword();
        verify(userEntity2).setEmail((String) any());
        verify(userEntity2).setFirstName((String) any());
        verify(userEntity2).setId((Long) any());
        verify(userEntity2).setIssuedBooks((java.util.List<IssuedBook>) any());
        verify(userEntity2).setLastName((String) any());
        verify(userEntity2).setPassword((String) any());
        verify(userEntity2).setReservations((java.util.List<Reservation>) any());
        verify(userEntity2).setUserRole((UserRole) any());
    }
}

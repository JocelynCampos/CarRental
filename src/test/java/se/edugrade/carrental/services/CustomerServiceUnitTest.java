package se.edugrade.carrental.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.edugrade.carrental.entities.User;
import se.edugrade.carrental.repositories.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Skapat av Mohamed

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

    @Mock
    UserRepository userRepo;

    @InjectMocks
    UserService userService;

    @Test
    void findAll_users() {
        List<User> mockUsers = List.of(
                new User("11111111-1111", "Ali", "Mahmoud", "Address 1", "0700000001", "ali@test.com", List.of()),
                new User("22222222-2222", "Mona", "Ibrahim", "Address 2", "0700000002", "mona@test.com", List.of())
        );

        when(userRepo.findAll()).thenReturn(mockUsers);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        verify(userRepo).findAll();
    }

    @Test
    void save_user() {
        User user = new User("33333333-3333", "Sara", "Youssef", "Testgatan 5", "0701112222", "sara@test.com", List.of());

        when(userRepo.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertEquals(user, result);
        verify(userRepo).save(user);
    }
}

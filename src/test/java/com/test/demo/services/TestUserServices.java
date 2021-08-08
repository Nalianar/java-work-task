package com.test.demo.services;


import com.test.demo.form.UserForm;
import com.test.demo.model.User;
import com.test.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestUserServices {

    public static final String TEST_LOGIN_1 = "TEST_LOGIN_1";
    public static final String TEST_PASSWORD_1 = "TEST_PASSWORD_1";
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Captor
    ArgumentCaptor<User> userCapture;

    @Before
    public void init() {
    }

    @Test
    public void createUserTest() {
        UserForm testUserForm = new UserForm(TEST_LOGIN_1, TEST_PASSWORD_1);

        userService.createUser(testUserForm);

        Mockito.verify(userRepository).save(userCapture.capture());
        Assert.assertEquals(userCapture.getValue().getLogin() , testUserForm.getLogin());
    }
}

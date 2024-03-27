package com.webdev.cosmo.cosmobackend.features;

import com.webdev.cosmo.cosmobackend.BaseTestConfiguration;
import com.webdev.cosmo.cosmobackend.service.api.ROLE;
import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.external.facebook.FacebookService;
import com.webdev.cosmo.cosmobackend.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserModel;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserIT extends BaseTestConfiguration {

    @MockBean
    private UserService userService;

    @Test
    void testPostUser() {
        User user = new User().setName("John").setSurname("Doe").setEmail("john.doe@example.com").setRole(ROLE.USER).setCreation_date(LocalDateTime.now());
        UserModel userModel = new UserModel();
        userModel.setEmail("john.doe@example.com");
        userModel.setRole(org.openapitools.model.ROLE.USER);

        when(userService.save(any(User.class))).thenReturn(userModel);

        ResponseEntity<UserModel> response = testRestTemplate.postForEntity("/api/user", user, UserModel.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getBody().getRole()).isEqualTo(ROLE.USER);

    }

    @Test
    void testPutUser() {

    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userService).delete(any(Long.class));

        // Perform the DELETE request
        ResponseEntity<Void> response = testRestTemplate.exchange("/api/user/{id}", HttpMethod.DELETE, null, Void.class, userId);

        // Assert the response
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetUser() {

    }
}

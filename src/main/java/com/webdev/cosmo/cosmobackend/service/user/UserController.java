package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.openapitools.model.UserModel;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserModel postUser(@RequestBody User user){
        return userService.save(user);
    }

    @GetMapping(value = "/{email}")
    public UserModel getUser(@PathVariable String email){
        return userService.findByEmail(email);
    }

    @PutMapping
    public UserModel updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @DeleteMapping(value ="{email}")
    public Map<String, String> deleteUser(@PathVariable String email){
         userService.deleteByEmail(email);
        return new HashMap<>() {{
            put("email", email);
        }};
    }
}

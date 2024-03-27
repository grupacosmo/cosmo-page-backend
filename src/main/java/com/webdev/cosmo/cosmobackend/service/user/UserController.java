package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.openapitools.model.UserModel;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserModel postUser(@RequestParam("user") User user){
        return userService.save(user);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel getUser(@PathVariable String id){
        return userService.findById(id);
    }

    @PutMapping
    public UserModel putUser(@RequestParam("user") User user){
        return userService.putUser(user);
    }

    @DeleteMapping(value ="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteUser(@PathVariable String id){
        return userService.deleteById(id);
    }
}

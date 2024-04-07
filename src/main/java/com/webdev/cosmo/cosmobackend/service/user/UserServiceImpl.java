package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.common.mapper.UserMapper;
import com.webdev.cosmo.cosmobackend.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.UserModel;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserModel save(User user) {
        User savedUser = userRepository.save(user);

        return userMapper.mapToModel(Optional.of(savedUser));
    }

    public UserModel findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
            return userMapper.mapToModel(user);
        else throw new RuntimeException("Could not find user");
    }

    public UserModel updateUser(User user) {
        if(userRepository.existsById(user.getId())){
            userRepository.deleteById(user.getId());
            userRepository.save(user);
            return userMapper.mapToModel(Optional.of(user));
        }
        else throw new RuntimeException("Could not put user");
    }

    public String deleteByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            userRepository.deleteById(user.get().getId());
            return user.get().getEmail();
        }
        else throw new RuntimeException("Could not delete user by email");
    }
}

package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.common.mapper.UserMapper;
import com.webdev.cosmo.cosmobackend.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.UserModel;
import java.util.Optional;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_REQUEST;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserModel save(User user) {
        User savedUser = userRepository.save(user);

        return userMapper.mapToModel(Optional.of(savedUser));
    }

    @Override
    public UserModel findByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(INVALID_REQUEST::getError));
        return userMapper.mapToModel(user);
    }

    @Override
    public UserModel updateUser(User updatedUser) {
        User user = userRepository.findByEmail(updatedUser.getEmail())
                .orElseThrow(INVALID_REQUEST::getError);

        user.setEmail(updatedUser.getEmail())
                .setRole(updatedUser.getRole())
                .setId(updatedUser.getId())
                .setSurname(updatedUser.getSurname())
                .setName(updatedUser.getName())
                .setCreationDate(updatedUser.getCreationDate());

        userRepository.save(user);

        return userMapper.mapToModel(Optional.of(user));
    }

    @Override
    public void deleteByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(INVALID_REQUEST::getError);
        userRepository.deleteById(user.get().getId());
    }
}

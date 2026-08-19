package com.webdev.cosmo.cosmobackend.service.user;

import com.webdev.cosmo.cosmobackend.service.api.User;
import com.webdev.cosmo.cosmobackend.service.common.mapper.UserMapper;
import com.webdev.cosmo.cosmobackend.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.UserModel;
import org.springframework.stereotype.Service;

import static com.webdev.cosmo.cosmobackend.error.Error.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserModel save(User user) {
        User savedUser = userRepository.save(user);

        return userMapper.mapToModel(savedUser);
    }

    @Override
    public UserModel findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(INVALID_REQUEST::getError);
        return userMapper.mapToModel(user);
    }

    @Override
    public UserModel updateUser(User updatedUser) {
        User user = userRepository.findByEmail(updatedUser.getEmail())
                .orElseThrow(INVALID_REQUEST::getError);

        user.setRole(updatedUser.getRole())
                .setSurname(updatedUser.getSurname())
                .setName(updatedUser.getName());

        if (updatedUser.getCreationDate() != null) {
            user.setCreationDate(updatedUser.getCreationDate());
        }

        userRepository.save(user);

        return userMapper.mapToModel(user);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(INVALID_REQUEST::getError);
        userRepository.deleteById(user.getId());
    }
}
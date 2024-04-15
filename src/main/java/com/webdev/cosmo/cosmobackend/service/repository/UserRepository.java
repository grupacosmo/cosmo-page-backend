package com.webdev.cosmo.cosmobackend.service.repository;

import com.webdev.cosmo.cosmobackend.service.api.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}

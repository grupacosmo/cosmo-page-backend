package com.webdev.cosmo.cosmobackend.service.repository;

import com.webdev.cosmo.cosmobackend.service.api.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

}

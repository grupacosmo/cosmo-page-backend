package com.webdev.cosmo.cosmobackend.service.internal.image.repository;

import com.webdev.cosmo.cosmobackend.service.api.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}

package com.webdev.cosmo.cosmobackend.service.repository;

import com.webdev.cosmo.cosmobackend.service.api.Image;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends R2dbcRepository<Image, String> {
}

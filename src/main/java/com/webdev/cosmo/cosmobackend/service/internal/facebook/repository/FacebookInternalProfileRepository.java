package com.webdev.cosmo.cosmobackend.service.internal.facebook.repository;

import com.webdev.cosmo.cosmobackend.service.api.InternalPageSecret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacebookInternalProfileRepository extends JpaRepository<InternalPageSecret, String> {
}

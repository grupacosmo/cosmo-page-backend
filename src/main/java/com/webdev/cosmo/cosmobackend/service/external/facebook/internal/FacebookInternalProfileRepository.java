package com.webdev.cosmo.cosmobackend.service.external.facebook.internal;

import com.webdev.cosmo.cosmobackend.service.api.FacebookInternalProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacebookInternalProfileRepository extends JpaRepository<FacebookInternalProfile, String> {
}

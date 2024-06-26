package com.webdev.cosmo.cosmobackend.util.interfaces;

import org.springframework.data.domain.Page;

public interface PageableQueryService<T> {
    Page<T> findAll(int page, int size);
}

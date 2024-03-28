package com.webdev.cosmo.cosmobackend.service.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Profile {
    LOCAL("local"),
    PROD("prod");

    private final String profileName;
}

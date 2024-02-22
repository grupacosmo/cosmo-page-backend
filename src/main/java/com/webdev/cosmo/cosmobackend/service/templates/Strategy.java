package com.webdev.cosmo.cosmobackend.service.templates;

public interface Strategy<T, R> {
    T run(R params);
}

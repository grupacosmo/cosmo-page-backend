package com.webdev.cosmo.cosmobackend.service.external.webhook;

public interface Strategy<T, R> {
    T run(R params);
}

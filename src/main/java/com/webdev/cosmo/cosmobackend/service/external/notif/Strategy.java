package com.webdev.cosmo.cosmobackend.service.external.notif;

public interface Strategy<T, R> {
    T run(R params);
}

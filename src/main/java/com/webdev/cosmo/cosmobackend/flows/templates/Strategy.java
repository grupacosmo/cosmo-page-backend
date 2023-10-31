package com.webdev.cosmo.cosmobackend.flows.templates;

public interface Strategy<T, R> {
    T run(R params);
}

package com.webdev.cosmo.cosmobackend.util.interfaces;

public interface Validator<RQ> {
    boolean exists(RQ rq);
}

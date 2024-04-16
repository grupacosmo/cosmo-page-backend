package com.webdev.cosmo.cosmobackend.util.templates;

public interface Validator<RQ> {
    boolean exists(RQ rq);
}

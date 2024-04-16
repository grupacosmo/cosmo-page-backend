package com.webdev.cosmo.cosmobackend.util.templates;

import org.apache.commons.lang3.NotImplementedException;

public interface UpdateService <RQ, RS> {
    default RS update(RQ rq) {
        throw new NotImplementedException();
    }
}

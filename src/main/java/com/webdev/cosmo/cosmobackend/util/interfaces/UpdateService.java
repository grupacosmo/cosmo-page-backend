package com.webdev.cosmo.cosmobackend.util.interfaces;

import org.apache.commons.lang3.NotImplementedException;

public interface UpdateService <RQ, RS, ID> {
    default RS update(RQ rq, ID id) {
        throw new NotImplementedException();
    }
}

package com.webdev.cosmo.cosmobackend.util.templates;

import org.apache.commons.lang3.NotImplementedException;

public interface DeleteService<RQ, RS> {
    default RS delete(RQ rq) {
        throw new NotImplementedException();
    }
}

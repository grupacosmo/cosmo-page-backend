package com.webdev.cosmo.cosmobackend.util.templates;

public interface CrudService<RQ, RS, ID> extends SaveService<RQ, RS>,
        QueryService<RS, ID>,
        UpdateService<RQ, RS>, DeleteService<RQ,RS>
{ }

package org.currs.model;

import org.glassfish.hk2.api.Factory;

/**
 * Factory that knows how to create Repository for dependency injection
 */
public class RepositoryFactory implements Factory<IRepository> {

    public RepositoryFactory() {
    }

    @Override
    public IRepository provide() {
       return new Repository();
    }

    @Override
    public void dispose(IRepository r) {
    }
}

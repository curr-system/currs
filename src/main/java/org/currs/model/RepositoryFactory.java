package org.currs.model;

import org.glassfish.hk2.api.Factory;

/**
 * Factory that knows how to create Repository for dependency injection
 */
public class RepositoryFactory implements Factory<IRepository> {

    private Repository repository;

    public RepositoryFactory() {
        repository = new Repository();
    }

    @Override
    public IRepository provide() {
       repository.Connect();
       return repository;
    }

    @Override
    public void dispose(IRepository r) {
        if (repository != null)
            repository.Disconnect();
    }
}

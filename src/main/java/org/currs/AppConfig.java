package org.currs;

import org.currs.model.IRepository;
import org.currs.model.RepositoryFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Application configuration class
 */
public class AppConfig extends ResourceConfig {

  public AppConfig() {
    register(new AbstractBinder() {
       @Override
       protected  void configure() {
          bindFactory(RepositoryFactory.class).to(IRepository.class)
            .proxy(true).proxyForSameScope(false).in(RequestScoped.class);
       }
    });
  }
}

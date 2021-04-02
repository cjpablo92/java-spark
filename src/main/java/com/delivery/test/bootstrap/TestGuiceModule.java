package com.pedidosya.test.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pedidosya.test.review.*;

public class TestGuiceModule extends AbstractModule {

    private static Injector injector;

    private TestGuiceModule() {
        super();
    }

    public static Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new TestGuiceModule());
        }
        return injector;
    }

    @Override
    protected void configure() {
        bind(ReviewService.class).to(ReviewServiceImpl.class);
        bind(ReviewCacheService.class).to(ReviewCacheServiceImpl.class);
        bind(ReviewRepository.class).to(ReviewRepositoryImpl.class);
    }
}

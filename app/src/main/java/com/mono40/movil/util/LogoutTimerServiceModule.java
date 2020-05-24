package com.mono40.movil.util;

import dagger.Module;
import dagger.Provides;

@Module
public class LogoutTimerServiceModule {
    LogoutTimerService service;

    LogoutTimerServiceModule(LogoutTimerService service) {
        this.service = service;
    }

    @Provides
    LogoutTimerService provideLogoutTimerService() {
        return service;
    }
}

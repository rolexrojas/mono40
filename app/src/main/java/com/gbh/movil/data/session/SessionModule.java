package com.gbh.movil.data.session;

import android.content.Context;

import com.gbh.movil.domain.session.SessionRepo;
import com.gbh.movil.domain.session.SessionService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
public class SessionModule {
  /**
   * TODO
   *
   * @param context
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  SessionRepo provideSessionRepo(Context context) {
    return new SharedPreferencesSessionRepo(context);
  }

  /**
   * TODO
   *
   * @param retrofit
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  SessionService provideSessionService(Retrofit retrofit) {
    return new RetrofitSessionService(retrofit);
  }
}

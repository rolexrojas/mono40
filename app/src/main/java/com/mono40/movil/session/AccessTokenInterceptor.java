package com.mono40.movil.session;

import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * {@link Interceptor} implementation that adds an authorization header to each {@link Request
 * request}.
 * <p>
 * Also saves any access token found as a header of each {@link Response response}.
 *
 * @author hecvasro
 */
public final class AccessTokenInterceptor implements Interceptor {

  public static AccessTokenInterceptor create(AccessTokenStore accessTokenStore) {
    return new AccessTokenInterceptor(accessTokenStore);
  }

  private final AccessTokenStore accessTokenStore;

  private AccessTokenInterceptor(AccessTokenStore accessTokenStore) {
    this.accessTokenStore = ObjectHelper.checkNotNull(accessTokenStore, "accessTokenStore");
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request.Builder requestBuilder = chain.request()
      .newBuilder();

    // Adds the current access token as a header, if any.
    if (this.accessTokenStore.isSet()) {
      requestBuilder.header("Authorization", String.format("Bearer %1$s", accessTokenStore.get()));
    }

    final Response response = chain.proceed(requestBuilder.build());

    // Saves the new access token token, if any.
    final String newAccessToken = response.header("token");
    if (!StringHelper.isNullOrEmpty(newAccessToken)) {
      this.accessTokenStore.set(newAccessToken);
    }

    return response;
  }
}

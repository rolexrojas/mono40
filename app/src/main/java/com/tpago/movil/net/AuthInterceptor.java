package com.tpago.movil.net;

import com.tpago.movil.session.AccessTokenManager;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

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
final class AuthInterceptor implements Interceptor {

  static AuthInterceptor create(AccessTokenManager accessTokenManager) {
    return new AuthInterceptor(accessTokenManager);
  }

  private final AccessTokenManager accessTokenManager;

  private AuthInterceptor(AccessTokenManager accessTokenManager) {
    this.accessTokenManager = ObjectHelper.checkNotNull(accessTokenManager, "accessTokenManager");
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request.Builder requestBuilder = chain.request()
      .newBuilder();

    // Adds the current access token as a header, if any.
    final String currentAccessToken = this.accessTokenManager.get();
    if (!StringHelper.isNullOrEmpty(currentAccessToken)) {
      requestBuilder.header("Authorization", String.format("Bearer %1$s", currentAccessToken));
    }

    final Response response = chain.proceed(requestBuilder.build());

    // Saves the new access token token, if any.
    final String newAccessToken = response.header("token");
    if (!StringHelper.isNullOrEmpty(newAccessToken)) {
      this.accessTokenManager.set(newAccessToken);
    }

    return response;
  }
}

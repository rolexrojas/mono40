package com.tpago.movil.data.net;

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
 * Also saves any {@link AuthToken authorization token} found as a header of each {@link Response
 * response}.
 *
 * @author hecvasro
 */
final class AuthInterceptor implements Interceptor {

  static AuthInterceptor create(AuthTokenStore authTokenStore) {
    return new AuthInterceptor(authTokenStore);
  }

  private final AuthTokenStore authTokenStore;

  private AuthInterceptor(AuthTokenStore authTokenStore) {
    this.authTokenStore = ObjectHelper.checkNotNull(authTokenStore, "authTokenStore");
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request.Builder requestBuilder = chain.request()
      .newBuilder();

    // Adds the current authorization token as a header, if any.
    final AuthToken currentAuthToken = this.authTokenStore.get();
    if (ObjectHelper.isNotNull(currentAuthToken)) {
      requestBuilder.header("Authorization", currentAuthToken.value());
    }

    final Response response = chain.proceed(requestBuilder.build());

    // Saves the new authorization token, if any.
    final String newAuthToken = response.header("token");
    if (!StringHelper.isNullOrEmpty(newAuthToken)) {
      this.authTokenStore.set(AuthToken.create(newAuthToken));
    }

    return response;
  }
}

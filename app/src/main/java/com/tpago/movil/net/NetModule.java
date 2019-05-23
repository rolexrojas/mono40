package com.tpago.movil.net;

import android.content.Context;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.io.FileHelper;
import com.tpago.movil.session.AccessTokenInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author hecvasro
 */
@Module
public final class NetModule {

    private static Interceptor createLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static Interceptor createUserAgentInterceptor() {
        return (chain) -> {
            final Request request = chain.request()
                    .newBuilder()
                    .addHeader("User-Agent", System.getProperty("http.agent"))
                    .build();

            return chain.proceed(request);
        };
    }

    @Provides
    @Singleton
    Cache provideCache(Context context) {
        return new Cache(FileHelper.createIntCacheDir(context), Integer.MAX_VALUE);
    }

    @Provides
    @Singleton
    OkHttpClient okHttpClient(Cache cache, AccessTokenInterceptor accessTokenInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(createLoggingInterceptor())
                .addInterceptor(accessTokenInterceptor)
                .addInterceptor(createUserAgentInterceptor())
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
//        if (BuildConfig.DEBUG) {
//            builder.addInterceptor(createLoggingInterceptor());
//        }` `
        return builder
                .build();
    }
}

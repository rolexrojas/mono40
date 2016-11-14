package com.gbh.movil.data.api;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
interface ApiService {
  @GET("initial-load")
  Observable<Response<InitialDataHalResource>> initialLoad();
}

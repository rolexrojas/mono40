package com.mono40.movil.api;

import com.mono40.movil.ServiceInformation.Maintenance;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IPService {
    @GET("/?format=json")
    Call<ApiResponse> getIp();

    @POST("/post")
    Call<ResponsePostResponse> echoPostman(@Body PostRequestData content);

    @Headers({
            "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjIxMDE1MTMxfQ.hXctO0tMGwq1kxz61tCrXmQ45ztzuhLIyw_ruD0t8Uw"
    })
    @POST("serviceReport/maintenance/encrypt")
    Call<CodeForQRImage> getEncryptedMaintenance(@Header("insuranceNo") String insuranceNo, @Header("model") String model, @Header("make") String make, @Header("year") String year, @Header("miles") String miles, @Body Maintenance body);
}

package com.mono40.movil.dep.init;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;

import com.mono40.movil.api.ApiResponse;
import com.mono40.movil.api.IPService;
import com.mono40.movil.api.PostRequestData;
import com.mono40.movil.api.ResponsePostResponse;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.dep.ActivityBase;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.dep.App;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.RootUtil;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hecvasro
 */
@Deprecated
public final class InitActivityBase extends ActivityBase implements InitContainer {

  public static Intent getLaunchIntent(Context context) {
    return new Intent(context, InitActivityBase.class);
  }

  public static InitActivityBase get(Activity activity) {
    ObjectHelper.checkNotNull(activity, "activity");
    if (!(activity instanceof InitActivityBase)) {
      throw new ClassCastException("!(activity instanceof DepMainActivityBase)");
    }
    return (InitActivityBase) activity;
  }

  private InitComponent component;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @BindView(android.R.id.content) View rootView;
  @BindView(R.id.view_placeholder) View placeholderView;
  @BindView(R.id.view_container) View screenContainerView;
  @BindView(R.id.logo) Logo logo;

  @Override
  protected int layoutResourceIdentifier() {
    return R.layout.activity_init;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Initializes the dependency injector.
    this.component = App.get(this)
      .component()
      .plus(
        ActivityModule.create(this),
        new com.mono40.movil.dep.ActivityModule(this),
        new InitModule()
      );
    // Injects all the annotated dependencies.
    this.component.inject(this);
    // Initializes the application.
    this.fragmentReplacer.begin(InitFragment.create())
      .commit();

    //testApiIP();
  }

  private void testApiIP() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.ipify.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    IPService service = retrofit.create(IPService.class);

    Call<ApiResponse> call = service.getIp();
    call.enqueue(new Callback<ApiResponse>() {
      @Override
      public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
        Log.i("DEBUG IP=", " " + response.body().ranges);
      }

      @Override
      public void onFailure(Call<ApiResponse> call, Throwable t) {
        Log.i("DEBUG IP=", "FAIL REQUEST");
      }
    });

    Retrofit echoService = new Retrofit.Builder()
          .baseUrl("https://postman-echo.com")
          .addConverterFactory(GsonConverterFactory.create())
          .build();

    IPService serviceEcho = echoService.create(IPService.class);

    PostRequestData content = new PostRequestData();

    Call<ResponsePostResponse> callEcho = serviceEcho.echoPostman(content);
    callEcho.enqueue(new Callback<ResponsePostResponse>() {
      @Override
      public void onResponse(Call<ResponsePostResponse> call, Response<ResponsePostResponse> response) {
        Log.i("DEBUG ECHO=", response.body().data.address);
        Log.i("DEBUG ECHO=", response.body().data.name);
        Log.i("DEBUG ECHO=", response.body().data.lastName);
      }

      @Override
      public void onFailure(Call<ResponsePostResponse> call, Throwable t) {
        Log.i("DEBUG=", "FAIL REQUEST");
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();

    if(RootUtil.isDeviceRooted()) {
      RootUtil.showRootErrorDialog(this, this);
    }
  }

  @Override
  public InitComponent getInitComponent() {
    return component;
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
  }
}

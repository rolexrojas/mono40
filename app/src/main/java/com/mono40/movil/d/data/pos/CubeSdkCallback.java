package com.mono40.movil.d.data.pos;

import com.mono40.movil.d.domain.pos.CubeError;
import com.mono40.movil.d.domain.pos.PaymentInfo;

public interface CubeSdkCallback<T, T1> {
    void success(ListCards data);

    void success(PaymentInfo data);

    void success(String message);

    void failure(CubeError error);
}

package com.leisan.rangiffler.api.core;


import com.leisan.rangiffler.jupiter.extension.ApiLoginExtension;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CodeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.isRedirect()) {
            String location = response.header("Location");
            if (location != null && location.contains("code=")) {
                String code = StringUtils.substringAfter(location, "code=");
                ApiLoginExtension.setCode(code);
            }
        }
        return response;
    }
}
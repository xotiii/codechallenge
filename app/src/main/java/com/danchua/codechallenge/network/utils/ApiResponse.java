/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.danchua.codechallenge.network.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {
    public final int code;

    public final T body;

    public final String errorMessage;

    Gson g = new Gson();

    Error error;

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = ErrorUtils.getErrorMessage(error);
        error.printStackTrace();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    String er = response.errorBody().toString();
                    error = g.fromJson(response.errorBody().string(), Error.class);
                    message = error.getMessage();
                    Log.e("ApiError", response.errorBody().toString());
                } catch (IOException e) {
                    Log.e("ApiResult", "error parsing result", e);
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
}

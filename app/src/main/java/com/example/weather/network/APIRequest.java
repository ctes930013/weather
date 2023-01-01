package com.example.weather.network;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * API請求類別
 *
 */
public class APIRequest {

    private static APIRequest mApiManager;

    public static APIRequest getInstance() {
        if (mApiManager == null) {
            synchronized (APIRequest.class) {
                if (mApiManager == null) {
                    mApiManager = new APIRequest();
                }
            }
        }
        return mApiManager;
    }

    /**
     * 送出post請求
     *
     * @param url       目標網址
     * @param map       請求的資料
     * @param callback  api回傳的資料
     */
    public void sendPOST(String url, Map<String, Object> map, APICallback callback) {
        /**建立連線*/
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
        /**設置傳送所需夾帶的內容*/
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            formBody.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        /**設置傳送需求*/
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        /**設置回傳*/
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**如果傳送過程有發生錯誤*/
                callback.onFailure(-1, e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /**取得回傳*/
                String json = response.body().string();
                Log.e("onResponse", "json: " + json);
                callback.onSuccess(json);
            }
        });
    }

    /**
     * 送出get請求
     *
     * @param url       目標網址
     * @param map       請求的資料
     * @param callback  api回傳的資料
     */
    public void sendGET(String url, Map<String, Object> map, APICallback callback) {
        /**建立連線*/
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
        /**設置傳送需求*/
        if(map.size() > 0)
            url += "?";
        boolean s = false;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(s) url += "&";
            url += entry.getKey() + "=" + entry.getValue();
            s = true;
        }
        Request request = new Request.Builder()
                .url(url)
//                .header("Cookie","")//有Cookie需求的話則可用此發送
//                .addHeader("","")//如果API有需要header的則可使用此發送
                .build();
        /**設置回傳*/
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**如果傳送過程有發生錯誤*/
                callback.onFailure(-1, e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /**取得回傳*/
                String json = response.body().string();
                Log.e("onResponse", "json: " + json);
                callback.onSuccess(json);
            }
        });
    }
}

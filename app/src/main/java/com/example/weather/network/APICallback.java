package com.example.weather.network;

/**
 * API的回調資訊
 *
 */
public interface APICallback<T> {

    void onFailure(int errorCode, String errorMessage);

    void onSuccess(T data);
}

package com.tuononen.petteri.phuesensor.Interfaces;

public interface APIcallback {
    void ApiRequestResult(String result);

    void ApiRequestResultTest(String response);

    void ApiRequestResultToken(String token);

    void ApiRequestResultToDevice(String token);
}

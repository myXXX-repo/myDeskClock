package com.mySync;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestUtils {
    private String mySyncServerAddress;
    private String mySyncAccessTokenKey = "access_token";
    private String mySyncAccessTokenValue = "asdf";

    public void setMySyncServerAddress(String mySyncServerAddress) {
        this.mySyncServerAddress = mySyncServerAddress;
    }

    public void setMySyncAccessTokenKey(String mySyncAccessTokenKey) {
        this.mySyncAccessTokenKey = mySyncAccessTokenKey;
    }

    public void setMySyncAccessTokenValue(String mySyncAccessTokenValue) {
        this.mySyncAccessTokenValue = mySyncAccessTokenValue;
    }

    public Response sendPostRequest(String[] keys, String[] values) throws IOException {
        HttpRequests httpRequests = new HttpRequests(mySyncAccessTokenKey,mySyncAccessTokenValue);
        httpRequests.setServerAddress(mySyncServerAddress);
        return httpRequests.sendRequest(
                httpRequests.genPostRequest(
                        httpRequests.genFormBody(keys, values)));
    }

    public Response sendPostRequest(String JSONString) throws IOException {
        HttpRequests httpRequests = new HttpRequests(mySyncAccessTokenKey,mySyncAccessTokenValue);
        httpRequests.setServerAddress(mySyncServerAddress);
        return httpRequests.sendRequest(
                httpRequests.genPostRequest(
                        httpRequests.genPostJson(JSONString)));
    }

    public Response sendGetRequest() throws IOException {
        HttpRequests httpRequests = new HttpRequests(mySyncAccessTokenKey,mySyncAccessTokenValue);
        httpRequests.setServerAddress(mySyncServerAddress);
        return httpRequests.sendRequest(
                httpRequests.genGetRequest());
    }

    public Response sendDeleteRequest() throws IOException {
        HttpRequests httpRequests = new HttpRequests(mySyncAccessTokenKey,mySyncAccessTokenValue);
        httpRequests.setServerAddress(mySyncServerAddress);
        return httpRequests.sendRequest(
                httpRequests.genDeleteRequest());
    }

    static class HttpRequests {
        private OkHttpClient client;
        private String serverAddress;
        private String access_token_key;
        private String access_token_value;

        HttpRequests(String access_token_key, String access_token_value) {
            this.client = new OkHttpClient();
            this.access_token_key = access_token_key;
            this.access_token_value = access_token_value;
        }

        void setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
        }

        RequestBody genPostJson(String JSONString) {
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            return RequestBody.create(JSONString, JSON);
        }

        FormBody genFormBody(String[] keys, String[] values) {
            FormBody.Builder formBody = new FormBody.Builder();
            for (int i = 0; i < keys.length; i++) {
                formBody.add(keys[i], values[i]);
            }
            return formBody.build();
        }

        Request genPostRequest(RequestBody requestBody) {
            return new Request.Builder()
                    .header(access_token_key, access_token_value)
                    .url(serverAddress)
                    .post(requestBody)
                    .build();
        }

        Request genPostRequest(FormBody formBody) {
            return new Request.Builder()
                    .header(access_token_key, access_token_value)
                    .url(serverAddress)
                    .post(formBody)
                    .build();
        }

        Request genGetRequest() {
            return new Request.Builder()
                    .header(access_token_key, access_token_value)
                    .url(serverAddress)
                    .build();
        }

        Request genDeleteRequest() {
            return new Request.Builder()
                    .header(access_token_key, access_token_value)
                    .url(serverAddress)
                    .delete()
                    .build();
        }

        Response sendRequest(Request request) throws IOException {
            return client.newCall(request).execute();
        }
    }
}


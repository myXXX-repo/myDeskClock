package com.mySync.values;

import androidx.collection.SimpleArrayMap;

public class HttpErrorCode {

    public static SimpleArrayMap<Integer, String> getHttpRequestErrorSet() {
        SimpleArrayMap<Integer,String> HTTP_REQUEST_ERROR_SET = new SimpleArrayMap<>();

        HTTP_REQUEST_ERROR_SET.put(400,"400 BadRequest");
        HTTP_REQUEST_ERROR_SET.put(401,"401 Unauthorized");
        HTTP_REQUEST_ERROR_SET.put(403,"403 Forbidden");
        HTTP_REQUEST_ERROR_SET.put(404,"404 NotFound");
        HTTP_REQUEST_ERROR_SET.put(405,"405 MethodNotAllowed");
        HTTP_REQUEST_ERROR_SET.put(406,"406 NotAcceptable");
        HTTP_REQUEST_ERROR_SET.put(408,"408 RequestTimeout");
        HTTP_REQUEST_ERROR_SET.put(409,"409 Conflict");
        HTTP_REQUEST_ERROR_SET.put(410,"410 Gone");
        HTTP_REQUEST_ERROR_SET.put(411,"411 LengthRequired");
        HTTP_REQUEST_ERROR_SET.put(412,"412 InternalServerError");
        HTTP_REQUEST_ERROR_SET.put(413,"413 RequestEntityTooLarge");
        HTTP_REQUEST_ERROR_SET.put(414,"414 RequestURITooLarge");
        HTTP_REQUEST_ERROR_SET.put(416,"416 RequestedRangeNotSatisfiable");
        HTTP_REQUEST_ERROR_SET.put(417,"417 ExpectationFailed");
        HTTP_REQUEST_ERROR_SET.put(500,"500 InternalServerError");
        HTTP_REQUEST_ERROR_SET.put(501,"501 NotImplemented");
        HTTP_REQUEST_ERROR_SET.put(502,"502 BadGateway");
        HTTP_REQUEST_ERROR_SET.put(503,"503 ServiceUnavailable");
        HTTP_REQUEST_ERROR_SET.put(504,"504 GatewayTimeout");
        HTTP_REQUEST_ERROR_SET.put(505,"505 HTTPVersionNotSupported");
        return HTTP_REQUEST_ERROR_SET;
    }
}

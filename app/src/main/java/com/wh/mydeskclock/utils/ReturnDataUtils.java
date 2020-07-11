package com.wh.mydeskclock.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.lang.reflect.Type;

/**
 * Created by Zhenjie Yan on 2018/6/9.
 */
public class ReturnDataUtils {

    /**
     * Business is successful.
     *
     * @param data return data.
     *
     * @return json.
     */
    public static String successfulJson(Object data) {
        ReturnData returnData = new ReturnData();
        returnData.setSuccess(true);
        returnData.setErrorCode(200);
        returnData.setData(data);
        return JSON.toJSONString(returnData);
    }

    /**
     * Business is failed.
     *
     * @param code error code.
     * @param message message.
     *
     * @return json.
     */
    public static String failedJson(int code, String message) {
        ReturnData returnData = new ReturnData();
        returnData.setSuccess(false);
        returnData.setErrorCode(code);
        returnData.setErrorMsg(message);
        return JSON.toJSONString(returnData);
    }

    /**
     * Converter object to json string.
     *
     * @param data the object.
     *
     * @return json string.
     */
    public static String toJsonString(Object data) {
        return JSON.toJSONString(data);
    }

    /**
     * Parse json to object.
     *
     * @param json json string.
     * @param type the type of object.
     * @param <T> type.
     *
     * @return object.
     */
    public static <T> T parseJson(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    public static class ReturnData implements Parcelable {

        @JSONField(name = "isSuccess")
        private boolean isSuccess;

        @JSONField(name = "errorCode")
        private int errorCode;

        @JSONField(name = "errorMsg")
        private String errorMsg;

        @JSONField(name = "data")
        private Object data;

        public ReturnData() {
        }

        protected ReturnData(Parcel in) {
            isSuccess = in.readByte() != 0;
            errorCode = in.readInt();
            errorMsg = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (isSuccess ? 1 : 0));
            dest.writeInt(errorCode);
            dest.writeString(errorMsg);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ReturnData> CREATOR = new Creator<ReturnData>() {
            @Override
            public ReturnData createFromParcel(Parcel in) {
                return new ReturnData(in);
            }

            @Override
            public ReturnData[] newArray(int size) {
                return new ReturnData[size];
            }
        };

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}

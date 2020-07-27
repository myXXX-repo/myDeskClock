package com.wh.mydeskclock.utils;

public class ApiNode {
    String app;
    String path;
    String usage;
    String describe;
    String method;
    String path_variable;
    String param;

    public ApiNode(String app, String path, String usage, String describe, String method, String path_variable, String param) {
        this.app = app;
        this.path = path;
        this.usage = usage;
        this.describe = describe;
        this.method = method;
        this.path_variable = path_variable;
        this.param = param;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath_variable() {
        return path_variable;
    }

    public void setPath_variable(String path_variable) {
        this.path_variable = path_variable;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}

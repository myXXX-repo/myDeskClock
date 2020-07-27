package com.wh.mydeskclock.utils;

public class ApiNode {
    String APP;
    String PATH;
    String USAGE;
    String DESCRIBE;
    String METHOD;
    String PATH_VARIABLE;
    String PARAM;

    public ApiNode(String APP, String PATH, String USAGE, String DESCRIBE, String METHOD, String PATH_VARIABLE, String PARAM) {
        this.APP = APP;
        this.PATH = PATH;
        this.USAGE = USAGE;
        this.DESCRIBE = DESCRIBE;
        this.METHOD = METHOD;
        this.PATH_VARIABLE = PATH_VARIABLE;
        this.PARAM = PARAM;
    }

    public String getAPP() {
        return APP;
    }

    public void setAPP(String APP) {
        this.APP = APP;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getUSAGE() {
        return USAGE;
    }

    public void setUSAGE(String USAGE) {
        this.USAGE = USAGE;
    }

    public String getDESCRIBE() {
        return DESCRIBE;
    }

    public void setDESCRIBE(String DESCRIBE) {
        this.DESCRIBE = DESCRIBE;
    }

    public String getMETHOD() {
        return METHOD;
    }

    public void setMETHOD(String METHOD) {
        this.METHOD = METHOD;
    }

    public String getPATH_VARIABLE() {
        return PATH_VARIABLE;
    }

    public void setPATH_VARIABLE(String PATH_VARIABLE) {
        this.PATH_VARIABLE = PATH_VARIABLE;
    }

    public String getPARAM() {
        return PARAM;
    }

    public void setPARAM(String PARAM) {
        this.PARAM = PARAM;
    }
}

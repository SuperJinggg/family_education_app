package com.example.ateachingapplication.data;




public class Result {
    private int code;
    private String msg;
    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    public void setSuccess(String msg, String result){
        this.code=200;
        this.msg="success-"+msg;
        this.result=result;
    }
    public void setInfo(String msg, String result){
        this.code=400;
        this.msg="warning-"+msg;
        this.result=result;
    }
}
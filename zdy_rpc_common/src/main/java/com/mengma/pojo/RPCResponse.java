package com.mengma.pojo;

import java.io.Serializable;

/**
 * @author fgm
 * @description  返回对象
 * @date 2020-04-12
 ***/
public class RPCResponse<T> implements Serializable {

    public static final String SUCCESS="1";
    public static final String FAILED="0";


    private static final long serialVersionUID = 7012199426774138911L;

    private String responseId;

    private T object;

    private String code;

    private String msg;

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

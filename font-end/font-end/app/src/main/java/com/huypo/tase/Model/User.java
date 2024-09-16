package com.huypo.tase.Model;

import java.io.Serializable;

public class User implements Serializable {
    private Boolean result;
    private String token="";
    private String _id="";
    private String email="";
    private String name="";
    private Boolean delete;

    public  User (Boolean result, String token, String _id, String email, String name, Boolean delete)
    {
        this.result=result;
        this.token=token;
        this._id=_id;
        this.email=email;
        this.name=name;
        this.delete=delete;
    }

    public  User(){};
    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
}

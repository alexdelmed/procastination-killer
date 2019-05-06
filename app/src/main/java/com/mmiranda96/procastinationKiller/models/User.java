package com.mmiranda96.procastinationKiller.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Base64;

public class User implements Serializable {

    private String username;
    private String password;


    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String buildAuthentication() {
        String authentication = this.username + ":" + this.password;
        return Base64.getEncoder().encodeToString(authentication.getBytes());
    }

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            result.put("email", this.username);
            result.put("password", this.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}

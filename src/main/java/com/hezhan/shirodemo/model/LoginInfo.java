package com.hezhan.shirodemo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginInfo implements Serializable {

    private String userName;
    private String password;
}

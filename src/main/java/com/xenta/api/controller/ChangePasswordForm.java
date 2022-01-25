package com.xenta.api.controller;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChangePasswordForm implements Serializable {
    private String username;
    private String oldPassword;
    private String newPassword;
}

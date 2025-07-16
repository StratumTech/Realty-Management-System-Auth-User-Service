package com.stratumtech.realtyauthuser.dto;

import lombok.Data;

@Data
public class AgentUpdateDTO {
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;
    private String imageUrl;
    private String password;
    private String role;
}
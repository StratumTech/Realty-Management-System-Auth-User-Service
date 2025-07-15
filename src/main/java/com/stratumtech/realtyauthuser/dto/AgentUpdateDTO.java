package com.stratumtech.realtyauthuser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentUpdateDTO {
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private String telegramTag;
    private String preferChannel;
    private String imageUrl;
}
package com.example.cloudBalance_server.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {

    private Integer accountId;
    private String accountName;
    private String accountArn;
    private String accountStatus;

}

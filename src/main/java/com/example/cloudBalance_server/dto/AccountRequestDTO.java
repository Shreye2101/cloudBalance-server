package com.example.cloudBalance_server.dto;

import lombok.Data;

@Data
public class AccountRequestDTO {
    private Integer accountId;
    private String accountName;
    private String accountArn;
}

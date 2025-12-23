package com.example.cloudBalance_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false, unique = true)
    private String accountArn;

    @Builder.Default
    @Column(nullable = false)
    private String accountStatus = "ORPHAN";
}
package com.example.app.dto;

import lombok.Data;


@Data
public class TransferDto {
    private String targetEmail;
    private Double amount;
}


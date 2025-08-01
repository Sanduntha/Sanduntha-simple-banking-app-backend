package com.example.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillPaymentDto {
    private String biller;
    private Double amount;
}


package com.example.app.service;

import com.example.app.dto.TopUpDto;
import com.example.app.dto.BillPaymentDto;
import com.example.app.dto.TransferDto;
import com.example.app.entity.Transaction;
import com.example.app.entity.User;

import java.util.List;

public interface TransactionService {
    Transaction topUp(User user, TopUpDto dto);
    Transaction payBill(User user, BillPaymentDto dto);
    Transaction transferFunds(User user, TransferDto dto);
    List<Transaction> getTransactions(User user);
}

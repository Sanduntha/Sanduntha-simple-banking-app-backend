package com.example.app.service.impl;

import com.example.app.dto.TopUpDto;
import com.example.app.dto.BillPaymentDto;
import com.example.app.dto.TransferDto;
import com.example.app.entity.Transaction;
import com.example.app.entity.TransactionType;
import com.example.app.entity.User;
import com.example.app.repo.TransactionRepo;
import com.example.app.repo.UserRepo;
import com.example.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;

    @Override
    public Transaction topUp(User user, TopUpDto dto) {
        user.setBalance(user.getBalance() + dto.getAmount());
        userRepo.save(user);

        Transaction tx = new Transaction(null, user, TransactionType.TOP_UP, dto.getAmount(), null, null, new Date());
        return transactionRepo.save(tx);
    }

    @Override
    public Transaction payBill(User user, BillPaymentDto dto) {
        double charge = 0.0;

        if ("Electricity".equalsIgnoreCase(dto.getBiller())) {
            charge = dto.getAmount() * 0.10; // 10% charge
        } else if ("Water".equalsIgnoreCase(dto.getBiller())) {
            charge = dto.getAmount() * 0.05; // 5% charge
        }

        double total = dto.getAmount() + charge;

        if (user.getBalance() < total) {
            throw new RuntimeException("Insufficient balance.");
        }

        // Deduct balance
        user.setBalance(user.getBalance() - total);
        userRepo.save(user);

        // Create and save transaction with current timestamp
        Transaction tx = new Transaction(null, user, TransactionType.BILL_PAYMENT, total, dto.getBiller(), null, new Date());

        return transactionRepo.save(tx);
    }


    @Override
    public Transaction transferFunds(User user, TransferDto dto) {
        Optional<User> targetUserOpt = userRepo.findByEmail(dto.getTargetEmail());
        if (targetUserOpt.isEmpty()) {
            throw new RuntimeException("Target user not found.");
        }

        if (user.getBalance() < dto.getAmount()) {
            throw new RuntimeException("Insufficient balance.");
        }

        User targetUser = targetUserOpt.get();

        user.setBalance(user.getBalance() - dto.getAmount());
        targetUser.setBalance(targetUser.getBalance() + dto.getAmount());

        userRepo.save(user);
        userRepo.save(targetUser);

        Transaction tx = new Transaction(null, user, TransactionType.TRANSFER, dto.getAmount(), null, dto.getTargetEmail(), new Date());
        return transactionRepo.save(tx);
    }

    @Override
    public List<Transaction> getTransactions(User user) {
        return transactionRepo.findByUser(user);
    }
}

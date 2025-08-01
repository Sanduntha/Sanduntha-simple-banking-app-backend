package com.example.app.controller;

import com.example.app.dto.BillPaymentDto;
import com.example.app.dto.TopUpDto;
import com.example.app.dto.TransferDto;
import com.example.app.entity.Transaction;
import com.example.app.entity.User;
import com.example.app.repo.UserRepo;
import com.example.app.service.TransactionService;
import com.example.app.util.JWTTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final UserRepo userRepo;

    private String extractToken(String header) {
        return header.startsWith("Bearer ") ? header.substring(7) : header;
    }

    private User getUserFromToken(String authHeader) {
        String token = extractToken(authHeader);
        if (!jwtTokenGenerator.verifyToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        String email = jwtTokenGenerator.getClaims(token).getPayload().getSubject();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/topup")
    public Map<String, Object> topUp(@RequestHeader("Authorization") String authHeader,
                                     @RequestBody TopUpDto dto) {
        User user = getUserFromToken(authHeader);
        Transaction tx = transactionService.topUp(user, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Top-up successful");
        response.put("amount", tx.getAmount());
        response.put("newBalance", user.getBalance());

        return response;
    }

    @PostMapping("/paybill")
    public Map<String, Object> payBill(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody BillPaymentDto dto) {
        User user = getUserFromToken(authHeader);
        Transaction tx = transactionService.payBill(user, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bill payment successful");
        response.put("biller", tx.getBiller());
        response.put("amountCharged", tx.getAmount());
        response.put("newBalance", user.getBalance());

        return response;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody TransferDto dto) {
        try {
            User user = getUserFromToken(authHeader);
            Transaction tx = transactionService.transferFunds(user, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Transfer successful");
            response.put("to", tx.getTargetUserEmail());
            response.put("amount", tx.getAmount());
            response.put("newBalance", user.getBalance());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error: " + e.getMessage()));
        }
    }


    @GetMapping("/history")
    public List<Transaction> getTransactionHistory(@RequestHeader("Authorization") String authHeader) {
        User user = getUserFromToken(authHeader);
        return transactionService.getTransactions(user);
    }
}

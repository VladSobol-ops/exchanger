package com.example.exchanger2.controller;

import com.example.exchanger2.CurrencyConverter;
import com.example.exchanger2.domain.PutRequest;
import com.example.exchanger2.domain.User;
import com.example.exchanger2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor//добавляет конструктор по полям
public class UserBalanceController {
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PostMapping("{username}/convert")
    public ResponseEntity<?> moneyTransfer(@PathVariable String username, @RequestBody PutRequest request) {
        return userService.moneyTransfer(username, request);
    }


    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUSerByUsername(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUserBalance(@PathVariable String username, @RequestBody PutRequest newBalance) {
        return ResponseEntity.ok(userService.updateUserBalance(username, newBalance));

    }

    @PutMapping("/{username}/{currency}")
    public ResponseEntity<?> updateUserCurrency(@PathVariable String username, @RequestBody PutRequest newCurrency, @PathVariable String currency) {
        return ResponseEntity.ok(userService.updateUserCurrency(username, newCurrency, currency));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.deleteUSer(username));
    }
}




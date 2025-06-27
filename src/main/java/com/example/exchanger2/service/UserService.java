package com.example.exchanger2.service;

import com.example.exchanger2.CurrencyConverter;
import com.example.exchanger2.domain.PutRequest;
import com.example.exchanger2.domain.User;
import com.example.exchanger2.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CurrencyConverter currencyConverter;

    public ResponseEntity<?> addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<?> moneyTransfer(String username, PutRequest request) {
        try {
            User user1 = findByUsername(username);
            User user2 = findByUsername(request.getToUsername());
            double convertedBalance = currencyConverter.convert(request.getBalance(), user1.getCurrency(), request.getCurrency());
            if (user1.getBalance() < request.getBalance()) {
                return ResponseEntity.badRequest().body("Недостаточно средств для перевода");
            }
            user1.setBalance(user1.getBalance() - request.getBalance());
            user2.setBalance(user2.getBalance() + convertedBalance);
            userRepository.saveAll(List.of(user1, user2));
            return ResponseEntity.ok("Пользователь: " + username + " конвертировал " + request.getBalance() + " " + user1.getCurrency() + " в " + user2.getCurrency() + ". Обновленный баланс пользователя " + username + ": " + user1.getBalance() + ", Обновленный баланс пользователя " + request.getToUsername() + ": " + user2.getBalance());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {
        return findByUsername(username);
    }

    @CacheEvict(value = "users", key = "#username")
    public ResponseEntity<?> updateUserBalance(String username, PutRequest newBalance) {
        try {
            User user = findByUsername(username);
            double oldBalance = user.getBalance();
            user.setBalance(newBalance.getBalance());
            userRepository.save(user);
            return ResponseEntity.ok("Баланс пользователя: " + username + " обновился с " + oldBalance + " на " + newBalance.getBalance());
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @CacheEvict(value = "users", key = "#username")
    public ResponseEntity<?> updateUserCurrency(String username, PutRequest newCurrency, String currency) {
        try {
            User user = findByUsername(username);
            if (!user.getCurrency().equalsIgnoreCase(currency)) {
                return ResponseEntity.badRequest().body("Указана некорректная валюта пользователя: " + username);
            }
            String oldCurrency = user.getCurrency();
            user.setCurrency(newCurrency.getCurrency());
            userRepository.save(user);
            return ResponseEntity.ok("Валюта пользователя: " + username + " изменилась с " + oldCurrency + " на " + newCurrency.getCurrency());
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @CacheEvict(value = "users", key = "#username")
    public ResponseEntity<?> deleteUser(String username) {
        try {
            User user = findByUsername(username);
            userRepository.delete(user);
            return ResponseEntity.ok("Пользователь: " + username + " удален из списка");
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }
}

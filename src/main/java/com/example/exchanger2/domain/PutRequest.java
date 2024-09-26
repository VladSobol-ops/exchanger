package com.example.exchanger2.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data// добавляет геттеры и сеттеры и конструктор
@NoArgsConstructor// автоматически добавляет пустой конструктор
public class PutRequest {
    private Double balance;
    private String currency;
    private double transaction;
    private String toUsername;
}

package com.example.exchanger2.domain;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity//Указывает, что этот класс является сущностью JPA
@Table(name = "users")//Указывает имя таблицы в базе данных.
public class User {
    @Id//Указывает, что поле id является уникальным идентификатором для каждой записи.
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id будет автоматически генерироваться базой данных
    private Long id;

    @Column(name = "username")
    private String username;
    private double balance;
    private String currency;
}

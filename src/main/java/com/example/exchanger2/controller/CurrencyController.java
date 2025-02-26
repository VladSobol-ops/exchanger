package com.example.exchanger2.controller;

import com.example.exchanger2.CurrencyConverter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
@AllArgsConstructor
public class CurrencyController {
    private final CurrencyConverter currencyConverter;

    @GetMapping("/rates")
    public String getExchangeRates(){
        return currencyConverter.getExchangeRateToRUB();
    }
}

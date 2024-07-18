package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class Rates {
    private Map<String, BigDecimal> rates;
}

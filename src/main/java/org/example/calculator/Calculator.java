package org.example.calculator;

import org.example.cacheProxy.Cachable;

import java.math.BigInteger;

public interface Calculator {

    @Cachable
    BigInteger factorial(int number);
}

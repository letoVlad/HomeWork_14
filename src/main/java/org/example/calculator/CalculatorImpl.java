package org.example.calculator;

import java.math.BigInteger;

public class CalculatorImpl implements Calculator {

    @Override
    public BigInteger factorial(int number) {
        BigInteger factorial = BigInteger.ONE;

        for (int i = 2; i <= number; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }

        return factorial;
    }
}

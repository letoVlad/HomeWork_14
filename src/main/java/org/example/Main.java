package org.example;

import org.example.cacheProxy.CacheProxy;
import org.example.calculator.CalculatorImpl;
import org.example.calculator.Calculator;

public class Main {
    public static void main(String[] args) {

        Calculator taskService = CacheProxy.createProxy(new CalculatorImpl());
        taskService.factorial(42);



    }
}

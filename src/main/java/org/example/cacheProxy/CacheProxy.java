package org.example.cacheProxy;

import org.example.database.DatabaseConnection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;

public class CacheProxy implements InvocationHandler {
    private final Object target;
    private final DatabaseConnection databaseConnection;

    public CacheProxy(Object target) {
        this.target = target;
        this.databaseConnection = new DatabaseConnection();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cachable.class)) {
            BigInteger cachedData = findDataByMethodName(method, args);
            if (cachedData != null) {
                return cachedData;
            }
        }

        Object result = method.invoke(target, args);
        saveToDatabase(method, args, result);
        return result;
    }

    private void saveToDatabase(Method method, Object[] args, Object result) {
        String methodName = generateCacheKey(method, args);
        databaseConnection.saveData(methodName, result.toString());
    }

    private String generateCacheKey(Method method, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder(method.getName());
        for (Object arg : args) {
            keyBuilder.append("_").append(arg);
        }
        return keyBuilder.toString();
    }

    private BigInteger findDataByMethodName(Method method, Object[] args) {
        var result = generateCacheKey(method, args);
        return databaseConnection.findDataByMethodName(result);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new CacheProxy(target)
        );
    }
}

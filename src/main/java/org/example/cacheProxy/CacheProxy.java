package org.example.cacheProxy;

import org.example.database.DatabaseConnection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.math.BigInteger;

public class CacheProxy implements InvocationHandler {
    private final Object target;
    DatabaseConnection databaseConnection = new DatabaseConnection();

    public CacheProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cachable.class)) {
            var dataByMethodName = findDataByMethodName(method, args);
            if (dataByMethodName != null) {
                return dataByMethodName;
            }
        }

        Object result = method.invoke(target, args);
        saveToDataBase(method, args, result);

        return result;
    }

    private void saveToDataBase(Method method, Object[] args, Object result) {
        var methodName = generateCacheKey(method, args);
        databaseConnection.saveData(methodName, result.toString());
    }

    private String generateCacheKey(Method method, Object[] args) {
        StringBuilder key = new StringBuilder(method.getName());
        for (int i = 0; i < args.length; i++) {
            key.append("_").append(args[i]);
        }
        return key.toString();
    }

    private BigInteger findDataByMethodName(Method method, Object[] args) {
        var result = generateCacheKey(method, args);
        return databaseConnection.findDataByMethodName(result);
    }

    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new CacheProxy(target)
        );
    }
}

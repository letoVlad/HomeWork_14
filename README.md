
## Описание CacheProxy Project

Этот проект реализует механизм кэширования для методов с использованием прокси и базы данных. Основной задачей является кэширование результатов методов, помеченных аннотацией `@Cachable`, с сохранением данных в базе данных для последующего извлечения.

## Описание

Этот проект реализует механизм кэширования с использованием прокси и базы данных для сохранения результатов выполнения методов. Основная цель - улучшить производительность за счет повторного использования ранее вычисленных результатов.

### Как это работает:

1. **Прокси для методов**: 
   В проекте используется паттерн Proxy для перехвата вызовов методов. Когда вызывается метод, который помечен аннотацией `@Cachable`, прокси проверяет, есть ли уже кэшированный результат для данного метода и его параметров. Если результат найден в базе данных, он возвращается немедленно, минуя выполнение метода.

2. **Кэширование**:
   Если результат метода не найден в кэше (в базе данных), метод выполняется, и результат сохраняется в таблице базы данных для последующего использования.

3. **База данных**:
   Результаты кэшируются в базе данных. Таблица `cache` хранит два поля: `method_name` (имя метода) и `result_method` (результат выполнения метода). Для каждого вызова сохраняется уникальная запись, которая включает имя метода и результат его выполнения.

4. **Динамическое создание прокси**:
   Используется механизм Java Reflection для создания прокси-объекта. Это позволяет обрабатывать методы без явного их вызова, а все вызовы методов проходят через прокси, который обрабатывает логику кэширования.


## Структура базы данных

Таблица `cache` в базе данных имеет два столбца:
- **method_name**: строка, имя метода.
- **result_method**: строка, результат выполнения метода.

Пример таблицы:
```sql
CREATE TABLE cache (
    method_name VARCHAR(255),
    result_method VARCHAR(255)
);
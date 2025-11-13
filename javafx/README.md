# Java Calculator - Desktop приложение (JavaFX)

Современный и функциональный калькулятор на Java с использованием JavaFX.

## Функции

- Базовые арифметические операции (+, -, ×, ÷)
- Очистка всего (C)
- Очистка текущего ввода (CE)
- Удаление последней цифры (⌫)
- Изменение знака (±)
- Работа с десятичными числами
- Конвертация систем счисления (BIN, OCT, DEC, HEX)

## Компиляция и запуск

### Требования

- Java JDK 11 или выше
- JavaFX SDK (скачать с [openjfx.io](https://openjfx.io/))

### Настройка переменной окружения

Установите переменную окружения `JAVAFX_HOME`, указывающую на директорию с JavaFX SDK:

```batch
set JAVAFX_HOME=C:\path\to\javafx-sdk
```

### Компиляция:

```batch
compile.bat
```

Или вручную:

```batch
javac --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls Calculator.java
```

### Запуск:

```batch
run.bat
```

Или вручную:

```batch
java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls Calculator
```

## Особенности

- Современный и интуитивный интерфейс на JavaFX
- Цветовая кодировка кнопок для лучшей читаемости
- Обработка ошибок (деление на ноль)
- Поддержка различных систем счисления
- Адаптивный дизайн

## Альтернативный способ запуска (без модулей)

Если у вас JavaFX в classpath, можно использовать упрощенные команды:

```batch
javac Calculator.java
java Calculator
```


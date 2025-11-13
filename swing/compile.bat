@echo off
cd /d %~dp0
echo Компиляция калькулятора...
javac Calculator.java
if %errorlevel% == 0 (
    echo Компиляция успешна!
    echo Для запуска используйте: java Calculator
) else (
    echo Ошибка компиляции!
    pause
)


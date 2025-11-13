@echo off
cd /d %~dp0
echo Компиляция калькулятора JavaFX...
javac --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls Calculator.java
if %errorlevel% == 0 (
    echo Компиляция успешна!
    echo Для запуска используйте: run.bat
) else (
    echo Ошибка компиляции!
    echo Убедитесь, что установлен JavaFX и переменная JAVAFX_HOME настроена правильно
    pause
)


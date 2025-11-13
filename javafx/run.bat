@echo off
cd /d %~dp0
echo Запуск калькулятора JavaFX...
java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls Calculator
pause


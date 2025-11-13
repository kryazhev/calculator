import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Calculator extends Application {
    private TextField display;
    private double firstNumber;
    private String operation;
    private boolean isOperationPressed;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Калькулятор");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED); // Скрываем заголовок и кнопку закрытия

        // Инициализация
        firstNumber = 0;
        operation = "";
        isOperationPressed = false;

        // Создание интерфейса с градиентным фоном
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        // Создание заголовка в стиле калькулятора с градиентом
        Text titleText = new Text("Калькулятор");
        titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        
        // Создаем градиент для текста (от голубого к светлому синему)
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0, true, null,
            new Stop(0, Color.web("#0ef")),
            new Stop(1, Color.web("#00f2fe"))
        );
        titleText.setFill(gradient);
        
        // Добавляем эффект свечения
        Glow glow = new Glow(0.8);
        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.rgb(14, 239, 255, 0.8));
        titleShadow.setRadius(15);
        titleShadow.setOffsetX(0);
        titleShadow.setOffsetY(0);
        glow.setInput(titleShadow);
        titleText.setEffect(glow);
        
        // Обертка для заголовка с отступами
        VBox titleBox = new VBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 10, 15, 10));
        titleBox.setStyle("-fx-background-color: transparent;");
        titleBox.getChildren().add(titleText);

        // Панель отображения с улучшенным дизайном
        display = new TextField("0");
        display.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setEditable(false);
        display.setStyle(
            "-fx-background-color: #1a1a2e; " +
            "-fx-text-fill: #0ef; " +
            "-fx-border-color: #0ef; " +
            "-fx-border-width: 3; " +
            "-fx-border-radius: 20; " +
            "-fx-background-radius: 20; " +
            "-fx-padding: 15 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(14, 239, 255, 0.5), 10, 0, 0, 0);"
        );
        display.setPrefHeight(90);

        // Панель кнопок с прозрачным фоном
        GridPane buttonPanel = new GridPane();
        buttonPanel.setHgap(10);
        buttonPanel.setVgap(10);
        buttonPanel.setPadding(new Insets(15));
        buttonPanel.setStyle("-fx-background-color: transparent;");

        // Кнопки
        String[] buttonLabels = {
            "C", "CE", "⌫", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "=",
            "BIN", "OCT", "DEC", "HEX"
        };

        int col = 0;
        int row = 0;
        for (String label : buttonLabels) {
            Button button = createButton(label);
            buttonPanel.add(button, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        // Создаем контейнер для верхней части (заголовок + дисплей)
        VBox topContainer = new VBox(10);
        topContainer.setStyle("-fx-background-color: transparent;");
        topContainer.setPadding(new Insets(0, 15, 0, 15));
        topContainer.getChildren().addAll(titleBox, display);

        // Добавляем компоненты
        root.setTop(topContainer);
        root.setCenter(buttonPanel);

        // Создание сцены с увеличенным размером (добавили место для заголовка)
        Scene scene = new Scene(root, 380, 640);
        primaryStage.setScene(scene);
        
        // Добавляем возможность перетаскивания окна (так как заголовок скрыт)
        final double[] xOffset = new double[1];
        final double[] yOffset = new double[1];
        
        root.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });
        
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset[0]);
            primaryStage.setY(event.getScreenY() - yOffset[0]);
        });
        
        // Закрытие приложения по Escape или Alt+F4
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });
        
        primaryStage.show();
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        button.setPrefSize(80, 70);
        
        // Добавляем тень для всех кнопок
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(8);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        button.setEffect(shadow);
        
        button.setStyle(getButtonStyle(label));

        // Эффект при наведении
        button.setOnMouseEntered(e -> {
            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.rgb(0, 0, 0, 0.5));
            hoverShadow.setRadius(12);
            hoverShadow.setOffsetX(3);
            hoverShadow.setOffsetY(3);
            button.setEffect(hoverShadow);
        });
        
        button.setOnMouseExited(e -> {
            button.setEffect(shadow);
        });

        button.setOnAction(e -> handleButtonClick(label));

        return button;
    }

    private String getButtonStyle(String label) {
        // Базовый стиль с очень круглыми углами
        String baseStyle = 
            "-fx-border-radius: 35; " +
            "-fx-background-radius: 35; " +
            "-fx-cursor: hand; " +
            "-fx-border-width: 0; " +
            "-fx-transition: all 0.2s;";
        
        if (label.matches("[0-9]")) {
            // Цифры - светлый фон с градиентом
            return baseStyle + 
                " -fx-background-color: linear-gradient(to bottom, #ffffff 0%, #f0f0f0 100%); " +
                " -fx-text-fill: #2c3e50;";
        } else if (label.equals("=")) {
            // Кнопка равно - яркий синий градиент
            return baseStyle + 
                " -fx-background-color: linear-gradient(to bottom, #4facfe 0%, #00f2fe 100%); " +
                " -fx-text-fill: white; " +
                " -fx-font-size: 24;";
        } else if (label.matches("[+\\-×÷]")) {
            // Операции - оранжевый градиент
            return baseStyle + 
                " -fx-background-color: linear-gradient(to bottom, #fa709a 0%, #fee140 100%); " +
                " -fx-text-fill: white;";
        } else if (label.matches("BIN|OCT|DEC|HEX")) {
            // Системы счисления - зеленый градиент
            return baseStyle + 
                " -fx-background-color: linear-gradient(to bottom, #30cfd0 0%, #330867 100%); " +
                " -fx-text-fill: white; " +
                " -fx-font-size: 16;";
        } else {
            // Остальные кнопки - серый градиент
            return baseStyle + 
                " -fx-background-color: linear-gradient(to bottom, #434343 0%, #000000 100%); " +
                " -fx-text-fill: white;";
        }
    }

    private void handleButtonClick(String buttonText) {
        if (buttonText.matches("[0-9]")) {
            handleNumber(buttonText);
        } else if (buttonText.equals(".")) {
            handleDecimalPoint();
        } else if (buttonText.matches("[+\\-×÷]")) {
            handleOperation(buttonText);
        } else if (buttonText.equals("=")) {
            handleEquals();
        } else if (buttonText.equals("C")) {
            handleClear();
        } else if (buttonText.equals("CE")) {
            handleClearEntry();
        } else if (buttonText.equals("⌫")) {
            handleBackspace();
        } else if (buttonText.equals("±")) {
            handleSignChange();
        } else if (buttonText.equals("BIN")) {
            handleBaseConversion(2);
        } else if (buttonText.equals("OCT")) {
            handleBaseConversion(8);
        } else if (buttonText.equals("DEC")) {
            handleBaseConversion(10);
        } else if (buttonText.equals("HEX")) {
            handleBaseConversion(16);
        }
    }

    private void handleNumber(String number) {
        if (isOperationPressed) {
            display.setText(number);
            isOperationPressed = false;
        } else {
            String currentText = display.getText();
            if (currentText.equals("0")) {
                display.setText(number);
            } else {
                display.setText(currentText + number);
            }
        }
    }

    private void handleDecimalPoint() {
        String currentText = display.getText();
        if (isOperationPressed) {
            display.setText("0.");
            isOperationPressed = false;
        } else if (!currentText.contains(".")) {
            display.setText(currentText + ".");
        }
    }

    private void handleOperation(String op) {
        if (!isOperationPressed) {
            if (!operation.isEmpty()) {
                handleEquals();
            } else {
                firstNumber = Double.parseDouble(display.getText());
            }
        }
        operation = op;
        isOperationPressed = true;
    }

    private void handleEquals() {
        if (operation.isEmpty()) {
            return;
        }

        try {
            double secondNumber = Double.parseDouble(display.getText());
            double result = 0;

            switch (operation) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "×":
                    result = firstNumber * secondNumber;
                    break;
                case "÷":
                    if (secondNumber == 0) {
                        display.setText("Ошибка");
                        operation = "";
                        isOperationPressed = false;
                        return;
                    }
                    result = firstNumber / secondNumber;
                    break;
            }

            // Форматируем результат
            if (result == (long) result) {
                display.setText(String.valueOf((long) result));
            } else {
                display.setText(String.valueOf(result));
            }

            firstNumber = result;
            operation = "";
            isOperationPressed = true;
        } catch (NumberFormatException ex) {
            display.setText("Ошибка");
            operation = "";
            isOperationPressed = false;
        }
    }

    private void handleClear() {
        display.setText("0");
        firstNumber = 0;
        operation = "";
        isOperationPressed = false;
    }

    private void handleClearEntry() {
        display.setText("0");
        isOperationPressed = false;
    }

    private void handleBackspace() {
        String currentText = display.getText();
        if (currentText.length() > 1 && !currentText.equals("0")) {
            display.setText(currentText.substring(0, currentText.length() - 1));
        } else {
            display.setText("0");
        }
        isOperationPressed = false;
    }

    private void handleSignChange() {
        String currentText = display.getText();
        if (!currentText.equals("0")) {
            if (currentText.startsWith("-")) {
                display.setText(currentText.substring(1));
            } else {
                display.setText("-" + currentText);
            }
        }
    }

    private void handleBaseConversion(int targetBase) {
        try {
            String currentText = display.getText();
            
            // Пропускаем конвертацию если отображается ошибка
            if (currentText.equals("Ошибка")) {
                return;
            }

            // Парсим число как double, затем берем целую часть
            double value = Double.parseDouble(currentText);
            long longValue = (long) Math.abs(value);
            boolean isNegative = value < 0;

            String result;
            
            if (targetBase == 10) {
                // Для десятичной системы - просто форматируем текущее значение
                if (value == (long) value) {
                    result = String.valueOf((long) value);
                } else {
                    result = String.valueOf(value);
                }
            } else {
                // Конвертируем в нужную систему счисления
                switch (targetBase) {
                    case 2:
                        result = Long.toBinaryString(longValue);
                        break;
                    case 8:
                        result = Long.toOctalString(longValue);
                        break;
                    case 16:
                        result = Long.toHexString(longValue).toUpperCase();
                        break;
                    default:
                        result = currentText;
                }
                
                // Добавляем знак минус для отрицательных чисел
                if (isNegative) {
                    result = "-" + result;
                }
            }

            display.setText(result);
            isOperationPressed = true;
        } catch (NumberFormatException ex) {
            display.setText("Ошибка");
            isOperationPressed = false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


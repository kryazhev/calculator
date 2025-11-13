import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame {
    private JTextField display;
    private double firstNumber;
    private String operation;
    private boolean isOperationPressed;

    public Calculator() {
        initializeCalculator();
    }

    private void initializeCalculator() {
        setTitle("Калькулятор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Панель отображения
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Панель кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        // Кнопки
        String[] buttonLabels = {
            "C", "CE", "⌫", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "=",
            "BIN", "OCT", "DEC", "HEX"
        };

        for (String label : buttonLabels) {
            JButton button = createButton(label);
            buttonPanel.add(button);
        }

        // Добавляем компоненты
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Инициализация
        firstNumber = 0;
        operation = "";
        isOperationPressed = false;

        pack();
        setLocationRelativeTo(null);
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.addActionListener(new ButtonClickListener());

        // Стили для разных типов кнопок
        if (label.matches("[0-9]")) {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
        } else if (label.equals("=")) {
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.WHITE);
        } else if (label.matches("[+\\-×÷]")) {
            button.setBackground(new Color(255, 193, 7));
            button.setForeground(Color.BLACK);
        } else if (label.matches("BIN|OCT|DEC|HEX")) {
            button.setBackground(new Color(40, 167, 69));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(108, 117, 125));
            button.setForeground(Color.WHITE);
        }

        button.setPreferredSize(new Dimension(80, 60));
        button.setBorder(BorderFactory.createRaisedBevelBorder());

        return button;
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();

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
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}


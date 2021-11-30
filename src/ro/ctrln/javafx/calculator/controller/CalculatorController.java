package ro.ctrln.javafx.calculator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import ro.ctrln.javafx.calculator.operations.Operation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class CalculatorController {

    @FXML
    public TextArea calculatorOperationsArea;
    @FXML
    public Label errorsLabel;

    @FXML
    public void writeZero(ActionEvent actionEvent) {
        checkNewOperation();
        if (!calculatorOperationsArea.getText().equalsIgnoreCase("0")) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("0"));
        }
        setPositionCaret();
    }

    private void setPositionCaret() {
        calculatorOperationsArea.positionCaret(calculatorOperationsArea.getText().length());
    }

    @FXML
    public void writeOne(ActionEvent actionEvent) {
        checkNewOperation();
        writeDigit("1");
    }

    private void writeDigit(String digit) {
        checkNewOperation();
        if (replaceZero(digit)) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat(digit));
        }
        setPositionCaret();
    }

    private boolean replaceZero(String replacement) {
        boolean zeroReplaced = false;
        if (calculatorOperationsArea.getText().equalsIgnoreCase("0")) {
            calculatorOperationsArea.setText(replacement);
            zeroReplaced = true;
        }
        return !zeroReplaced;
    }

    @FXML
    public void writeTwo(ActionEvent actionEvent) {
        writeDigit("2");
    }

    @FXML
    public void writeThree(ActionEvent actionEvent) {
        writeDigit("3");
    }

    @FXML
    public void writeFour(ActionEvent actionEvent) {
        writeDigit("4");
    }

    @FXML
    public void writeFive(ActionEvent actionEvent) {
        writeDigit("5");
    }

    @FXML
    public void writeSix(ActionEvent actionEvent) {
        writeDigit("6");
    }

    @FXML
    public void writeSeven(ActionEvent actionEvent) {
        writeDigit("7");
    }

    @FXML
    public void writeEight(ActionEvent actionEvent) {
        writeDigit("8");
    }

    @FXML
    public void writeNine(ActionEvent actionEvent) {
        writeDigit("9");
    }

    private void checkNewOperation() {
        if (calculatorOperationsArea.getText().contains("=")) {
            calculatorOperationsArea.setText("");
        }
    }

    @FXML
    public void writeComma(ActionEvent actionEvent) {
        if (!commaAlreadyPresentOnOperand(calculatorOperationsArea.getText())) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("."));
        }
    }

    private boolean commaAlreadyPresentOnOperand(String text) {
        if (mathOperationsNotPresentOnCalculatorTextArea()) {
            return text.contains("."); //verificam operandul din partea stanga
        } else { //verificam operandul din partea dreapta

            String[] operands = {};
            for (String mathOperation : operationsCharacters) {
                if (operands.length == 2) {
                    break;
                }
                operands = splitOperation(text, mathOperation);
            }
            return operands[1].contains(".");
        }
    }

    @FXML
    public void addition(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea()){
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("+"));
        }
    }

    @FXML
    public void subtraction(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea()){
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("-"));
        }
    }

    @FXML
    public void multiplication(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea()){
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("*"));
        }
    }

    @FXML
    public void division(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea()){
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("/"));
        }
    }

    private boolean mathOperationsNotPresentOnCalculatorTextArea() {
        return !calculatorOperationsArea.getText().contains("+") &&
                !calculatorOperationsArea.getText().contains("-") &&
                !calculatorOperationsArea.getText().contains("/") &&
                !calculatorOperationsArea.getText().contains("*");
    }

    @FXML
    public void clearCalculatorOperationsArea(ActionEvent actionEvent) {
        calculatorOperationsArea.setText("");
    }

    @FXML
    public void evaluate(ActionEvent actionEvent) {
        String operation = calculatorOperationsArea.getText();
        if (!operation.isEmpty()) {
            if (operation.contains("+")) {
                performAddition(operation);
            } else if (operation.contains("-")) {
                performSubtraction(operation);
            } else if (operation.contains("*")) {
                performMultiplication(operation);
            } else if (operation.contains("/")) {
                performDivision(operation);
            } else {
                errorsLabel.setText("Avem o operatie necunoscuta!");
            }
        }
    }

    private void performAddition(String operation) {
        String[] operands = splitOperation(operation, "+");
        if (operands.length == 2) {
            doOperation(operands, Operation.ADDITION);
        }
    }

    private void performSubtraction(String operation) {
        String[] operands = splitOperation(operation, "-");
        if (operands.length == 2) {
            doOperation(operands, Operation.SUBTRACTION);
        }
    }

    private void performMultiplication(String operation) {
        String[] operands = splitOperation(operation, "*");
        if (operands.length == 2) {
            doOperation(operands, Operation.MULTIPLICATION);
        }
    }

    private void performDivision(String operation) {
        String[] operands = splitOperation(operation, "/");
        if (operands.length == 2) {
            doOperation(operands, Operation.DIVISION);
        }
    }

    private String[] splitOperation(String operation, String splitter) {
        String[] operands = {};
        try {
            if (Arrays.asList("+", "-", "/", "*").contains(splitter)) {
                operation = operation.replace(splitter, "----");
            }
            operands = operation.split("----");
        } catch (Exception ex) {
            errorsLabel.setText("Operanzi nedetectati!");
            ex.printStackTrace();
        }
        //errorsLabel.setText(operands[0] + " " + operands[1]);
        return operands;
    }

    private void doOperation(String[] operands, Operation operation) {
        try {
            BigDecimal firstOperand = new BigDecimal(cleanOperand(operands[0]));
            BigDecimal secondOperand = new BigDecimal(cleanOperand(operands[1]));

            switch (operation) {
                case ADDITION:
                    writeResult(firstOperand.add(secondOperand));
                    break;
                case SUBTRACTION:
                    writeResult(firstOperand.subtract(secondOperand));
                    break;
                case DIVISION:
                    writeResult(firstOperand.divide(secondOperand, RoundingMode.HALF_DOWN));
                    break;
                case MULTIPLICATION:
                    writeResult(firstOperand.multiply(secondOperand));
                    break;
            }
        } catch (NumberFormatException nfe) {
            errorsLabel.setText("Operanzii sunt gresiti!");
        }
    }

    private String cleanOperand(String operand) {
        return operand.replaceAll("\n", "");
    }

    private void writeResult(BigDecimal result) {
        calculatorOperationsArea.setText(calculatorOperationsArea.getText()
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .concat(" = ").concat(result.toString()));
    }

    @FXML
    public void handleKeyTyped(KeyEvent keyEvent) {
        if (allowedCharacter(keyEvent.getCharacter())) {
            //checkNewOperation();

            handleDigitCharacter(keyEvent);

            handleComma(keyEvent);

            handleOperations(keyEvent);

            handleEvaluationKeys(keyEvent);

        } else {
            keyEvent.consume();
        }
    }

    private void handleEvaluationKeys(KeyEvent keyEvent) {
        if (keyEvent.getCharacter().equalsIgnoreCase("=") || keyEvent.getCharacter().equalsIgnoreCase("\r")) {
            keyEvent.consume();
            evaluate(new ActionEvent());
        }
    }

    private void handleOperations(KeyEvent keyEvent) {
        if (operationCharacter(keyEvent.getCharacter())) {
            if (!mathOperationsNotPresentOnCalculatorTextArea()) {
                keyEvent.consume();
            }
        }
    }

    private void handleComma(KeyEvent keyEvent) {
        if (keyEvent.getCharacter().equalsIgnoreCase(".")) {
            writeComma(new ActionEvent());
            keyEvent.consume();
        }
    }

    private void handleDigitCharacter(KeyEvent keyEvent) {
        if (isDigitCharacter(keyEvent.getCharacter())) {

            switch (keyEvent.getCharacter()) {
                case "0":
                    writeZero(new ActionEvent());
                    break;
                case "1":
                    writeOne(new ActionEvent());
                    break;
                case "2":
                    writeTwo(new ActionEvent());
                    break;
                case "3":
                    writeThree(new ActionEvent());
                    break;
                case "4":
                    writeFour(new ActionEvent());
                    break;
                case "5":
                    writeFive(new ActionEvent());
                    break;
                case "6":
                    writeSix(new ActionEvent());
                    break;
                case "7":
                    writeSeven(new ActionEvent());
                    break;
                case "8":
                    writeEight(new ActionEvent());
                    break;
                case "9":
                    writeNine(new ActionEvent());
                    break;
            }

            keyEvent.consume();
        }
    }

    private List<String> allowedCharacters = Arrays
            .asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "=", "+", "-", "/", "*", "\r", "\n");

    private boolean allowedCharacter(String character) {
        return allowedCharacters.contains(character);
    }

    private List<String> operationsCharacters = Arrays.asList("+", "-", "/", "*");

    private boolean operationCharacter(String character) {
        return operationsCharacters.contains(character);
    }

    private List<String> digitCharacters = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    private boolean isDigitCharacter(String character) {
        return digitCharacters.contains(character);
    }

}

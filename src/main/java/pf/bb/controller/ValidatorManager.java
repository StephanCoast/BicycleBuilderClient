package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

/**
 * Diese Klasse regelt alle benötigten Validierungen des BicycleBuilder.
 * @author Alexander Rauch
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern & Frameworks Winter 2022/2023
 */
public class ValidatorManager {

    /**
     * Variablendeklaration für die Singleton ValidatorManager Instanz.
     */
    private static final ValidatorManager instance = new ValidatorManager();

    /**
     * Standard-Konstruktor der Klasse
     */
    public ValidatorManager() {
    }

    /**
     * Rückgabe-Methode der Singleton Instanz.
     * @return ValidatorManager Instanz
     */
    public static ValidatorManager getInstance() {
        return instance;
    }

    /**
     * Validatoren werden den Textfeldern zugeordnet.
     * @param textField Textfeld
     * @param validator Validator
     */
    public void initTextValidators(JFXTextField textField, RequiredFieldValidator validator) {
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { textField.validate(); }
        });
    }

    /**
     * Validatoren werden den Passwortfeldern zugeordnet.
     * @param passwordField Passwortfeld
     * @param validator Validator
     */
    public void initPasswordValidators(JFXPasswordField passwordField, RequiredFieldValidator validator) {
        passwordField.getValidators().add(validator);
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { passwordField.validate(); }
        });
    }

    /**
     * RegEx-Prüf-Strings werden den Textfeldern zugeordnet.
     * @param tf Textfeld
     * @param pattern RegEx-Pattern
     */
    public static void setTextFieldRules(JFXTextField tf, String pattern) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(pattern)) {
                tf.setText(newValue.replaceAll("[^" + pattern + "]", ""));
            }
        });
    }

    /**
     * Das Textfeld wird auf ein bestimmtes Symbol überprüft.
     * @param tf Textfeld
     * @param symbol Symbol-String
     * @return Boolean wahr/falsch
     */
    public static boolean textFieldNotHaveSymbol(JFXTextField tf, String symbol) { return !tf.getText().contains(symbol); }

    /**
     * Das Textfeld wird auf vorhandenen Inhalt überprüft.
     * @param tf Textfeld
     * @return Boolean wahr/falsch
     */
    public static boolean textFieldIsEmpty(JFXTextField tf) {
        return tf.getText().isEmpty();
    }

    /**
     * Das Passwortfeld wird auf vorhandenen Inhalt überprüft.
     * @param pf Passwortfeld
     * @return Boolean wahr/falsch
     */
    public static boolean pwFieldIsEmpty(JFXPasswordField pf) {
        return pf.getText().isEmpty();
    }
}

package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import pf.bb.model.Customer;
import pf.bb.model.User;

import java.util.ArrayList;

/**
 * Diese Klasse regelt alle benötigten Validierungen des BicycleBuilder.
 * @author Alexander Rauch
 * @version 1.0
 * TH-Brandenburg Semesterprojekt Pattern und Frameworks Winter 2022/2023
 */
public class ValidatorManager {

    /**
     * Variablendeklaration für die Singleton ValidatorManager Instanz.
     */
    private static final ValidatorManager instance = new ValidatorManager();
    /**
     * Variablendeklaration für eine Liste aller User zur Überprüfung von Benutzername und E-Mail-Adresse.
     */
    public final ArrayList<User> USERS = new ArrayList<>();
    /**
     * Variablendeklaration für eine Liste aller Kunden zur Überprüfung der E-Mail-Adresse.
     */
    public final ArrayList<Customer> CUSTOMERS = new ArrayList<>();

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
    public void setTextFieldRules(JFXTextField tf, String pattern) {
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
    public boolean textFieldNotHaveSymbol(JFXTextField tf, String symbol) { return !tf.getText().contains(symbol); }

    /**
     * Das Textfeld wird auf vorhandenen Inhalt überprüft.
     * @param tf Textfeld
     * @return Boolean wahr/falsch
     */
    public boolean textFieldIsEmpty(JFXTextField tf) {
        return tf.getText().isEmpty();
    }

    /**
     * Das Passwortfeld wird auf vorhandenen Inhalt überprüft.
     * @param pf Passwortfeld
     * @return Boolean wahr/falsch
     */
    public boolean pwFieldIsEmpty(JFXPasswordField pf) {
        return pf.getText().isEmpty();
    }

    /**
     * Die eingegebene E-Mail-Adresse des Benutzers wird auf Duplikate überprüft.
     * @param conditionMailString E-Mail-Adresse als String
     * @return Prüfungsergebnis true/false
     */
    public boolean textFieldUserMailExists(String conditionMailString) {
        boolean mailExists = false;
        for (User u : USERS) {
            if (u.email.equals(conditionMailString))  {
                mailExists = true;
                break;
            } else {
                mailExists = false;
            }
        }
        return mailExists;
    }

    /**
     * Der eingegebene Benutzername wird auf Duplikate überprüft.
     * @param conditionUserNameString Benutzername als String
     * @return Prüfungsergebnis true/false
     */
    public boolean textFieldUserNameExists(String conditionUserNameString) {
        boolean nameExists = false;
        for (User u : USERS) {
            if (u.name.equals(conditionUserNameString))  {
                nameExists = true;
                break;
            } else {
                nameExists = false;
            }
        }
        return nameExists;
    }

    /**
     * Die eingegebene E-Mail-Adresse des Kunden wird auf Duplikate überprüft.
     * @param conditionMailString E-Mail-Adresse als String
     * @return Prüfungsergebnis true/false
     */
    public boolean textFieldCustomerMailExists(String conditionMailString) {
        boolean mailExists = false;
        for (Customer c : CUSTOMERS) {
            if (c.email.equals(conditionMailString))  {
                mailExists = true;
                break;
            } else {
                mailExists = false;
            }
        }
        return mailExists;
    }
}

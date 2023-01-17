package pf.bb.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ValidatorManager {

    private static final ValidatorManager instance = new ValidatorManager();

    public ValidatorManager() {
    }

    public static ValidatorManager getInstance() {
        return instance;
    }

    public void initTextValidators(JFXTextField textField, RequiredFieldValidator validator) {
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) { textField.validate(); }
            }
        });
    }

    public void initPasswordValidators(JFXPasswordField passwordField, RequiredFieldValidator validator) {
        passwordField.getValidators().add(validator);
        passwordField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) { passwordField.validate(); }
            }
        });
    }

    public static void setTextFieldRules(JFXTextField tf, String pattern) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(pattern)) {
                tf.setText(newValue.replaceAll("[^" + pattern + "]", ""));
            }
        });
    }

    public static boolean textFieldNotHaveSymbol(JFXTextField tf, String symbol) { return !tf.getText().contains(symbol); }

    public static boolean textFieldIsEmpty(JFXTextField tf) {
        return tf.getText().isEmpty();
    }

    public static boolean pwFieldIsEmpty(JFXPasswordField pf) {
        return pf.getText().isEmpty();
    }
}

package com.example.agenda;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a person.
 *
 * @author Marco Jakob
 */
public class PersonEditDialogController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField birthdayField;


    private Stage dialogStage;
    private Persona persona;
    private boolean okClicked = false;

    @FXML
    private void initialize() {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPerson(Persona person) {
        this.persona = person;

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postalCodeField.setText(Integer.toString(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateUtil.format(person.getBirthday()));
        birthdayField.setPromptText("dd.mm.yyyy");
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            persona.setFirstName(firstNameField.getText());
            persona.setLastName(lastNameField.getText());
            persona.setStreet(streetField.getText());
            persona.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            persona.setCity(cityField.getText());
            persona.setBirthday(DateUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String error = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            error += "-El nombre no es válido\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            error += "-El apellido no es válido\n";
        }
        if (streetField.getText() == null || streetField.getText().length() == 0) {
            error += "-La calle no es válida\n";
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
            error += "-El CP no es válido\n";
        } else {
            // intenta convertir el CP a un entero
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {//si no es posible, se añade el error
                error += "-El CP no es válido (debe ser un entero)\n";
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            error += "-La ciudad no es válida\n";
        }

        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
            error += "-La fecha de nacimiento no es válida\n";
        } else {
            if (!DateUtil.validDate(birthdayField.getText())) {
                error += "-La fecha de nacimiento no es válida. Debe usar el formato dd.mm.yyyy\n";
            }
        }

        if (error.length() == 0) {//si no hay errores
            return true;
        } else {
            // Muestra el error o los errores que haya en cualquiera de los campos.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Error en algunos campos");
            alert.setHeaderText("Por favor, corrige los errores en los siguientes campos:");
            alert.setContentText(error);

            alert.showAndWait();

            return false;
        }
    }
}
package com.example.agenda;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PersonOverviewController {
    //utilizar una variable o atributo privado para que sea accesible por el programa completo
    @FXML
    private TableView<Persona> personTable;
    @FXML
    private TableColumn<Persona, String> firstNameColumn;
    @FXML
    private TableColumn<Persona, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    // Hago referencia a la clase ejecutable
    private Agenda agenda;

    public PersonOverviewController() {
    }

    @FXML
    private void initialize() {
        // Inicializo la tabla con sus dos columnas.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        //limpio los datos de la persona seleccionada anteriormente en caso de que la hubiera.
        showPersonDetails(null);

        //obtengo la persona que ha sido seleccionada en la tabla y muestro sus datos a la derecha.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;

        // Añado la información de cada persona a la tabla
        personTable.setItems(agenda.getPersonData());
    }

    private void showPersonDetails(Persona persona){
        if (persona != null){
            //si el parámetro no es nulo, añado los datos de la persona a los label correspondientes.
            firstNameLabel.setText(persona.getFirstName());
            lastNameLabel.setText((persona.getLastName()));
            streetLabel.setText(persona.getStreet());
            postalCodeLabel.setText(String.valueOf(persona.getPostalCode())); //tengo que convertir el codigo postal (int) a un String
            cityLabel.setText(persona.getCity());

            //se convierte la fecha a un String mediante el método format de la clase Persona
            birthdayLabel.setText(DateUtil.format(persona.getBirthday()));

        }else{
            //si el parámetro es nulo, vacío todas las etiquetas
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }
    @FXML
    private void handleDeletePerson(){
        //obtengo el indice seleccionado de la tabla
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            //si hay algo seleccionado, lo borro de la tabla
            personTable.getItems().remove(selectedIndex);
        }else {
            //sino, envío un mensaje diciendo que debe seleccionar una persona
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(agenda.getPrimaryStage());
            alert.setTitle("No hay selección");
            alert.setHeaderText("No hay ninguna persona seleccionada");
            alert.setContentText("Por favor, selecciona una persona en la tabla antes de pulsar el botón.");
            alert.showAndWait();
        }


    }

    /*@FXML
    private void handleNewPerson() {//cuando el usuario quiere añadir otra persona
        Persona tempPerson = new Persona();
        boolean okClicked = agenda.showPersonEditDialog(tempPerson);

        if (okClicked) {
            agenda.getPersonData().add(tempPerson);
        }
    }*/

    @FXML
    private void handleEditPerson() {//cuando el usuario pulsa el botón editar
        Persona selectedPerson = personTable.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            boolean okClicked = agenda.showPersonEditDialog(selectedPerson);

            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // No hay selección.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(agenda.getPrimaryStage());
            alert.setTitle("No hay selección");
            alert.setHeaderText("No hay personas seleccionadas");
            alert.setContentText("Por favor, selecciona una persona en la tabla.");

            alert.showAndWait();
        }
    }


}
package com.example.agenda;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class RootLayoutController {

    private Agenda mainapp = new Agenda();

    public void setAgenda(Agenda mainapp){
        this.mainapp = mainapp;
    }

    //Creo un método con una agenda vacía para el botón new.
    @FXML
    private void handleNew(){
        mainapp.getPersonData();
        mainapp.setPersonFilePath(null);
    }

    //creo un métdo para seleccionar el fichero que cargar, el botón Open
    @FXML
    private void handleOpen(){
        //CREO UN FILECHOOSER
        FileChooser fileChooser = new FileChooser();

        //meto un filtro para que el fichero tenga extension xml
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files", ".xml");
        fileChooser.getExtensionFilters().add(extFilter);

        //abro el cuadro de dialogo de abrir fichero para seleccionar el xml y pasarselo al main.
        File file = fileChooser.showOpenDialog(mainapp.getPrimaryStage());

        //un if vara ver si el fichero es válido y cargarlo
        if (file != null){
            mainapp.loadPersonDataFromFile(file);
        }
    }

    //metodo para guardar los datos de la persona seleccionada en el fichero xml
    //si no hay fichero abierto, automaticamente llama al metodo saveFile.
    @FXML
    private void handleSave(){
        //declaro un objeto personfile que obtiene el path donde guardar los ficheros del método getPersonFilePath()
        //creado en mainapp
        File personFile = mainapp.getPersonFilePath();
        if (personFile != null){
            mainapp.savePersonDataToFile(personFile);
        }else {
            //si no hay ningun xml abierto, llamamos al metodo handleSaveAs
            handlesaveAs();
        }
    }

    @FXML
    private void handlesaveAs() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        //creo el cuadro de diálogo para guardar y le paso como objeto el stage del mainapp
        File file = fileChooser.showSaveDialog(mainapp.getPrimaryStage());

        //condicional para ver si el file es valido
        if (file != null){
            mainapp.savePersonDataToFile(file);
        }

    }

    //crear el metodo para el acerca de
    @FXML
    public void handleAbout(){
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("AdressApp");
        a.setHeaderText("About");
        a.setContentText("Autor: MJacob, Autor en español: Jesús García Segovia");
    }

    @FXML
    public void handleClose(){
        System.exit(0);
    }
}

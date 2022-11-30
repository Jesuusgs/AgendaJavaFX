package com.example.agenda;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class Agenda extends Application{

    private ObservableList<Persona> personData = FXCollections.observableArrayList();

    // Constructor

    public Agenda() {
        // Añadir personas a una lista
        personData.add(new Persona("Hans", "Muster"));
        personData.add(new Persona("Ruth", "Mueller"));
        personData.add(new Persona("Heinz", "Kurz"));
        personData.add(new Persona("Cornelia", "Meier"));
        personData.add(new Persona("Werner", "Meyer"));
        personData.add(new Persona("Lydia", "Kunz"));
        personData.add(new Persona("Anna", "Best"));
        personData.add(new Persona("Stefan", "Meier"));
        personData.add(new Persona("Martin", "Mueller"));
    }

    //devuelve la lista de personas
    public ObservableList<Persona> getPersonData() {
        return personData;
    }

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Agenda");

        //añadir un icono a la aplicación
        this.primaryStage.getIcons().add(new Image("file:images/agenda.png"));

        initRootLayout();

        showPersonOverview();
    }

    private void initRootLayout() {
        try {
            // Cargar el rootlayout desde el fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Agenda.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Mostrar scene.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Acesso del controlador.
            RootLayoutController controller = loader.getController();
            controller.setAgenda(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Intentar abrir el último fichero.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    public void showPersonOverview() {
        try {
            // Cargar el fichero PersonOverview.fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PersonOverview.fxml"));

            //Cargar el contenido del fxml en el AnchorPane
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Posicionar el personOverview en el centro del rootLayout.
            rootLayout.setCenter(personOverview);

            // Dar acceso a la clase ejecutable al controlador
            PersonOverviewController controller = loader.getController();
            controller.setAgenda(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
      launch();
    }

    public boolean showPersonEditDialog(Persona persona) {
        try {
            // Carga el fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Agenda.class.getResource("PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea el Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar persona");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Le pasa la persona al controlador.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(persona);

            // Muestra la ventana hasta que el usuario decide cerrarla
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Agenda.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    //Establecer el path obtenido en el método información
    public void setPersonFilePath(File file){
        //creo el objeto pref
        Preferences pref = Preferences.userNodeForPackage(Agenda.class);

        //si hay fichero, obtiene su ruta y pone el título
        //si no lo hay, descarto la ruta que ha pasado el preferences.
        if (file != null){
            //obtener la ruta y meterlo en el filepath
            pref.put("filepath", file.getPath());

            //cambia el título del stage
            primaryStage.setTitle("AdressApp " + file.getName());

        }else {
            pref.remove("filePath");
            primaryStage.setTitle("AdressApp");
        }
    }

    //metodo para cargar el fichero especificado con el wrapper, fichero xml
    public void loadPersonDataFromFile(File file){
        try {
            //Creamos el contexto instanciando la clase que cree
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);

            //Utilizo un objeto para crear un Unmarshaller.
            Unmarshaller um = context.createUnmarshaller();

            //Creo un objeto para leer xml del fichero xml
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            //una vex extraídos los datos del xml con el Unmarshal voy a añadir los datos al objeto
            personData.clear();
            personData.addAll(wrapper.getPerson());
            personData.clear();

            //guardo el path
            setPersonFilePath(file);

        }catch (Exception e){
            //mano un mensaje que avise al usuario de que ha habido un error al leer el fichero xml.
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("No se puede cargar");
            a.setContentText("No se puede cargar el fichero " + file.getPath());
            a.showAndWait();


        }
    }

    //metodo para guardar la persona seleccionada en el fichero xml
    public void savePersonDataToFile(File file){
        try {
            //Creo el contexto y lo conecto al archivo xml
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);

            //Declaro un marshall para meter datos en el xml
            Marshaller m = context.createMarshaller();

            //Configuro el Marshaller para meter los datos en el xml
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //uso el envoltorio para fijar donde voy a meter los datos
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPerson(personData);

            //escribo los datos xml al fichero
            m.marshal(wrapper, file);

            //guardo el path
            setPersonFilePath(file);

        }catch (Exception e){
            //alert para controlar las excepciones
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("No se puede guardar");
            a.setContentText("No se puede guardar el fichero " + file.getPath());
            a.showAndWait();
        }
    }

}
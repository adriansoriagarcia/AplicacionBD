package es.adriansoriagarcia.aplicacionbd;

import es.adriansoriagarcia.aplicacionbd.entities.Depart;
import es.adriansoriagarcia.aplicacionbd.entities.Emple;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.Query;
import javax.persistence.RollbackException;

public class SecondaryController implements Initializable{

    private Emple empleado;
    
    private static final char CASADO = 'C';
    private static final char SOLTERO = 'S';
    private static final char VIUDO = 'V';
    
    private static final String CARPETA_FOTOS = "Fotos";
    
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellido;
    @FXML
    private TextField textFieldOficio;
    @FXML
    private TextField textFieldNumEmple;
    @FXML
    private TextField textFieldDir;
    @FXML
    private TextField textFieldSalario;
    @FXML
    private TextField textFieldComision;
    @FXML
    private TextField textFieldHijos;
    @FXML
    private ToggleGroup estadoCivilGroup;
    @FXML
    private CheckBox checkBoxBaja;
    @FXML
    private DatePicker datePickerFecha;
    @FXML
    private RadioButton radioButtonSoltero;
    @FXML
    private RadioButton radioButtonCasado;
    @FXML
    private RadioButton radioButtonViudo;
    @FXML
    private ComboBox<Depart> comboBoxDepartamento;
    @FXML
    private BorderPane rootSecondary;
    @FXML
    private ImageView imageViewFoto;

    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){  
    }
    
    public void setEmpleado(Emple empleado) {
        App.em.getTransaction().begin();
        this.empleado = empleado;
        mostrarDatos();
    }
    
    private void mostrarDatos(){
        textFieldNombre.setText(empleado.getNombre());
        textFieldApellido.setText(empleado.getApellido());
        textFieldOficio.setText(empleado.getOficio());
        
        if(empleado.getEmpNo() != null){
            textFieldNumEmple.setText(String.valueOf(empleado.getEmpNo()));
        }
        
        if(empleado.getDir() != null){
            textFieldDir.setText(String.valueOf(empleado.getDir()));
        }
        
        if(empleado.getNumHijos() != null){
            textFieldHijos.setText(String.valueOf(empleado.getNumHijos()));
        }
        
        if(empleado.getSalario() != null){
            textFieldSalario.setText(String.valueOf(empleado.getSalario()));
        }
        
        if(empleado.getComision() != null){
            textFieldComision.setText(String.valueOf(empleado.getComision()));
        }
        
        if(empleado.getBaja() != null){
            checkBoxBaja.setSelected(empleado.getBaja());
        }
        
        if(empleado.getEstadoCivil() != null){
            switch (empleado.getEstadoCivil()){
                case CASADO:
                    radioButtonCasado.setSelected(true);
                    break;
                case SOLTERO:
                    radioButtonSoltero.setSelected(true);
                    break;   
                case VIUDO:
                    radioButtonViudo.setSelected(true);
                    break;    
            }
        }
        
        if(empleado.getFechaAlt() != null){
            Date date = empleado.getFechaAlt();
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.toLocalDate();
            datePickerFecha.setValue(localDate);
        }
        
        Query queryDepartamentoFindAll = App.em.createNamedQuery("Depart.findAll");
        List<Depart> listDepartamentos = queryDepartamentoFindAll.getResultList();
        
        comboBoxDepartamento.setItems(FXCollections.observableList(listDepartamentos));
        if(empleado.getDeptNo() != null){
            comboBoxDepartamento.setValue(empleado.getDeptNo());
        }
        comboBoxDepartamento.setCellFactory((ListView<Depart> l) -> new ListCell<Depart>(){
            @Override
            protected void updateItem (Depart departamento, boolean empty){
                super.updateItem(departamento, empty);
                if(departamento == null || empty){
                    setText("");
                }else {
                    setText(departamento.getDeptNo() + "-" + departamento.getDnombre());
                } 
            }
        });
        
        //Formato para el valor mostrado actualmente como seleccionado
        comboBoxDepartamento.setConverter(new StringConverter<Depart>(){
            @Override
            public String toString (Depart departamento){
                if(departamento == null ){
                    return null;
                }else {
                    return departamento.getDeptNo() + "-" + departamento.getDnombre();
                } 
            }

            @Override
            public Depart fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        if(empleado.getImagen() != null){
            String imageFileName = empleado.getImagen();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()){
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encuentra la imágen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        boolean errorFormato = false;
        
        empleado.setNombre(textFieldNombre.getText());
        empleado.setApellido(textFieldApellido.getText());
        empleado.setOficio(textFieldOficio.getText());
        
        if(!textFieldNumEmple.getText().isEmpty()){
            try{
                empleado.setEmpNo(Short.valueOf(textFieldNumEmple.getText()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de empleado no váido");
                alert.showAndWait();
                textFieldNumEmple.requestFocus();
            }
        }
        
        if(!textFieldDir.getText().isEmpty()){
            try{
                empleado.setDir(Short.valueOf(textFieldDir.getText()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de director no váido");
                alert.showAndWait();
                textFieldDir.requestFocus();
            }
        }
        
        if(!textFieldHijos.getText().isEmpty()){
            try{
                empleado.setNumHijos(Short.valueOf(textFieldHijos.getText()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de hijos no váido");
                alert.showAndWait();
                textFieldDir.requestFocus();
            }
        }
        
        if(!textFieldSalario.getText().isEmpty()){
            try{
                empleado.setSalario(BigDecimal.valueOf(Double.valueOf(textFieldSalario.getText()).doubleValue()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salario no valido");
                alert.showAndWait();
                textFieldSalario.requestFocus();
            }
        }
        
        empleado.setBaja(checkBoxBaja.isSelected());
        
        if (radioButtonCasado.isSelected()){
            empleado.setEstadoCivil(CASADO);
        }
        if (radioButtonSoltero.isSelected()){
            empleado.setEstadoCivil(SOLTERO);
        }
        if (radioButtonViudo.isSelected()){
            empleado.setEstadoCivil(VIUDO);
        }
        
        if(datePickerFecha.getValue() != null){
            LocalDate localDate = datePickerFecha.getValue();
            ZonedDateTime zoneDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zoneDateTime.toInstant();
            Date date = Date.from(instant);
            empleado.setFechaAlt(date);
        }else {
            empleado.setFechaAlt(null);
        }
        
        empleado.setDeptNo(comboBoxDepartamento.getValue());
        
        if (!errorFormato){
            try{
                if(empleado.getEmpNo() == null){
                    System.out.println("Guardando nuevo empleado en BD");
                    App.em.persist(empleado);
                }else{
                    System.out.println("Actualizando empleado en BD");
                    App.em.merge(empleado);
                }
                App.em.getTransaction().commit();
                
                App.setRoot("primary");
                
            }catch(RollbackException ex){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se ha podido guardar los cambios. "
                    + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            } catch (IOException ex) {
                //No se ha podido cargar la ventana primary
                Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        App.em.getTransaction().rollback();
        
        try{
            App.setRoot("primary");
        }catch (IOException ex){
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionButtonExaminar(ActionEvent event) throws IOException {
        File carpertaFotos = new File(CARPETA_FOTOS);
        if(!carpertaFotos.exists()){
            carpertaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.png"),
                 new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        
        File file = fileChooser.showOpenDialog(rootSecondary.getScene().getWindow());
        if (file != null){
            try{
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                empleado.setImagen(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING,"Nombre de archivo duplicado");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresion ed imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen, \n"
                + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija a opción deseada");
        
        ButtonType buttonTypeEiminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonTypeEiminar, buttonTypeMantener, buttonTypeCancel);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == buttonTypeEiminar) {
            String imageFileName = empleado.getImagen();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if(file.exists()){
                file.delete();
            }
            empleado.setImagen(null);
            imageViewFoto.setImage(null);
        } else if (result.get() == buttonTypeMantener){
            empleado.setImagen(null);
            imageViewFoto.setImage(null);
        }
    }
}
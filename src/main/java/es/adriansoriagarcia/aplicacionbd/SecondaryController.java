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
    
    private static final char CASADO = 'C';//declaracion e inicialización
    private static final char SOLTERO = 'S';//declaracion e inicialización
    private static final char VIUDO = 'V';//declaracion e inicialización
    
    private static final String CARPETA_FOTOS = "Fotos";
    
    private final byte TAM_NOMBRE = 20;
    private final byte TAM_APELLIDO = 10;
    private final byte TAM_OFICIO = 10;
    
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
    
    //Metodo que recibe los datos de la primera ventana.
    public void setEmpleado(Emple empleado) {
        //Inicializamos la transación en el momento.
        App.em.getTransaction().begin();
        //Igualamos el empleado recibido.
        this.empleado = empleado;
        //Llamada al método mostrarDatos.
        mostrarDatos();
    }
    //Método encargado de mostrar en cada campo los datos del empleado.
    private void mostrarDatos(){
        //muestra el nombre del empleado
        textFieldNombre.setText(empleado.getNombre());
        //muestra el apellido del empleado
        textFieldApellido.setText(empleado.getApellido());
        //muestra el oficio del empleado
        textFieldOficio.setText(empleado.getOficio());
        
        //Comprobamos que no sea null
        if(empleado.getEmpNo() != null){
            //muestra el numero de empleado del empleado
            textFieldNumEmple.setText(String.valueOf(empleado.getEmpNo()));
        }
        
        //Comprobamos que no sea null
        if(empleado.getDir() != null){
            //muestra el dir de empleado del empleado
            textFieldDir.setText(String.valueOf(empleado.getDir()));
        }
        
        //Comprobamos que no sea null
        if(empleado.getNumHijos() != null){
            //muestra el numero de hijos de empleado
            textFieldHijos.setText(String.valueOf(empleado.getNumHijos()));
        }
        
        //Comprobamos que no sea null
        if(empleado.getSalario() != null){
            //muestra el salario de empleado
            textFieldSalario.setText(String.valueOf(empleado.getSalario()));
        }
        
        //Comprobamos que no sea null
        if(empleado.getComision() != null){
            //muestra la comision de empleado 
            textFieldComision.setText(String.valueOf(empleado.getComision()));
        }
        
        //Comprobamos que no sea null
        if(empleado.getBaja() != null){
            //muestra marcado o no
            checkBoxBaja.setSelected(empleado.getBaja());
        }
        
        //Comprobamos que no sea null
        if(empleado.getEstadoCivil() != null){
            //segun el estado civil activa el correspondiente
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
        
        //Comprobamos que no sea null
        if(empleado.getFechaAlt() != null){
            //Optiene la fecha del empleado
            Date date = empleado.getFechaAlt();
            //Realizamos varias conversiones
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.toLocalDate();
            //Le pasamos al datePicker el localdate.
            datePickerFecha.setValue(localDate);
        }
        
        //Creamos una consulta con todos los datos de los departamentos
        Query queryDepartamentoFindAll = App.em.createNamedQuery("Depart.findAll");
        //Creamos una variables list de objetos departamento
        List<Depart> listDepartamentos = queryDepartamentoFindAll.getResultList();
        //Asignamos al comoboBox los Items de todos los departamentos.
        comboBoxDepartamento.setItems(FXCollections.observableList(listDepartamentos));
        
        //Comprobamos que no sea null
        if(empleado.getDeptNo() != null){
            //Muestra el numero de departamento.
            comboBoxDepartamento.setValue(empleado.getDeptNo());
        }
        comboBoxDepartamento.setCellFactory((ListView<Depart> l) -> new ListCell<Depart>(){
            @Override
            protected void updateItem (Depart departamento, boolean empty){
                super.updateItem(departamento, empty);
                if(departamento == null || empty){
                    setText("");
                }else {
                    //Formamos un string con lo que se muestra en el desplegable.
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
        
        //Comprobamos que no sea null
        if(empleado.getImagen() != null){
            //obtenemos el nombre del archivo
            String imageFileName = empleado.getImagen();
            //creamos un objeto file con la imagen.
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            //si existe el archivo 
            if (file.exists()){
                //se lo asignamos al imagevier
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                //si no se encuentra muestra u alert.
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encuentra la imágen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        boolean errorFormato = false;//declaración e inicialización.
        //asignamos el valor del textfield

        if(textFieldNombre.getText().length() <= TAM_NOMBRE) {
            empleado.setNombre(textFieldNombre.getText());
        } else {
            errorFormato = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                    "El tamaño del nombre no puede ser superior a " + TAM_NOMBRE + " caracteres");
            alert.showAndWait();
            textFieldNombre.requestFocus();
        }
        
        if(textFieldApellido.getText().length() <= TAM_APELLIDO) {
            empleado.setNombre(textFieldApellido.getText());
        } else {
            errorFormato = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                    "El tamaño del apellido no puede ser superior a " + TAM_APELLIDO + " caracteres");
            alert.showAndWait();
            textFieldApellido.requestFocus();
        }
        
        if(textFieldOficio.getText().length() <= TAM_OFICIO) {
            empleado.setNombre(textFieldOficio.getText());
        } else {
            errorFormato = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                    "El tamaño del oficio no puede ser superior a " + TAM_OFICIO + " caracteres");
            alert.showAndWait();
            textFieldOficio.requestFocus();
        }
        empleado.setNombre(textFieldNombre.getText());
        empleado.setApellido(textFieldApellido.getText());
        empleado.setOficio(textFieldOficio.getText());
        
        //Si el textfield no esta vacio
        if(!textFieldNumEmple.getText().isEmpty()){
            try{
                empleado.setEmpNo(Short.valueOf(textFieldNumEmple.getText()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de empleado no válido");
                alert.showAndWait();
                textFieldNumEmple.requestFocus();
            }
        }
        //Si el textfield no esta vacio
        if(!textFieldDir.getText().isEmpty()){
            try{
                //cogemos el valor del textfield lo convertimos a short.
                empleado.setDir(Short.valueOf(textFieldDir.getText()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de director no válido");
                alert.showAndWait();
                textFieldDir.requestFocus();
            }
        }
        //Si el textfield no esta vacio
        if(!textFieldHijos.getText().isEmpty()){
            try{
                //cogemos el valor del textfield lo convertimos a short.
                empleado.setNumHijos(Short.valueOf(textFieldHijos.getText()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de hijos no válido");
                alert.showAndWait();
                textFieldDir.requestFocus();
            }
        }
        //Si el textfield no esta vacio
        if(!textFieldSalario.getText().isEmpty()){
            try{
                //cogemos el valor del textfield lo convertimos a bigdecimal.
                empleado.setSalario(BigDecimal.valueOf(Double.valueOf(textFieldSalario.getText()).doubleValue()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salario no valido");
                alert.showAndWait();
                textFieldSalario.requestFocus();
            }
        }
        
        //Guarda la opción seleccionada
        empleado.setBaja(checkBoxBaja.isSelected());
        
        //Si esta seleccionado
        if (radioButtonCasado.isSelected()){
            //Guarda dicha opción
            empleado.setEstadoCivil(CASADO);
        }
        //Si esta seleccionado
        if (radioButtonSoltero.isSelected()){
            //Guarda dicha opción
            empleado.setEstadoCivil(SOLTERO);
        }
        //Si esta seleccionado
        if (radioButtonViudo.isSelected()){
            //Guarda dicha opción
            empleado.setEstadoCivil(VIUDO);
        }
        
        //Guarda la fecha introducida.
        if(datePickerFecha.getValue() != null){
            LocalDate localDate = datePickerFecha.getValue();
            ZonedDateTime zoneDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zoneDateTime.toInstant();
            Date date = Date.from(instant);
            empleado.setFechaAlt(date);
        }else {
            empleado.setFechaAlt(null);
        }
        //Guarda el numero de departamento seleccionado.
        empleado.setDeptNo(comboBoxDepartamento.getValue());
        //si no existe error de formato
        if (!errorFormato){
            try{
                //Si el numero de empledo es nulo es un nuevo empleado
                if(empleado.getEmpNo() == null){
                    System.out.println("Guardando nuevo empleado en BD");
                    App.em.persist(empleado);
                }else{//Si existe el numero de empleado actualiza la base de datos.
                    System.out.println("Actualizando empleado en BD");
                    App.em.merge(empleado);
                }
                App.em.getTransaction().commit();
                //Volvemos a la primera ventana.
                App.setRoot("primary");
                
            }catch(RollbackException ex){
                //Muestra un alert si no se ha podido realizar el guardado.
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
        //Establecemos una carperta donde guardar las imagenes.
        File carpertaFotos = new File(CARPETA_FOTOS);
        //Si no existe la carpera la crea.
        if(!carpertaFotos.exists()){
            carpertaFotos.mkdir();
        }
        //Creamos el objeto filechooser ara la ventana de seleccion.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        //Añadimos los varios filtros.
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.png", "*.jpg"),
                 new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        //obtenemos el archivo seleccionado por el usuario
        File file = fileChooser.showOpenDialog(rootSecondary.getScene().getWindow());
        //si el usuario ha seleccionado una imganen.
        if (file != null){
            try{
                //Copia la seleccion a la carpeta creada anteriormente.
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                //guarda el nombre del archivo.
                empleado.setImagen(file.getName());
                Image image = new Image(file.toURI().toString());
                //asigna al imageView la imagen seleccionada.
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex){
                //Si esta duplicado salta el alert
                Alert alert = new Alert(Alert.AlertType.WARNING,"Nombre de archivo duplicado");
                alert.showAndWait();
            } catch (IOException ex){
                //si existe algun error nos muestra el alert.
                Alert alert = new Alert(Alert.AlertType.WARNING, "No se ha podido guardar la imágen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        //Alert que muestra la confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresion de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen, \n"
                + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija a opción deseada");
        //creamos los tres botones
        ButtonType buttonTypeEiminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        //Mostramos el alert con los tres botones
        alert.getButtonTypes().setAll(buttonTypeEiminar, buttonTypeMantener, buttonTypeCancel);
        
        Optional<ButtonType> result = alert.showAndWait();
        //si ha seleccionado el boton eliminar.
        if(result.get() == buttonTypeEiminar) {
            //obtenemos el bombre de la imagen
            String imageFileName = empleado.getImagen();
            //accedemos a la carpeta de fotos
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            //si existe lo elimina
            if(file.exists()){
                file.delete();
            }
            //lo eliminamos del empleado
            empleado.setImagen(null);
            //la eliminadmos del imageView.
            imageViewFoto.setImage(null);
            //si queremos mantener la imagen se mantiene en la carpera de foto pero 
            //se le elimina al empleado.
        } else if (result.get() == buttonTypeMantener){
            empleado.setImagen(null);
            imageViewFoto.setImage(null);
        }
    }
}
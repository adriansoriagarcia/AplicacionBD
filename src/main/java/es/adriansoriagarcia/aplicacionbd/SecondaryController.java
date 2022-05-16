package es.adriansoriagarcia.aplicacionbd;

import es.adriansoriagarcia.aplicacionbd.entities.Depart;
import es.adriansoriagarcia.aplicacionbd.entities.Emple;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javax.persistence.Query;

public class SecondaryController implements Initializable{

    private Emple empleado;
    
    private static final char CASADO = 'C';
    private static final char SOLTERO = 'S';
    private static final char VIUDO = 'V';
    
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
        });
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
    }

    @FXML
    private void onActionButtonExaminar(ActionEvent event) {
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
    }
}
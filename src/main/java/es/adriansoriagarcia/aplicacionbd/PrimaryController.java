package es.adriansoriagarcia.aplicacionbd;

import es.adriansoriagarcia.aplicacionbd.entities.Emple;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.Query;

public class PrimaryController implements Initializable{
    
    private Emple empleadoSeleccionado;
    @FXML
    private TableView<Emple> tableViewEmpleados;
    @FXML
    private TableColumn<Emple, Short> columnNumEmple;
    @FXML
    private TableColumn<Emple, String> columnNombre;
    @FXML
    private TableColumn<Emple, String> columnApellidos;
    @FXML
    private TableColumn<Emple, String> columnOficio;
    @FXML
    private TableColumn<Emple, String> columnNumDepart;
    @FXML
    private Button buttonNuevo;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonSuprimir;
    @FXML
    private TextArea textAreaNombre;
    @FXML
    private TextArea textAreaApellidos;
    @FXML
    private Button buttonGuardar;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        columnNumEmple.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnOficio.setCellValueFactory(new PropertyValueFactory<>("oficio"));
        columnNumDepart.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getDeptNo() != null){
                        String nombreDept = cellData.getValue().getDeptNo().getDnombre();
                        property.setValue(nombreDept);
                        
                    }
                    return property;
                }
        );
        tableViewEmpleados.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                empleadoSeleccionado = newValue;
                if(empleadoSeleccionado != null){
                    textAreaNombre.setText(empleadoSeleccionado.getNombre());
                    textAreaApellidos.setText(empleadoSeleccionado.getApellido());
                }else {
                    textAreaNombre.setText("");
                    textAreaApellidos.setText("");
                }
        });
                
        cargarTodosEmpleados();
    }
    
    private void cargarTodosEmpleados(){
        Query queryEmpleadoFindAll = App.em.createNamedQuery("Emple.findAll");
        List<Emple> listEmpleado = queryEmpleadoFindAll.getResultList();
        tableViewEmpleados.setItems(FXCollections.observableArrayList(listEmpleado));
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        if (empleadoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Deseas suprimir el siguiente registro?");
            alert.setContentText(empleadoSeleccionado.getNombre() + ""
                + empleadoSeleccionado.getApellido());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                App.em.getTransaction().begin();
                App.em.remove(empleadoSeleccionado);
                App.em.getTransaction().commit();
                tableViewEmpleados.getItems().remove(empleadoSeleccionado);
                tableViewEmpleados.getFocusModel().focus(null);
                tableViewEmpleados.requestFocus();
            }else {
                int numFilaSeleccionada = tableViewEmpleados.getSelectionModel().getSelectedIndex();
                tableViewEmpleados.getItems().set(numFilaSeleccionada, empleadoSeleccionado);
                TablePosition pos = new TablePosition(tableViewEmpleados, numFilaSeleccionada, null);
                tableViewEmpleados.getFocusModel().focus(pos);
                tableViewEmpleados.requestFocus();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Debe seleccionar un registro");
            alert.setTitle("Atención");
            alert.showAndWait();
        }
        
    }

}
    

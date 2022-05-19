package es.adriansoriagarcia.aplicacionbd;

import es.adriansoriagarcia.aplicacionbd.entities.Emple;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    @FXML
    private Button buttonBuscar;
    @FXML
    private CheckBox checkBoxCoincide;
    @FXML
    private TextField textFieldBuscar;


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
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void onActionButtonNuevo(ActionEvent event) {
        try{
           App.setRoot("secondary");
           SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController(); 
           empleadoSeleccionado = new Emple();
           secondaryController.setEmpleado(empleadoSeleccionado);
        }catch(IOException ex){
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
                
    }
    
    @FXML
    private void onActionButtonEditar(ActionEvent event) {
        if(empleadoSeleccionado != null) {
            try{
                App.setRoot("secondary");
                SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController(); 
                secondaryController.setEmpleado(empleadoSeleccionado);
                System.out.println(empleadoSeleccionado.getEmpNo());
            }catch(IOException ex){
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
        
                
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        if (empleadoSeleccionado != null) {
            empleadoSeleccionado.setNombre(textAreaNombre.getText());
            empleadoSeleccionado.setApellido(textAreaApellidos.getText());
            App.em.getTransaction().begin();
            App.em.merge(empleadoSeleccionado);
            App.em.getTransaction().commit();
            
            int numFilaSeleccionada = tableViewEmpleados.getSelectionModel().getSelectedIndex();
            tableViewEmpleados.getItems().set(numFilaSeleccionada, empleadoSeleccionado);
            TablePosition pos = new TablePosition(tableViewEmpleados, numFilaSeleccionada, null);
            tableViewEmpleados.getFocusModel().focus(pos);
            tableViewEmpleados.requestFocus();
        }
    }

    @FXML
    private void onActionButtonBuscar(ActionEvent event) {
        if (!textFieldBuscar.getText().isEmpty()){
            if(checkBoxCoincide.isSelected()){
                Query queryEmpleadoFindAll = App.em.createNamedQuery("Emple.findByNombre");
                queryEmpleadoFindAll.setParameter("nombre", textFieldBuscar.getText());
                List<Emple> listEmpleado = queryEmpleadoFindAll.getResultList();
                tableViewEmpleados.setItems(FXCollections.observableArrayList(listEmpleado));
            } else {
                String strQuery = "SELECT * FROM Emple WHERE LOWER(nombre) LIKE ";
                strQuery += "\'%" + textFieldBuscar.getText().toLowerCase() + "%\'";
                Query queryEmpleadoLikeNombre = App.em.createNativeQuery(strQuery, Emple.class);

                List<Emple> listEmpleado = queryEmpleadoLikeNombre.getResultList();
                tableViewEmpleados.setItems(FXCollections.observableArrayList(listEmpleado));

                Logger.getLogger(this.getClass().getName()).log(Level.INFO, strQuery);
            
            }
            
        } else {
            cargarTodosEmpleados();
        }
    }

}
    

package es.adriansoriagarcia.aplicacionbd;

import es.adriansoriagarcia.aplicacionbd.entities.Emple;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.Query;

public class PrimaryController implements Initializable{
    
    @FXML
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

    @Override
    public void initialize(URL url, ResourceBundle rb){
        columnNumEmple.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnOficio.setCellValueFactory(new PropertyValueFactory<>("oficio"));
        
        cargarTodosEmpleados();
    }
    
    private void cargarTodosEmpleados(){
        Query queryEmpleadoFindAll = App.em.createNamedQuery("Emple.findAll");
        List<Emple> listEmpleado = queryEmpleadoFindAll.getResultList();
        tableViewEmpleados.setItems(FXCollections.observableArrayList(listEmpleado));
    }

}
    

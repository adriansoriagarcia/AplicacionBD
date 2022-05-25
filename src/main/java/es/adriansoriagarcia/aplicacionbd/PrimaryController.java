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
    
    //Declaracion variable empleadoSeleccionado de tipo emple.
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

    //Este método se ejecuta automaticamente al abrir la ventana.
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Asignamos a las columnas el nombre del campo correspondiente de la tabla
        columnNumEmple.setCellValueFactory(new PropertyValueFactory<>("empNo"));
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnOficio.setCellValueFactory(new PropertyValueFactory<>("oficio"));
        columnNumDepart.setCellValueFactory(
                cellData -> {
                    //Creamos un objeto de la clase SimpleStringProperty
                    SimpleStringProperty property = new SimpleStringProperty();
                    //Comprobamos si no es null la provincia en cada empleado.
                    if (cellData.getValue().getDeptNo() != null){
                        //Cramos un String que contiene el nombre del departamento
                        String nombreDept = cellData.getValue().getDeptNo().getDnombre();
                        //Le añadimos el valor nombreDept a property
                        property.setValue(nombreDept);
                        
                    }
                    //retornamos la propiedad
                    return property;
                }
        );
        //Añadimos al tableView una accion "addListener" que está pendiente de que ocurra algo.
        //Cuando se seleccione un nuevo valor del tableView se ejecuta el siguiente código
        tableViewEmpleados.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                empleadoSeleccionado = newValue;//Guarda un nuevo valor de selección.
                //Si empleado seleccionado es distinto de nulo(Se ha seleccionado a un empleado).
                if(empleadoSeleccionado != null){
                    //Añadimos al textArea el nombre del empleado seleccionado
                    textAreaNombre.setText(empleadoSeleccionado.getNombre());
                    //Añadimos al textArea el apellido del empleado seleccionado
                    textAreaApellidos.setText(empleadoSeleccionado.getApellido());
                //Si es nulo deja vacion los textArea(No se ha seleccionado ningun empleado).
                }else {
                    textAreaNombre.setText("");
                    textAreaApellidos.setText("");
                }
        });
        
        //Llamada al método cargarTodosEmpleados.
        cargarTodosEmpleados();
    }
    
    private void cargarTodosEmpleados(){
        //Creamos una consulta sobre todos los datos de los empleados.
        Query queryEmpleadoFindAll = App.em.createNamedQuery("Emple.findAll");
        //Variable de tipo List de tipo Emple que contiene todos los empleados
        List<Emple> listEmpleado = queryEmpleadoFindAll.getResultList();
        //Asignamos al tableView la lista anterior.
        tableViewEmpleados.setItems(FXCollections.observableArrayList(listEmpleado));
    }

    //Método correspondiente al boton suprimir.
    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        //Si existe una persona seleccionada
        if (empleadoSeleccionado != null) {
            //Muestra un Alert de confimación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Deseas suprimir el siguiente registro?");
            alert.setContentText(empleadoSeleccionado.getNombre() + ""
                + empleadoSeleccionado.getApellido());
            //Obtenemos el botón seleccionado.
            Optional<ButtonType> result = alert.showAndWait();
            //Si le da a aceptar borramos el empleado.
            if (result.get() == ButtonType.OK){
                //Actualizamos el objeto en la base de datos.
                App.em.getTransaction().begin();
                //eliminamos el empleado seleccionado
                App.em.remove(empleadoSeleccionado);
                //Realizamos el commit.
                App.em.getTransaction().commit();
                //Eliminamos del tableView el objeto.
                tableViewEmpleados.getItems().remove(empleadoSeleccionado);
                tableViewEmpleados.getFocusModel().focus(null);
                tableViewEmpleados.requestFocus();  
            }else {//Selecciona de nuevo el empleado seleccionado
                int numFilaSeleccionada = tableViewEmpleados.getSelectionModel().getSelectedIndex();
                tableViewEmpleados.getItems().set(numFilaSeleccionada, empleadoSeleccionado);
                TablePosition pos = new TablePosition(tableViewEmpleados, numFilaSeleccionada, null);
                tableViewEmpleados.getFocusModel().focus(pos);
                tableViewEmpleados.requestFocus();
            }
        }else {//Si no se selecciona nada muestra un Alert informativo.  
            //Muestra un alert 
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
        
    }
    
    //Método correspondiente al boton nuevo.
    @FXML
    private void onActionButtonNuevo(ActionEvent event) {
        try{
           //Llamamos al metodo setRoot y le pasamos el nombre de la segunda pantalla como parametro.
           App.setRoot("secondary");
           //Obtener el objeto creado de la segunda pantalla.
           SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
           //Creamos un objeto empleado.
           empleadoSeleccionado = new Emple();
           //Pasamos a la segunda pantalla el nuevo objeto en blanco.
           secondaryController.setEmpleado(empleadoSeleccionado);
        }catch(IOException ex){
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
                
    }
    
    //Método correspondiente al boton editar.
    @FXML
    private void onActionButtonEditar(ActionEvent event) {
        //Si existe una persona seleccionada
        if(empleadoSeleccionado != null) {
            try{
                //Llamamos al metodo setRoot y le pasamos el nombre de la segunda pantalla como parametro.
                App.setRoot("secondary");
                SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController(); 
                //Pasamos a la segunda pantalla el empleado seleccionado..
                secondaryController.setEmpleado(empleadoSeleccionado);
                System.out.println(empleadoSeleccionado.getEmpNo());
            }catch(IOException ex){
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            //Alert que se muestra si se intenta editar sin seleccionar.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
        
                
    }
    
    //Método correspondiente al boton guardar.
    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        //Aseguramos que exista un empleado seleccionado.
        if (empleadoSeleccionado != null) {
            //Actualizamos el valor del nombre por el que contiene el textArea de nombre.
            empleadoSeleccionado.setNombre(textAreaNombre.getText());
            //Actualizamos el valor del apellido por el que contiene el textArea de apellido.
            empleadoSeleccionado.setApellido(textAreaApellidos.getText());
            //Guardamos el objeto en la base de datos.
            App.em.getTransaction().begin();
            //Hacemos un merge(update)de la base de datos.
            App.em.merge(empleadoSeleccionado);
            //Hacemos un commit(guardamos) en la base de dtos.
            App.em.getTransaction().commit();
            
            //Selecciona el numero de fila seleccionada.
            int numFilaSeleccionada = tableViewEmpleados.getSelectionModel().getSelectedIndex();
            //en dicha fila añade el objeto actualizado
            tableViewEmpleados.getItems().set(numFilaSeleccionada, empleadoSeleccionado);
            //Obtiene la posición seleccionada.
            TablePosition pos = new TablePosition(tableViewEmpleados, numFilaSeleccionada, null);
            //Le asigna el focus en la linea seleccionada.
            tableViewEmpleados.getFocusModel().focus(pos);
            tableViewEmpleados.requestFocus();
        }
    }
    
    //Método correspondiente al boton buscar.
    @FXML
    private void onActionButtonBuscar(ActionEvent event) {
        //Si el textfield no esta vacio
        if (!textFieldBuscar.getText().isEmpty()){
            //Si el checkBox esta seleccionado
            if(checkBoxCoincide.isSelected()){
                //Realizamos una consulta por nombre de empleado.
                Query queryEmpleadoFindAll = App.em.createNamedQuery("Emple.findByNombre");
                //Le asignamos un parametro con el valor del textfield.
                queryEmpleadoFindAll.setParameter("nombre", textFieldBuscar.getText());
                //Creamos una lista de tipo empleado con los resultados
                List<Emple> listEmpleado = queryEmpleadoFindAll.getResultList();
                //Asignamos al tableview la lista anterior.
                tableViewEmpleados.setItems(FXCollections.observableArrayList(listEmpleado));
            } else {//Si el checkBox no esta seleccionado
                //Creamos una consulta manualmente segun lo que necesitemos.
                String strQuery = "SELECT * FROM Emple WHERE LOWER(nombre) LIKE ";
                strQuery += "\'%" + textFieldBuscar.getText().toLowerCase() + "%\'";
                Query queryEmpleadoLikeNombre = App.em.createNativeQuery(strQuery, Emple.class);
                //Creamos una lista de tipo emple con todos los nombre de empleados que coincide
                List<Emple> listEmpleado = queryEmpleadoLikeNombre.getResultList();
                //Asignamos al tableview la lista anterior.
                tableViewEmpleados.setItems(FXCollections.observableArrayList(listEmpleado));

                Logger.getLogger(this.getClass().getName()).log(Level.INFO, strQuery);
            
            }
            
        } else {//Si el textfield esta vacion
            cargarTodosEmpleados();
        }
    }
}
    

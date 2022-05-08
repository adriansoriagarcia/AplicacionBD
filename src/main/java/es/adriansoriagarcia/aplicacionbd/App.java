package es.adriansoriagarcia.aplicacionbd;

import es.adriansoriagarcia.aplicacionbd.entities.Depart;
import es.adriansoriagarcia.aplicacionbd.entities.Emple;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static EntityManager em;

    @Override
    public void start(Stage stage) throws IOException {
        
        //conexion con la base de datos.
        try{
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicacionBDPU");
            em = emf.createEntityManager();
        }catch (PersistenceException ex){
            Logger.getLogger(App.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
        
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
       
//        Emple e = new Emple((short) 2021, "Pepe");
//        em.getTransaction().begin();
//        em.persist(e);
//        em.getTransaction().commit();
    }
    
    @Override
    public void stop() throws Exception {
        em.close();
        try{
           DriverManager.getConnection("jdbc:derby:BDEmpresa;shutdown=true");
        } catch(SQLException ex){
            
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
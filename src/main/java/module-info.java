module es.adriansoriagarcia.aplicacionbd {
    requires javafx.controls;
    requires javafx.fxml;
    
    requires java.instrument;
    requires java.persistence;
    requires java.sql;
    requires java.base;
    
    opens es.adriansoriagarcia.aplicacionbd.entities;
    opens es.adriansoriagarcia.aplicacionbd to javafx.fxml;

    exports es.adriansoriagarcia.aplicacionbd;
}

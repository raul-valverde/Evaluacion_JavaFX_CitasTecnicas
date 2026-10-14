module ni.edu.uam.asistech {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ni.edu.uam.asistech to javafx.fxml;
    exports ni.edu.uam.asistech;
}
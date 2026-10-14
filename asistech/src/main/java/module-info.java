 module ni.edu.uam.asistech {
        requires javafx.controls;
        requires javafx.fxml;
        requires java.sql;
        requires org.postgresql.jdbc;

        // Permite que FXMLLoader instancie y acceda a los controladores y vistas
        opens ni.edu.uam.asistech to javafx.fxml;
        opens ni.edu.uam.asistech.controller to javafx.fxml;

        // Permite que TableView lea las propiedades de los modelos
        opens ni.edu.uam.asistech.model to javafx.base;

        // Exporta los paquetes para acceso público del sistema
        exports ni.edu.uam.asistech;
        exports ni.edu.uam.asistech.controller;
        exports ni.edu.uam.asistech.model;
        }
package ni.edu.uam.asistech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal que arranca la aplicación JavaFX.
 * Permite cargar y probar el módulo de clientes conectado a PostgreSQL.
 *
 * Proyecto: Sistema de Gestión de Citas de Atención Técnica
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carga la interfaz visual FXML de clientes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ni/edu/uam/asistech/view/MainView.fxml"));
            Parent root = loader.load();

            // Configuración de la escena y el escenario (ventana)
            Scene scene = new Scene(root);
            primaryStage.setTitle("TecnoAsistencia Nicaragua S.A. - Gestión de Clientes");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(600);
            // ABRIR EN PANTALLA COMPLETA / MAXIMIZADA
            primaryStage.setMaximized(true);
            primaryStage.show();

            System.out.println(" Aplicación JavaFX iniciada con éxito.");

        } catch (Exception e) {
            System.err.println(" Error al cargar la interfaz FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
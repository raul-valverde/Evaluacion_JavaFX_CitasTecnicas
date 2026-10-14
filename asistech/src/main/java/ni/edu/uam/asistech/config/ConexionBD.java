package ni.edu.uam.asistech.config;

import java.sql.*;

/**
 * Clase centralizada para gestionar la conexión a la base de datos PostgreSQL
 * Integrante: Raúl Valverde
 */
public class ConexionBD {

    // Configuración de la base de datos local
    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_citas";
    private static final String USUARIO = "postgres";
    private static final String CLAVE = ""; //!!!!!! Escribe tu contraseña entre las comillas para que funcione

    /**
     * Obtiene y retorna una conexión activa con PostgreSQL.
     *
     * @return Connection objeto de conexión.
     * @throws SQLException si ocurre un error al conectarse.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Carga explícita del driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: Driver JDBC de PostgreSQL no encontrado. Revisa tu pom.xml o librerías.", e);
        }

        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    /**
     * Comprueba la conectividad con la base de datos ejecutando la consulta fija
     */
    public static boolean probarConexion() {
        String sqlPrueba = "SELECT 1";

        // Cierre automático de recursos con try-with-resources
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlPrueba)) {

            if (rs.next()) {
                int valorPrueba = rs.getInt(1);
                System.out.println("=================================================");
                System.out.println("✅ CONEXIÓN EXITOSA A POSTGRESQL (gestion_citas)");
                System.out.println("Prueba SELECT 1 retornó: " + valorPrueba);
                System.out.println("=================================================");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("=================================================");
            System.err.println("❌ ERROR AL CONECTAR A LA BASE DE DATOS");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Código de error SQL: " + e.getErrorCode());
            System.err.println("=================================================");
        }

        return false;
    }

    /**
     * Método ejecutable para probar la conexión directamente desde IntelliJ IDEA.
     */
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión JDBC...");
        probarConexion();
    }
}
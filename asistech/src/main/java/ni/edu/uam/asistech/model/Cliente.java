package ni.edu.uam.asistech.model;

/**
 * Clase de modelo que representa la entidad Cliente de la base de datos.
 * Utilizada para transportar datos entre la base de datos (JDBC), los controladores
 * y los componentes visuales de JavaFX (TableView, ComboBox).
 * Proyecto: Sistema de Gestión de Citas de Atención Técnica (TecnoAsistencia Nicaragua S.A.)
 * Integrante: Kellys Bellanger
 */
public class Cliente {

    // Atributos privados según el modelo relacional de la base de datos
    private int id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private boolean activo;

    /**
     * Constructor vacío por defecto.
     * Requerido para instanciación flexible.
     */
    public Cliente() {
        this.activo = true; // Por defecto activo
    }

    /**
     * Constructor para la creación de un nuevo cliente (sin ID).
     * El ID es generado automáticamente por la base de datos (SERIAL / AUTO_INCREMENT).
     */
    public Cliente(String nombres, String apellidos, String telefono, String correo, boolean activo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.activo = activo;
    }

    /**
     * Constructor completo (con ID).
     * Utilizado para recuperar registros existentes desde la base de datos.
     */
    public Cliente(int id, String nombres, String apellidos, String telefono, String correo, boolean activo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.activo = activo;
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // =========================================================================
    // MÉTODOS AUXILIARES ÚTILES PARA JAVAFX
    // =========================================================================

    /**
     * Retorna el nombre completo concatenando nombres y apellidos.
     * Muy útil para mostrar en la TableView de Citas y en los reportes (RF04).
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Sobrescribe el método toString.
     * Permite que el ComboBox de selección de cliente muestre el nombre completo
     * de forma automática sin mostrar la referencia de objeto.
     */
    @Override
    public String toString() {
        return getNombreCompleto() + " (" + correo + ")";
    }
}

package ni.edu.uam.asistech.controller;

import ni.edu.uam.asistech.dao.ClienteDAO;
import ni.edu.uam.asistech.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Optional;

/**
 * Controlador para la gestión de clientes con refresco dinámico de la TableView.
 * Proyecto: Sistema de Gestión de Citas de Atención Técnica
 * Integrante: Steven Flores
 */
public class ClienteController implements Initializable {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private ObservableList<Cliente> listaClientes;
    private FilteredList<Cliente> listaFiltrada;

    // Componentes FXML del Formulario
    @FXML private TextField txtId;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtBuscar;
    @FXML private CheckBox chkActivo;

    // Botones
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnLimpiarBusqueda;

    // Tabla y Columnas
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombres;
    @FXML private TableColumn<Cliente, String> colApellidos;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, Boolean> colActivo;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        cargarClientes();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colActivo.setCellFactory(column -> new TableCell<Cliente, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Activo" : "Inactivo");
                    setStyle(item ? "-fx-text-fill: #16a34a; -fx-font-weight: bold;"
                            : "-fx-text-fill: #dc2626;");
                }
            }
        });
    }

    /**
     * Consulta PostgreSQL a través del DAO y actualiza la lista observable.
     */
    public void cargarClientes() {
        List<Cliente> clientesBD = clienteDAO.obtenerTodos();
        listaClientes = FXCollections.observableArrayList(clientesBD);
        listaFiltrada = new FilteredList<>(listaClientes, p -> true);
        tblClientes.setItems(listaFiltrada);
    }

    /**
     * Guarda un nuevo cliente y refresca dinámicamente la TableView.
     */
    @FXML
    public void guardarCliente() {
        // Pasa null indicando que es un nuevo registro
        if (!validarCampos(null)) return;

        Cliente cliente = new Cliente(
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                chkActivo.isSelected()
        );

        if (clienteDAO.insertar(cliente)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente registrado con éxito.");
            cargarClientes();
            limpiarFormulario();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al guardar el cliente en la base de datos.");
        }
    }

    @FXML
    public void modificarCliente() {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un cliente de la tabla para modificar.");
            return;
        }

        int idCliente = Integer.parseInt(txtId.getText());

        // Pasa el idCliente para ignorar su propio correo durante la verificación de unicidad
        if (!validarCampos(idCliente)) return;

        Cliente cliente = new Cliente(
                idCliente,
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                chkActivo.isSelected()
        );

        if (clienteDAO.actualizar(cliente)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente actualizado correctamente.");
            cargarClientes();
            limpiarFormulario();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la información del cliente.");
        }
    }

    /**
     * Elimina el cliente seleccionado y refresca dinámicamente la TableView.
     */
    @FXML
    public void eliminarCliente() {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un cliente de la tabla para eliminar.");
            return;
        }

        int idCliente = Integer.parseInt(txtId.getText());

        // Control de Integridad Referencial
        if (clienteDAO.tieneCitasAsociadas(idCliente)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Acción Bloqueada",
                    "No se puede eliminar este cliente porque tiene citas técnicas registradas a su nombre.");
            return;
        }

        // Alerta de Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar al cliente?");
        confirmacion.setContentText("Cliente ID: " + idCliente);

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (clienteDAO.eliminar(idCliente)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado correctamente de la base de datos.");
                cargarClientes();
                limpiarFormulario();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el cliente.");
            }
        }
    }

    @FXML
    public void seleccionarClienteTabla() {
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            txtId.setText(String.valueOf(clienteSeleccionado.getId()));
            txtNombres.setText(clienteSeleccionado.getNombres());
            txtApellidos.setText(clienteSeleccionado.getApellidos());
            txtTelefono.setText(clienteSeleccionado.getTelefono());
            txtCorreo.setText(clienteSeleccionado.getCorreo());
            chkActivo.setSelected(clienteSeleccionado.isActivo());
        }
    }

    @FXML
    public void buscarCliente() {
        String filtro = txtBuscar.getText();

        // Si el filtro está vacío o es nulo, se muestran todos los clientes
        if (filtro == null || filtro.trim().isEmpty()) {
            listaFiltrada.setPredicate(cliente -> true);
            return;
        }

        // Normalizar texto a minúsculas para búsqueda case-insensitive
        String filtroLower = filtro.toLowerCase().trim();

        listaFiltrada.setPredicate(cliente -> {
            // Evaluación de coincidencia parcial en nombres, apellidos o correo
            boolean coincideNombre = cliente.getNombres() != null &&
                    cliente.getNombres().toLowerCase().contains(filtroLower);

            boolean coincideApellido = cliente.getApellidos() != null &&
                    cliente.getApellidos().toLowerCase().contains(filtroLower);

            boolean coincideCorreo = cliente.getCorreo() != null &&
                    cliente.getCorreo().toLowerCase().contains(filtroLower);

            return coincideNombre || coincideApellido || coincideCorreo;
        });
    }

    /**
     * Limpia la caja de texto de búsqueda y restaura todos los registros en la TableView.
     */
    @FXML
    public void limpiarBusqueda() {
        txtBuscar.clear();
        if (listaFiltrada != null) {
            listaFiltrada.setPredicate(cliente -> true);
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        chkActivo.setSelected(true);
        tblClientes.getSelectionModel().clearSelection();
    }

    private boolean validarCampos(Integer idClienteActual) {
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        // 1. Validar Campos Obligatorios
        if (nombres.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obligatorios",
                    "Todos los campos (Nombres, Apellidos, Teléfono y Correo) son requeridos.");
            return false;
        }

        // 2. Validar Formato Regex del Correo
        if (!correo.matches(EMAIL_REGEX)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formato Inválido",
                    "El correo electrónico ingresado no tiene un formato válido (Ej. usuario@dominio.com).");
            txtCorreo.requestFocus();
            return false;
        }

        // 3. Validar Unicidad de Correo en PostgreSQL
        if (clienteDAO.existeCorreo(correo, idClienteActual)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Correo Duplicado",
                    "El correo electrónico '" + correo + "' ya pertenece a otro cliente registrado.");
            txtCorreo.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
package ni.edu.uam.asistech.controller;

import ni.edu.uam.asistech.dao.ClienteDAO;
import ni.edu.uam.asistech.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de gestión de clientes.
 * Proyecto: Sistema de Gestión de Citas de Atención Técnica
 * Integrante: Steven Flores
 */
public class ClienteController implements Initializable {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private ObservableList<Cliente> listaClientes;

    // Componentes FXML del Formulario
    @FXML private TextField txtId;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtBuscar;
    @FXML private CheckBox chkActivo;

    // Botones FXML
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        cargarClientes();
    }

    /**
     * Mapea cada columna de la TableView con las propiedades del modelo Cliente
     * y aplica formato personalizado para mostrar 'Activo'/'Inactivo' en la columna booleana.
     */
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // Enlace de la columna booleana activo con formateador visual
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
     * Consulta los clientes persistidos en PostgreSQL a través del DAO
     * y actualiza el contenido de la TableView.
     */
    public void cargarClientes() {
        List<Cliente> clientesBD = clienteDAO.obtenerTodos();
        listaClientes = FXCollections.observableArrayList(clientesBD);
        tblClientes.setItems(listaClientes);
    }

    /**
     * Evento al seleccionar una fila de la tabla: carga los datos del cliente
     * seleccionado en los campos del formulario lateral.
     */
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

    // Métodos de eventos FXML (Pendientes de implementar lógica completa)
    @FXML public void guardarCliente() {}
    @FXML public void modificarCliente() {}
    @FXML public void eliminarCliente() {}
    @FXML public void limpiarFormulario() {}
    @FXML public void buscarCliente() {}
    @FXML public void limpiarBusqueda() {}
}
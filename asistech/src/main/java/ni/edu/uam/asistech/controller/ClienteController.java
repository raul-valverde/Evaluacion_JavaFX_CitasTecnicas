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
 * Vincula los componentes FXML con la lógica del DAO y llena la TableView.
 *
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
     * Mapea cada columna de la TableView con las propiedades del modelo Cliente.
     */
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
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

    // Métodos de eventos FXML (Pendientes de implementar lógica completa)
    @FXML public void guardarCliente() {}
    @FXML public void modificarCliente() {}
    @FXML public void eliminarCliente() {}
    @FXML public void limpiarFormulario() {}
    @FXML public void buscarCliente() {}
    @FXML public void limpiarBusqueda() {}
    @FXML public void seleccionarClienteTabla() {}
}
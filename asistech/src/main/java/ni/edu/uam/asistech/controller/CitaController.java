package ni.edu.uam.asistech.controller;

import ni.edu.uam.asistech.dao.ClienteDAO;
import ni.edu.uam.asistech.model.CitaTecnica;
import ni.edu.uam.asistech.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para la gestión de citas técnicas.
 * Vincula la interfaz VistaCitas.fxml con la lógica de negocio y PostgreSQL.
 *
 * Proyecto: Sistema de Gestión de Citas de Atención Técnica
 * Integrante: Steven Flores
 */
public class CitaController implements Initializable {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    // Componentes FXML
    @FXML private TextField txtId;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private ComboBox<String> cmbTipoServicio;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TextArea taDescripcion;
    @FXML private TextField txtBuscar;

    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnLimpiarBusqueda;

    @FXML private TableView<CitaTecnica> tblCitas;
    @FXML private TableColumn<CitaTecnica, Integer> colId;
    @FXML private TableColumn<CitaTecnica, String> colCliente;
    @FXML private TableColumn<CitaTecnica, String> colFecha;
    @FXML private TableColumn<CitaTecnica, String> colHora;
    @FXML private TableColumn<CitaTecnica, String> colTipoServicio;
    @FXML private TableColumn<CitaTecnica, String> colEstado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarClientesEnComboBox();
        inicializarCombosEstaticos();
    }

    /**
     * Consulta todos los clientes registrados en PostgreSQL usando ClienteDAO
     * y los carga en el ComboBox configurando el formato visual del objeto.
     */
    private void cargarClientesEnComboBox() {
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(clientes);
        cmbCliente.setItems(listaClientes);

        // Configurar formateador visual para el ComboBox de Cliente (Mostrar ID y Nombre)
        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                if (cliente == null) {
                    return "";
                }
                return cliente.getId() + " - " + cliente.getNombres() + " " + cliente.getApellidos();
            }

            @Override
            public Cliente fromString(String string) {
                return null; // No requerido para selección
            }
        });
    }

    /**
     * Inicializa las opciones predefinidas para Servicio y Estado.
     */
    private void inicializarCombosEstaticos() {
        cmbTipoServicio.setItems(FXCollections.observableArrayList(
                "Mantenimiento Preventivo",
                "Reparación de Hardware",
                "Instalación de Software",
                "Diagnóstico de Red",
                "Soporte Técnico General"
        ));

        cmbEstado.setItems(FXCollections.observableArrayList(
                "Pendiente",
                "En proceso",
                "Finalizada",
                "Cancelada"
        ));
    }

    // Métodos de eventos FXML (Pendientes de implementación de lógica)
    @FXML public void guardarCita() {}
    @FXML public void modificarCita() {}
    @FXML public void eliminarCita() {}
    @FXML public void limpiarFormulario() {}
    @FXML public void buscarCita() {}
    @FXML public void limpiarBusqueda() {}
    @FXML public void seleccionarCitaTabla() {}
}
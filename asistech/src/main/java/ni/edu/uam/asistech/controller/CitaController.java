package ni.edu.uam.asistech.controller;

import ni.edu.uam.asistech.dao.CitaTecnicaDAO;
import ni.edu.uam.asistech.dao.ClienteDAO;
import ni.edu.uam.asistech.model.CitaTecnica;
import ni.edu.uam.asistech.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Optional;
/**
 * Controlador para la gestión de citas técnicas con refresco dinámico.
 * Proyecto: Sistema de Gestión de Citas de Atención Técnica
 * Integrantes: Steven Flores / Nora Obregón
 */
public class CitaController implements Initializable {

    private final CitaTecnicaDAO citaDAO = new CitaTecnicaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private ObservableList<CitaTecnica> listaCitas;
    private FilteredList<CitaTecnica> listaFiltrada;

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
    @FXML private TableColumn<CitaTecnica, LocalDate> colFecha;
    @FXML private TableColumn<CitaTecnica, LocalTime> colHora;
    @FXML private TableColumn<CitaTecnica, String> colTipoServicio;
    @FXML private TableColumn<CitaTecnica, String> colEstado;

    @FXML private DatePicker dpFechaFiltro;
    @FXML private ComboBox<String> cmbEstadoFiltro;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        cargarClientesEnComboBox();
        inicializarCombosEstaticos();
        cargarCitas();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<>("tipoServicio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    public void cargarCitas() {
        List<CitaTecnica> citasBD = citaDAO.listar();
        listaCitas = FXCollections.observableArrayList(citasBD);
        listaFiltrada = new FilteredList<>(listaCitas, p -> true);
        tblCitas.setItems(listaFiltrada);
    }

    private void cargarClientesEnComboBox() {
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        cmbCliente.setItems(FXCollections.observableArrayList(clientes));
        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                return (cliente == null) ? "" : cliente.getId() + " - " + cliente.getNombres() + " " + cliente.getApellidos();
            }

            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });
    }

    private void inicializarCombosEstaticos() {
        cmbTipoServicio.setItems(FXCollections.observableArrayList(
                "Mantenimiento Preventivo", "Reparación de Hardware", "Instalación de Software", "Diagnóstico de Red", "Soporte Técnico General"
        ));
        cmbEstado.setItems(FXCollections.observableArrayList(
                "Pendiente", "En proceso", "Finalizada", "Cancelada"
        ));
    }

    @FXML
    public void guardarCita() {
        if (!validarCampos()) return;

        try {
            CitaTecnica cita = new CitaTecnica(
                    cmbCliente.getValue().getId(),
                    dpFecha.getValue(),
                    LocalTime.parse(txtHora.getText().trim()),
                    cmbTipoServicio.getValue(),
                    taDescripcion.getText().trim(),
                    cmbEstado.getValue()
            );

            if (citaDAO.insertar(cita)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cita programada correctamente.");
                cargarCitas();
                limpiarFormulario();
            }
        } catch (DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato Incorrecto", "Ingrese la hora en formato HH:mm (Ej. 09:30).");
        }
    }

    @FXML
    public void modificarCita() {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione una cita de la tabla.");
            return;
        }

        if (!validarCampos()) return;

        try {
            CitaTecnica cita = new CitaTecnica(
                    Integer.parseInt(txtId.getText()),
                    cmbCliente.getValue().getId(),
                    cmbCliente.getValue().getNombreCompleto(),
                    dpFecha.getValue(),
                    LocalTime.parse(txtHora.getText().trim()),
                    cmbTipoServicio.getValue(),
                    taDescripcion.getText().trim(),
                    cmbEstado.getValue()
            );

            if (citaDAO.actualizar(cita)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cita actualizada correctamente.");
                cargarCitas();
                limpiarFormulario();
            }
        } catch (DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato Incorrecto", "Ingrese la hora en formato HH:mm (Ej. 09:30).");
        }
    }

    @FXML
    public void eliminarCita() {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione una cita técnica de la tabla para eliminar.");
            return;
        }

        int idCita = Integer.parseInt(txtId.getText());
        String clienteNombre = (cmbCliente.getValue() != null) ? cmbCliente.getValue().getNombreCompleto() : "Cliente seleccionado";

        // 1. Alerta de Confirmación (RF08)
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cancelación de Cita");
        confirmacion.setHeaderText("¿Desea eliminar la cita técnica registrada?");
        confirmacion.setContentText("Cita ID: " + idCita + "\nCliente: " + clienteNombre + "\nFecha: " + dpFecha.getValue());

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        // 2. Ejecutar eliminación solo si se confirma la acción
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (citaDAO.eliminar(idCita)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La cita técnica fue eliminada correctamente.");
                cargarCitas();
                limpiarFormulario();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error SQL", "No se pudo eliminar la cita técnica de la base de datos.");
            }
        }
    }

    @FXML
    public void seleccionarCitaTabla() {
        CitaTecnica cita = tblCitas.getSelectionModel().getSelectedItem();
        if (cita != null) {
            txtId.setText(String.valueOf(cita.getId()));
            dpFecha.setValue(cita.getFecha());
            txtHora.setText(cita.getHora().toString());
            cmbTipoServicio.setValue(cita.getTipoServicio());
            cmbEstado.setValue(cita.getEstado());
            taDescripcion.setText(cita.getDescripcion());

            cmbCliente.getItems().stream()
                    .filter(c -> c.getId() == cita.getClienteId())
                    .findFirst()
                    .ifPresent(c -> cmbCliente.setValue(c));
        }
    }

    @FXML
    public void buscarCita() {
        String filtro = txtBuscar.getText().toLowerCase().trim();
        listaFiltrada.setPredicate(cita -> {
            if (filtro.isEmpty()) return true;
            return cita.getNombreCliente().toLowerCase().contains(filtro) ||
                    cita.getTipoServicio().toLowerCase().contains(filtro) ||
                    cita.getEstado().toLowerCase().contains(filtro);
        });
    }

    @FXML
    public void limpiarBusqueda() {
        txtBuscar.clear();
        if (listaFiltrada != null) {
            listaFiltrada.setPredicate(p -> true);
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtId.clear();
        cmbCliente.setValue(null);
        dpFecha.setValue(null);
        txtHora.clear();
        cmbTipoServicio.setValue(null);
        cmbEstado.setValue(null);
        taDescripcion.clear();
        tblCitas.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        if (cmbCliente.getValue() == null || dpFecha.getValue() == null ||
                txtHora.getText().trim().isEmpty() || cmbTipoServicio.getValue() == null ||
                cmbEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Complete todos los campos obligatorios.");
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

    @FXML
    public void aplicarFiltrosCombinados() {
        String textoBusqueda = (txtBuscar != null && txtBuscar.getText() != null)
                ? txtBuscar.getText().toLowerCase().trim() : "";
        LocalDate fechaSeleccionada = (dpFechaFiltro != null) ? dpFechaFiltro.getValue() : null;
        String estadoSeleccionado = (cmbEstadoFiltro != null) ? cmbEstadoFiltro.getValue() : null;

        listaFiltrada.setPredicate(cita -> {
            // 1. Evaluación por búsqueda de texto (Cliente, Servicio o Estado)
            boolean coincideTexto = textoBusqueda.isEmpty() ||
                    (cita.getNombreCliente() != null && cita.getNombreCliente().toLowerCase().contains(textoBusqueda)) ||
                    (cita.getTipoServicio() != null && cita.getTipoServicio().toLowerCase().contains(textoBusqueda)) ||
                    (cita.getEstado() != null && cita.getEstado().toLowerCase().contains(textoBusqueda));

            // 2. Evaluación por fecha exacta
            boolean coincideFecha = (fechaSeleccionada == null) ||
                    (cita.getFecha() != null && cita.getFecha().equals(fechaSeleccionada));

            // 3. Evaluación por estado seleccionado
            boolean coincideEstado = (estadoSeleccionado == null || estadoSeleccionado.trim().isEmpty() || estadoSeleccionado.equals("Todos")) ||
                    (cita.getEstado() != null && cita.getEstado().equalsIgnoreCase(estadoSeleccionado));

            // La cita debe cumplir con los 3 criterios al mismo tiempo
            return coincideTexto && coincideFecha && coincideEstado;
        });
    }

    /**
     * Restablece todos los controles de filtro (texto, fecha y estado)
     * y recarga la lista completa de citas en la TableView.
     */
    @FXML
    public void limpiarFiltros() {
        if (txtBuscar != null) txtBuscar.clear();
        if (dpFechaFiltro != null) dpFechaFiltro.setValue(null);
        if (cmbEstadoFiltro != null) cmbEstadoFiltro.setValue("Todos");

        // Restaurar el predicado para mostrar todos los registros
        if (listaFiltrada != null) {
            listaFiltrada.setPredicate(cita -> true);
        }
    }
}

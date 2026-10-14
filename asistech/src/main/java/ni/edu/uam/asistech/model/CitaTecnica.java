package ni.edu.uam.asistech.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase de modelo que representa la entidad 'cita_tecnica' de la base de datos.
 * Incluye el campo auxiliar 'nombreCliente' para cumplir con el requerimiento RF04
 * (mostrar el nombre completo del cliente en la TableView de citas).
 *

 */
public class CitaTecnica {

    private int id;
    private int clienteId;
    private String nombreCliente;
    private LocalDate fecha;
    private LocalTime hora;
    private String tipoServicio;
    private String descripcion;
    private String estado; // Estados: Pendiente, En proceso, Finalizada, Cancelada


    public CitaTecnica() {}


    public CitaTecnica(int clienteId, LocalDate fecha, LocalTime hora, String tipoServicio, String descripcion, String estado) {
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoServicio = tipoServicio;
        this.descripcion = descripcion;
        this.estado = estado;
    }


    public CitaTecnica(int id, int clienteId, String nombreCliente, LocalDate fecha, LocalTime hora, String tipoServicio, String descripcion, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoServicio = tipoServicio;
        this.descripcion = descripcion;
        this.estado = estado;
    }

  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
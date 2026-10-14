package ni.edu.uam.asistech.dao;

import ni.edu.uam.asistech.config.ConexionBD;
import ni.edu.uam.asistech.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    /**
     * Consulta y recupera todos los registros de la tabla "cliente" ordenados por ID.
     */
    public List<Cliente> obtenerTodos() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT id, nombres, apellidos, telefono, correo, activo FROM cliente ORDER BY id ASC";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                boolean activo = rs.getBoolean("activo");

                Cliente cliente = new Cliente(id, nombres, apellidos, telefono, correo, activo);
                listaClientes.add(cliente);
            }

            System.out.println("Consulta exitosa: Se recuperaron " + listaClientes.size() + " clientes.");

        } catch (SQLException e) {
            System.err.println("Error SQL al consultar obtenerTodos(): " + e.getMessage());
            e.printStackTrace();
        }

        return listaClientes;
    }

    /**
     * Inserta un nuevo registro de cliente en la base de datos PostgreSQL.
     */
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombres, apellidos, telefono, correo, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombres());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getCorreo());
            pstmt.setBoolean(5, cliente.isActivo());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cliente agregado correctamente: " + cliente.getNombreCompleto());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al agregar cliente: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Actualiza la información de un cliente existente según su ID.
     */
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nombres = ?, apellidos = ?, telefono = ?, correo = ?, activo = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombres());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getCorreo());
            pstmt.setBoolean(5, cliente.isActivo());
            pstmt.setInt(6, cliente.getId());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cliente actualizado correctamente ID: " + cliente.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina un cliente de la base de datos según su ID.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cliente eliminado correctamente ID: " + id);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    public boolean existeCorreo(String correo, Integer idExcluir) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM cliente WHERE LOWER(correo) = LOWER(?)");
        if (idExcluir != null) {
            sql.append(" AND id <> ?");
        }

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            pstmt.setString(1, correo.trim());
            if (idExcluir != null) {
                pstmt.setInt(2, idExcluir);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al comprobar existencia de correo: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
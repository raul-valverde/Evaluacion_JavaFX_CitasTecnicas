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
     * Utiliza PreparedStatement y ResultSet en una estructura try-with-resources.
     */
    public List<Cliente> obtenerTodos() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT id, nombres, apellidos, telefono, correo, activo FROM cliente ORDER BY id ASC";

        // Try-with-resources: Cierra automaticamente Connection, PreparedStatement y ResultSet
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Recorrido de los resultados obtenidos desde PostgreSQL
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                boolean activo = rs.getBoolean("activo");

                // Creacion del objeto Cliente y se agrega a la lista
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
     * Utiliza PreparedStatement parametrizado para prevenir inyección SQL.
     *
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
                System.out.println(" Cliente agregado correctamente: " + cliente.getNombreCompleto());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al agregar cliente: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
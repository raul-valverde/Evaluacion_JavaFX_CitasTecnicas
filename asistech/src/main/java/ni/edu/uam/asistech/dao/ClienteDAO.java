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
     *
     
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

            System.out.println(" Consulta exitosa: Se recuperaron " + listaClientes.size() + " clientes.");

        } catch (SQLException e) {
            System.err.println("Error SQL al consultar obtenerTodos(): " + e.getMessage());
            e.printStackTrace();
        }

        return listaClientes;
    }
}

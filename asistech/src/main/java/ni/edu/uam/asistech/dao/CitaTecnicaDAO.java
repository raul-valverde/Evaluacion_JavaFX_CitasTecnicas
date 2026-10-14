package ni.edu.uam.asistech.dao;

import ni.edu.uam.asistech.config.ConexionBD;
import ni.edu.uam.asistech.model.CitaTecnica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de citas técnicas en PostgreSQL.
 */
public class CitaTecnicaDAO {

    /**
     * Consulta todas las citas realizando un JOIN con la tabla cliente.
     */
    public List<CitaTecnica> listar() {
        List<CitaTecnica> listaCitas = new ArrayList<>();
        String sql = "SELECT c.id, c.cliente_id, CONCAT(cl.nombres, ' ', cl.apellidos) AS nombre_cliente, " +
                "c.fecha, c.hora, c.tipo_servicio, c.descripcion, c.estado " +
                "FROM cita_tecnica c " +
                "JOIN cliente cl ON c.cliente_id = cl.id " +
                "ORDER BY c.fecha DESC, c.hora DESC";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CitaTecnica cita = new CitaTecnica(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getString("nombre_cliente"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime(),
                        rs.getString("tipo_servicio"),
                        rs.getString("descripcion"),
                        rs.getString("estado")
                );
                listaCitas.add(cita);
            }

            System.out.println("Citas recuperadas correctamente: " + listaCitas.size());

        } catch (SQLException e) {
            System.err.println("Error SQL al listar citas: " + e.getMessage());
            e.printStackTrace();
        }

        return listaCitas;
    }

    /**
     * Inserta una nueva cita técnica en la base de datos PostgreSQL.
     */
    public boolean insertar(CitaTecnica cita) {
        String sql = "INSERT INTO cita_tecnica (cliente_id, fecha, hora, tipo_servicio, descripcion, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cita.getClienteId());
            pstmt.setDate(2, Date.valueOf(cita.getFecha()));
            pstmt.setTime(3, Time.valueOf(cita.getHora()));
            pstmt.setString(4, cita.getTipoServicio());
            pstmt.setString(5, cita.getDescripcion());
            pstmt.setString(6, cita.getEstado());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cita técnica registrada con éxito.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al insertar cita técnica: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Actualiza la información de una cita técnica según su ID.
     */
    public boolean actualizar(CitaTecnica cita) {
        String sql = "UPDATE cita_tecnica SET cliente_id = ?, fecha = ?, hora = ?, tipo_servicio = ?, descripcion = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cita.getClienteId());
            pstmt.setDate(2, Date.valueOf(cita.getFecha()));
            pstmt.setTime(3, Time.valueOf(cita.getHora()));
            pstmt.setString(4, cita.getTipoServicio());
            pstmt.setString(5, cita.getDescripcion());
            pstmt.setString(6, cita.getEstado());
            pstmt.setInt(7, cita.getId());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cita técnica actualizada con éxito ID: " + cita.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar cita técnica: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina una cita técnica según su ID.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cita_tecnica WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cita técnica eliminada con éxito ID: " + id);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar cita técnica: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
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
 * Realiza un JOIN con la tabla cliente para obtener el nombre completo .
 *
 */
public class CitaTecnicaDAO {

    /**
     * Consulta todas las citas realizando un JOIN con la tabla cliente
     * para obtener el nombre completo en una sola consulta SQL.

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

            System.out.println(" Citas recuperadas correctamente: " + listaCitas.size());

        } catch (SQLException e) {
            System.err.println(" Error SQL al listar citas: " + e.getMessage());
            e.printStackTrace();
        }

        return listaCitas;
    }

    /**
     * Inserta una nueva cita técnica en la base de datos PostgreSQL.
     * Utiliza PreparedStatement con parámetros para garantizar la seguridad de los datos.
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
                System.out.println(" Cita técnica registrada con éxito.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println(" Error SQL al insertar cita técnica: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}

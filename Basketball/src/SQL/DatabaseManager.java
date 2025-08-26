package SQL;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import logico.Equipo;
import logico.Juego;
import logico.Jugador;
import logico.Lesion;
import logico.User;

public class DatabaseManager {

    public static String tipoUserConectado = null;

    // ======================= CONEXIÓN SEGURA =======================
    private static Connection getConnection() throws SQLException {
        Connection conn = Conexion.getConexion();
        if (conn == null || conn.isClosed()) {
            throw new SQLException("No se pudo establecer la conexión con la base de datos.");
        }
        return conn;
    }

    // ======================= CRUD de USUARIO =======================
    public static boolean validarInicioSesion(String usuario, String contrasena) {
        String consulta = "SELECT Tipo FROM Usuario WHERE Nombre_usuario = ? AND Password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tipoUserConectado = rs.getString("Tipo");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static boolean validarNombreUsuario(String usuario) {
        String consulta = "SELECT 1 FROM Usuario WHERE Nombre_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registrarUsuario(String usuario, String contrasena, String tipo) {
        String consulta = "INSERT INTO Usuario (Nombre_usuario, Password, Tipo) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ps.setString(3, tipo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static ArrayList<User> listarUsuarios() {
        ArrayList<User> lista = new ArrayList<>();
        String consulta = "SELECT Nombre_usuario, Tipo FROM Usuario";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User(rs.getString("Tipo"), rs.getString("Nombre_usuario"));
                lista.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al listar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public static boolean eliminarUsuario(String nombreUsuario) {
        String consulta = "DELETE FROM Usuario WHERE Nombre_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, nombreUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean actualizarUsuario(String nombreAntiguo, String nombre, String contrasena, String tipo) {
        String consulta = "UPDATE Usuario SET Nombre_usuario = ?, Password = ?, Tipo = ? WHERE Nombre_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, nombre);
            ps.setString(2, contrasena);
            ps.setString(3, tipo);
            ps.setString(4, nombreAntiguo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static User buscarUsuario(String nombreUsuario) {
        String consulta = "SELECT Nombre_usuario, Password, Tipo FROM Usuario WHERE Nombre_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("Tipo"),
                        rs.getString("Nombre_usuario"),
                        rs.getString("Password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // ======================= CRUD de EQUIPO =======================
    public static String obtenerProximoIdEquipo() {
        return obtenerProximoId("Equipo", "EQ");
    }

    public static boolean registrarEquipo(String nombre, int anio, String pais, String entrenador,
                                          String propietario, File archivoImagen) {
        String consulta = "INSERT INTO Equipo (Nombre, Anio_fundacion, Pais, Entrenador, Propetario, Imagen_Logo) VALUES (?, ?, ?, ?, ?, ?)";
        byte[] imagenBytes = convertirImagenABytes(archivoImagen);
        if (archivoImagen != null && imagenBytes == null) return false;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, nombre);
            ps.setInt(2, anio);
            ps.setString(3, pais);
            ps.setString(4, entrenador);
            ps.setString(5, propietario);
            ps.setBytes(6, imagenBytes);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean eliminarEquipo(String idEquipo) {
        try {
            desvincularJugadoresEquipoElim(idEquipo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "DELETE FROM Equipo WHERE ID_Equipo = ?";
        return ejecutarActualizacion(sql, idEquipo);
    }

    public static void desvincularJugadoresEquipoElim(String idEquipo) {
        String sql = "UPDATE Jugador SET ID_Equipo = NULL WHERE ID_Equipo = ?";
        ejecutarActualizacion(sql, idEquipo);
    }

    public static ArrayList<Equipo> listarEquipo() {
        ArrayList<Equipo> lista = new ArrayList<>();
        String consulta = "SELECT ID_Equipo, Nombre, Anio_fundacion, Pais, Entrenador, Propetario, Imagen_Logo FROM Equipo ORDER BY ID_Equipo_Num";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BufferedImage img = null;
                InputStream is = rs.getBinaryStream("Imagen_Logo");
                if (is != null) img = ImageIO.read(is);

                Equipo eq = new Equipo(
                    rs.getString("ID_Equipo"),
                    rs.getString("Nombre"),
                    rs.getInt("Anio_fundacion"),
                    rs.getString("Pais"),
                    rs.getString("Entrenador"),
                    rs.getString("Propetario"),
                    img
                );
                lista.add(eq);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar equipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public static Equipo obtenerEquipoPorId(String id) {
        String sql = "SELECT Nombre, Anio_fundacion, Pais, Entrenador, Propetario, Imagen_Logo FROM Equipo WHERE ID_Equipo = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BufferedImage img = null;
                    InputStream is = rs.getBinaryStream("Imagen_Logo");
                    if (is != null) img = ImageIO.read(is);

                    return new Equipo(
                        id,
                        rs.getString("Nombre"),
                        rs.getInt("Anio_fundacion"),
                        rs.getString("Pais"),
                        rs.getString("Entrenador"),
                        rs.getString("Propetario"),
                        img
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // ======================= CRUD de JUGADOR =======================
    public static String obtenerProximoIdJugador() {
        return obtenerProximoId("Jugador", "PL");
    }

    public static boolean registrarJugador(String idEquipo, String nombre, String apellido, String posicion,
                                          float peso, float altura, int numero, File archivoImagen) {
        String consulta = "INSERT INTO Jugador (ID_Equipo, Nombre, Apellido, Posicion, Peso, Altura, Numero, Imagen_Jugador) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        byte[] imagenBytes = convertirImagenABytes(archivoImagen);
        if (archivoImagen != null && imagenBytes == null) return false;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setString(1, idEquipo);
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, posicion);
            ps.setFloat(5, peso);
            ps.setFloat(6, altura);
            ps.setInt(7, numero);
            ps.setBytes(8, imagenBytes);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar jugador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean eliminarJugador(String idJugador) {
        String sql = "DELETE FROM Jugador WHERE ID_Jugador = ?";
        return ejecutarActualizacion(sql, idJugador);
    }

    public static ArrayList<Jugador> listarJugadores() {
        return listarJugadores("SELECT ID_Jugador, ID_Equipo, Nombre, Apellido, Posicion, Peso, Altura, Numero, Imagen_Jugador FROM Jugador ORDER BY ID_Jugador_Num");
    }

    public static ArrayList<Jugador> listarJugadoresDeEquipo(String idEquipo) {
        String sql = "SELECT ID_Jugador, ID_Equipo, Nombre, Apellido, Posicion, Peso, Altura, Numero, Imagen_Jugador FROM Jugador WHERE ID_Equipo = ? ORDER BY ID_Jugador_Num";
        return listarJugadores(sql, idEquipo);
    }

    private static ArrayList<Jugador> listarJugadores(String sql, String... params) {
        ArrayList<Jugador> lista = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BufferedImage img = null;
                    InputStream is = rs.getBinaryStream("Imagen_Jugador");
                    if (is != null) img = ImageIO.read(is);

                    Jugador j = new Jugador(
                        rs.getString("ID_Jugador"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("Posicion"),
                        rs.getFloat("Peso"),
                        rs.getFloat("Altura"),
                        rs.getInt("Numero"),
                        img,
                        rs.getString("ID_Equipo")
                    );
                    lista.add(j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar jugadores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public static Jugador obtenerJugadorPorId(String id) {
        String sql = "SELECT ID_Jugador, ID_Equipo, Nombre, Apellido, Posicion, Peso, Altura, Numero, Imagen_Jugador FROM Jugador WHERE ID_Jugador = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BufferedImage img = null;
                    InputStream is = rs.getBinaryStream("Imagen_Jugador");
                    if (is != null) img = ImageIO.read(is);

                    return new Jugador(
                        rs.getString("ID_Jugador"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("Posicion"),
                        rs.getFloat("Peso"),
                        rs.getFloat("Altura"),
                        rs.getInt("Numero"),
                        img,
                        rs.getString("ID_Equipo")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar jugador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // ======================= CRUD de JUEGO =======================
    public static String obtenerProximoIdJuego() {
        String consulta = "SELECT MAX(Juego_Num) FROM Juego";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {

            if (rs.next()) {
                int maxNum = rs.getInt(1);
                if (rs.wasNull()) maxNum = 0;
                return "JG-" + (maxNum + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "JG-1";
    }

    public static boolean registrarJuego(String id, String idEquipoCasa, String idEquipoVisita, String ganador) {
        String sql = "INSERT INTO Juego (ID_Juego, ID_Equipo_Casa, ID_Equipo_Visita, Ganador) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, idEquipoCasa);
            ps.setString(3, idEquipoVisita);
            ps.setString(4, ganador);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static ArrayList<Juego> listarJuegos() {
        ArrayList<Juego> lista = new ArrayList<>();
        String sql = "SELECT ID_Juego, ID_Equipo_Casa, ID_Equipo_Visita, Ganador FROM Juego ORDER BY Juego_Num";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID_Juego");
                String idCasa = rs.getString("ID_Equipo_Casa");
                String idVisita = rs.getString("ID_Equipo_Visita");
                String ganador = rs.getString("Ganador");

                Equipo casa = obtenerEquipoPorId(idCasa);
                Equipo visita = obtenerEquipoPorId(idVisita);

                if (casa == null || visita == null) continue;

                Juego juego = new Juego(id, casa, visita);
                if (ganador != null) {
                    juego.setGanador(ganador);
                }
                lista.add(juego);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al listar juegos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public static Juego obtenerJuegoPorId(String idJuego) {
        String sql = "SELECT ID_Equipo_Casa, ID_Equipo_Visita, Ganador FROM Juego WHERE ID_Juego = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idJuego);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String idCasa = rs.getString("ID_Equipo_Casa");
                    String idVisita = rs.getString("ID_Equipo_Visita");
                    String ganador = rs.getString("Ganador");

                    Equipo casa = obtenerEquipoPorId(idCasa);
                    Equipo visita = obtenerEquipoPorId(idVisita);

                    if (casa == null || visita == null) return null;

                    Juego juego = new Juego(idJuego, casa, visita);
                    if (ganador != null) {
                        juego.setGanador(ganador);
                    }
                    return juego;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static boolean actualizarJuego(String idJuego, String ganador) {
        String sql = "UPDATE Juego SET Ganador = ? WHERE ID_Juego = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ganador);
            ps.setString(2, idJuego);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean eliminarJuego(String idJuego) {
        String sql = "DELETE FROM Juego WHERE ID_Juego = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idJuego);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static ArrayList<Juego> listarJuegosPorEquipo(String idEquipo) {
        ArrayList<Juego> lista = new ArrayList<>();
        String sql = """
            SELECT ID_Juego, ID_Equipo_Casa, ID_Equipo_Visita, Ganador 
            FROM Juego 
            WHERE ID_Equipo_Casa = ? OR ID_Equipo_Visita = ? 
            ORDER BY Juego_Num
            """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idEquipo);
            ps.setString(2, idEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("ID_Juego");
                    String idCasa = rs.getString("ID_Equipo_Casa");
                    String idVisita = rs.getString("ID_Equipo_Visita");
                    String ganador = rs.getString("Ganador");

                    Equipo casa = obtenerEquipoPorId(idCasa);
                    Equipo visita = obtenerEquipoPorId(idVisita);

                    if (casa == null || visita == null) continue;

                    Juego juego = new Juego(id, casa, visita);
                    if (ganador != null) {
                        juego.setGanador(ganador);
                    }
                    lista.add(juego);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al listar juegos del equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    // ======================= CRUD de LESIÓN =======================
    public static String obtenerProximoIdLesion() {
        String consulta = "SELECT MAX(Lesion_Num) FROM Lesion";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {

            if (rs.next()) {
                int maxNum = rs.getInt(1);
                if (rs.wasNull()) maxNum = 0;
                return "LE-" + (maxNum + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "LE-1";
    }

    public static boolean registrarLesion(String id, String idJugador, LocalDate fechaLesion,
                                          String tipoLesion, LocalDate fechaRecuperacion,
                                          String descripcionCorta, boolean estado) {
        String sql = "INSERT INTO Lesion (ID_Lesion, ID_Jugador, Fecha_Lesion, Tipo_De_Lesion, " +
                     "Fecha_Recuperacion, Descripcion_Corta, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, idJugador);
            ps.setDate(3, java.sql.Date.valueOf(fechaLesion));
            ps.setString(4, tipoLesion);
            ps.setDate(5, java.sql.Date.valueOf(fechaRecuperacion));
            ps.setString(6, descripcionCorta);
            ps.setBoolean(7, estado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar lesión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean actualizarLesion(String id, String tipoLesion, LocalDate fechaRecuperacion,
                                           String descripcionCorta, boolean estado) {
        String sql = "UPDATE Lesion SET Tipo_De_Lesion = ?, Fecha_Recuperacion = ?, " +
                     "Descripcion_Corta = ?, Estado = ? WHERE ID_Lesion = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipoLesion);
            ps.setDate(2, java.sql.Date.valueOf(fechaRecuperacion));
            ps.setString(3, descripcionCorta);
            ps.setBoolean(4, estado);
            ps.setString(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar lesión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static Lesion obtenerLesionPorId(String id) {
        String sql = "SELECT ID_Jugador, Fecha_Lesion, Tipo_De_Lesion, Fecha_Recuperacion, " +
                     "Descripcion_Corta, Estado FROM Lesion WHERE ID_Lesion = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String idJugador = rs.getString("ID_Jugador");
                    Jugador jugador = obtenerJugadorPorId(idJugador);
                    if (jugador == null) return null;

                    return new Lesion(
                        id,
                        jugador,
                        rs.getDate("Fecha_Lesion").toLocalDate(),
                        rs.getString("Tipo_De_Lesion"),
                        rs.getDate("Fecha_Recuperacion").toLocalDate(),
                        rs.getString("Descripcion_Corta"),
                        rs.getBoolean("Estado")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener lesión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static ArrayList<Lesion> listarTodasLesiones() {
        ArrayList<Lesion> lista = new ArrayList<>();
        String sql = "SELECT ID_Lesion, ID_Jugador, Fecha_Lesion, Tipo_De_Lesion, " +
                     "Fecha_Recuperacion, Descripcion_Corta, Estado FROM Lesion ORDER BY ID_Lesion";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID_Lesion");
                String idJugador = rs.getString("ID_Jugador");
                Jugador jugador = obtenerJugadorPorId(idJugador);
                if (jugador == null) continue;

                Lesion les = new Lesion(
                    id,
                    jugador,
                    rs.getDate("Fecha_Lesion").toLocalDate(),
                    rs.getString("Tipo_De_Lesion"),
                    rs.getDate("Fecha_Recuperacion").toLocalDate(),
                    rs.getString("Descripcion_Corta"),
                    rs.getBoolean("Estado")
                );
                lista.add(les);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al listar lesiones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public static ArrayList<Lesion> listarLesionesPorJugador(String idJugador) {
        ArrayList<Lesion> lista = new ArrayList<>();
        String sql = "SELECT ID_Lesion, Fecha_Lesion, Tipo_De_Lesion, Fecha_Recuperacion, " +
                     "Descripcion_Corta, Estado FROM Lesion WHERE ID_Jugador = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idJugador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lesion les = new Lesion(
                        rs.getString("ID_Lesion"),
                        obtenerJugadorPorId(idJugador),
                        rs.getDate("Fecha_Lesion").toLocalDate(),
                        rs.getString("Tipo_De_Lesion"),
                        rs.getDate("Fecha_Recuperacion").toLocalDate(),
                        rs.getString("Descripcion_Corta"),
                        rs.getBoolean("Estado")
                    );
                    lista.add(les);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al listar lesiones del jugador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    // ======================= MÉTODOS AUXILIARES =======================
    private static String obtenerProximoId(String tabla, String prefijo) {
        String consulta = "SELECT MAX(" + tabla + "_Num) FROM " + tabla;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consulta)) {

            if (rs.next()) {
                int maxNum = rs.getInt(1);
                if (rs.wasNull()) maxNum = 0;
                return prefijo + "-" + (maxNum + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefijo + "-1";
    }

    private static byte[] convertirImagenABytes(File archivo) {
        if (archivo == null) return null;
        try {
            BufferedImage img = ImageIO.read(archivo);
            if (img == null) {
                JOptionPane.showMessageDialog(null, "El archivo no es una imagen válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al procesar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static boolean ejecutarActualizacion(String sql, String parametro) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parametro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error en operación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

	public static void cerrarConexion() {
		// TODO Auto-generated method stub
		
	}

	public static void actualizarJugador(Jugador j) {
		// TODO Auto-generated method stub
		
	}

	public static void actualizarEquipo(Equipo equipo2) {
		// TODO Auto-generated method stub
		
	}
}
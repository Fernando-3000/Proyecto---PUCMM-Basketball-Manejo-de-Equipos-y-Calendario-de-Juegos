package SQL;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import logico.Equipo;
import logico.User;

public class DatabaseManager {

	public static String tipoUserConectado = null;

	// Conexion a Servidor
	static Connection conexion = Conexion.getConexion();

	public static boolean validarInicioSesion(String usuario, String contrasena) {
		try {
			String consulta = "SELECT Nombre_usuario, Password, Tipo FROM Usuario WHERE Nombre_usuario = ? AND Password = ?";
			// Connection conexion = Conexion.getConexion();
			PreparedStatement prepareState = conexion.prepareStatement(consulta);
			prepareState.setString(1, usuario);
			prepareState.setString(2, contrasena);

			ResultSet resultado = prepareState.executeQuery();

			if (resultado.next()) {
				tipoUserConectado = resultado.getString("Tipo");
				return true;
			} else {
				return false;
			}

		} catch (SQLException exception) {
			exception.printStackTrace();
			return false;
		}
	}

	// Verifica si el nombre del usuario esta YA esta registrado
	public static boolean validarNombreUsuario(String usuario) {
		try {
			// Validando que el id del no este registrado
			String consultaValidarUser = "SELECT Nombre_usuario FROM Usuario WHERE Nombre_usuario = ?";
			PreparedStatement prepareState = conexion.prepareStatement(consultaValidarUser);
			prepareState.setString(1, usuario);

			ResultSet resultado = prepareState.executeQuery();
			boolean usuarioEncontrado = resultado.next(); // Si encontro el Usuario
			return usuarioEncontrado;

		} catch (SQLException exception) {
			exception.printStackTrace();
			return false;
		}
	}

	// Verifica si el nombre del usuario esta YA esta registrado
	public static boolean registrarUsuario(String usuario, String contrasena, String tipo_usuario) {
		try {
			// Insertar nuevo usuario registrado
			String consultaRegistrarUsuario = "INSERT INTO Usuario (Nombre_usuario, Password, Tipo) VALUES (?, ?, ?)";
			PreparedStatement prepareState = conexion.prepareStatement(consultaRegistrarUsuario);
			prepareState.setString(1, usuario);
			prepareState.setString(2, contrasena);
			prepareState.setString(3, tipo_usuario);

			int filasAfectadas = prepareState.executeUpdate();

			if (filasAfectadas > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException exception) {
			exception.printStackTrace();
			return false;
		}
	}

	// Devuelve un arreglo con todos los usuarios registrados (solo su tipo y
	// usuario)
	public static ArrayList<User> listarUsuarios() {

		ArrayList<User> listaUsuarios = new ArrayList<>();

		try {
			String consulta = "SELECT Nombre_usuario, Tipo FROM Usuario";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);

			ResultSet resultado = preparedStatement.executeQuery();

			while (resultado.next()) {
				String nombre_usuario = resultado.getString("Nombre_usuario");
				String tipo_usuario = resultado.getString("Tipo");

				User User = new User(tipo_usuario, nombre_usuario);
				listaUsuarios.add(User);
			}

			return listaUsuarios;

		} catch (SQLException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
			return null;
		}

	}

	// Elimina un usuario de la base de datos
	public static boolean eliminarUsuario(String nombre_Usuario) {

		try {
			String consulta = "DELETE FROM Usuario WHERE Nombre_usuario = ?";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setString(1, nombre_Usuario);
			int filasAfectadas = preparedStatement.executeUpdate();

			if (filasAfectadas > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
			return false;
		}
	}

	// Busca y devuelve objeto Usuario de la base de datos
	public static User buscarUsuario(String nombre_Usuario) {
		try {
			String consulta = "SELECT Nombre_usuario, Tipo FROM Usuario WHERE Nombre_usuario = ?";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setString(1, nombre_Usuario);
			ResultSet resultado = preparedStatement.executeQuery();

			if (resultado.next()) {
				String nombre_usuario = resultado.getString("Nombre_usuario");
				String tipo_usuario = resultado.getString("Tipo");
				return new User(tipo_usuario, nombre_usuario);
			} else {
				return null;
			}
		} catch (SQLException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
			return null;
		}
	}

	// Devuelve el ID_Equipo Para el siguiente equipo que se registrara
	public static String obtenerProximoIdEquipo() {
		String proximoId = "EQ-1"; // Valor por defecto
		String consulta = "SELECT 'EQ-' + CAST(ISNULL(IDENT_CURRENT('Equipo'), 0) + 1 AS VARCHAR) AS ProximoID";

		try {
			Statement statement = conexion.createStatement();
			ResultSet resultado = statement.executeQuery(consulta);

			if (resultado.next()) {
				proximoId = resultado.getString("ProximoID");
			}

		} catch (SQLException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
		}

		return proximoId;
	}

	// Registra equipo en la base de datos
	public static boolean registrarEquipo(String nombre, int anio_fundacion, String pais, String entrenador,
			String propetario, File archivoImagen) {

		String consultaRegistrarEquipo = "INSERT INTO Equipo (Nombre, Anio_fundacion, Pais, Entrenador, Propetario, Imagen_Logo) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = conexion.prepareStatement(consultaRegistrarEquipo)) {

			byte[] imagenBytes = null;
			if (archivoImagen != null) {
				// Leer el archivo de imagen como BufferedImage
				BufferedImage bufferedImage = ImageIO.read(archivoImagen);
				if (bufferedImage == null) {
					JOptionPane.showMessageDialog(null, "El archivo no es una imagen válida.");
					return false;
				}
				// Convertir BufferedImage a byte
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", baos); // Usa png como formato por defecto
				imagenBytes = baos.toByteArray();
			}


			preparedStatement.setString(1, nombre);
			preparedStatement.setInt(2, anio_fundacion);
			preparedStatement.setString(3, pais);
			preparedStatement.setString(4, entrenador);
			preparedStatement.setString(5, propetario);
			preparedStatement.setBytes(6, imagenBytes);

			int filasAfectadas = preparedStatement.executeUpdate();

			if (filasAfectadas > 0) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException | IOException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
			return false;
		}
	}

	// Devuelve un arreglo con todos los equipo de la base de datos
	public static ArrayList<Equipo> listarEquipo() {

		ArrayList<Equipo> listaEquipos = new ArrayList<>();

		try {
			String consulta = "SELECT ID_Equipo, Nombre, Anio_fundacion, Pais, Entrenador, Propetario, Imagen_Logo FROM Equipo";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);

			ResultSet resultado = preparedStatement.executeQuery();

			while (resultado.next()) {
				String id = resultado.getString("ID_Equipo");
				String nombre = resultado.getString("Nombre");
				int anioFundacion = resultado.getInt("Anio_fundacion");
				String pais = resultado.getString("Pais");
				String entrenador = resultado.getString("Entrenador");
				String propietario = resultado.getString("Propetario");
				InputStream is = resultado.getBinaryStream("Imagen_Logo");
				BufferedImage imagen = null;
				if (is != null) {
					imagen = ImageIO.read(is);
				}

				Equipo equipo = new Equipo(id, nombre, anioFundacion, pais, entrenador, propietario, imagen);
				listaEquipos.add(equipo);
			}

			return listaEquipos;

		} catch (SQLException | IOException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
			return null;
		}
	}

	// Busca en la base de datos y devuelve un equipo por medio de su ID
	public static Equipo obtenerEquipoPorId(String id) {
		String sql = "SELECT Nombre, Anio_fundacion, Pais, Entrenador, Propetario, Imagen_Logo FROM Equipo WHERE ID_Equipo = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String nombreEquipo = rs.getString("Nombre");
				int anio_fundacion = rs.getInt("Anio_fundacion");
				String pais = rs.getString("Pais");
				String entrenador = rs.getString("Entrenador");
				String propetario = rs.getString("Propetario");

				InputStream is = rs.getBinaryStream("Imagen_Logo");
				BufferedImage imagen = null;
				if (is != null) {
					imagen = ImageIO.read(is);
				}

				Equipo equipo = new Equipo(id, nombreEquipo, anio_fundacion, pais, entrenador, propetario, imagen);
				return equipo;
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Error al consultar equipo: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

}

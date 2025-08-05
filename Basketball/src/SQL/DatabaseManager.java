package SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DatabaseManager {
	
	//Conexion a Servidor
	static Connection conexion = Conexion.getConexion();
    
    public static boolean validarInicioSesion(String usuario, String contrasena) {
    	try {
	    	String consulta = "SELECT Nombre_usuario, Password FROM Usuario WHERE Nombre_usuario = ? AND Password = ?";
	    	//Connection conexion = Conexion.getConexion();
			PreparedStatement prepareState = conexion.prepareStatement(consulta);
			prepareState.setString(1, usuario);
			prepareState.setString(2, contrasena);
			
			ResultSet resultado = prepareState.executeQuery();
			return resultado.next();
		
    	} catch(SQLException exception) {
        	exception.printStackTrace();
        	return false;
		}	
    }
    
    //Verifica si el nombre del usuario esta YA esta registrado
    public static boolean validarNombreUsuario(String usuario) {
    	try {
    		//Validando que el id del no este registrado
			String consultaValidarUser = "SELECT Nombre_usuario FROM Usuario WHERE Nombre_usuario = ?";
			PreparedStatement prepareState = conexion.prepareStatement(consultaValidarUser);
			prepareState.setString(1, usuario);
			
			ResultSet resultado = prepareState.executeQuery();
    		boolean usuarioEncontrado = resultado.next(); // Si encontro el Usuario
			return usuarioEncontrado;
		
    	} catch(SQLException exception) {
        	exception.printStackTrace();
        	return false;
		}	
    }
    
  //Verifica si el nombre del usuario esta YA esta registrado
    public static boolean registrarUsuario(String usuario, String contrasena, String tipo_usuario) {
    	try {
    		//Insertar nuevo usuario registrado
		    String consultaRegistrarUsuario = "INSERT INTO Usuario (Nombre_usuario, Password, Tipo) VALUES (?, ?, ?)";
		    PreparedStatement prepareState = conexion.prepareStatement(consultaRegistrarUsuario);
		    prepareState.setString(1, usuario);
		    prepareState.setString(2, contrasena);
		    prepareState.setString(3, tipo_usuario);
												
			int filasAfectadas = prepareState.executeUpdate();
			
			if(filasAfectadas > 0) {
				return true;
			} else {
				return false;
			}
		
    	} catch(SQLException exception) {
        	exception.printStackTrace();
        	return false;
		}	
    }
    
    //Obtener Proximo ID EQUIPO
    public static String obtenerProximoIdEquipo() {
	    String proximoId = "EQ-1"; // Valor por defecto
	    String consulta = "SELECT 'EQ-' + CAST(ISNULL(IDENT_CURRENT('Equipo'), 0) + 1 AS VARCHAR) AS ProximoID";

	    try {
	    	Statement statement = conexion.createStatement();
	        ResultSet resultado = statement.executeQuery(consulta);
	        
	        if (resultado.next()) {
	            proximoId = resultado.getString("ProximoID");
	        }
	        
	    } catch(SQLException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
		}
	    
	    return proximoId;
	}
    
  //Obtener Proximo ID EQUIPO
    public static boolean registrarEquipo(String nombre, int anio_fundacion, String pais, String entrenador, String propetario) {
		try {
			String consultaRegistrarEquipo = "INSERT INTO Equipo (Nombre, Anio_fundacion, Pais, Entrenador, Propetario) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement;
			
			preparedStatement = conexion.prepareStatement(consultaRegistrarEquipo);
			preparedStatement.setString(1, nombre);
			preparedStatement.setInt(2, anio_fundacion);
			preparedStatement.setString(3, pais);
			preparedStatement.setString(4, entrenador);
			preparedStatement.setString(5, propetario);
			
			int filasAfectadas = preparedStatement.executeUpdate();
			
			if(filasAfectadas > 0) {
				return true;
			} else {
				return false;
			}
	        
	    } catch(SQLException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
			return false;
		}
	    
	}
    
    
    //Obtiene un equipo por su ID desde la base de datos
    public static Equipo obtenerEquipoPorId(String id) {
        String sql = "SELECT id, Nombre, Anio_fundacion, Pais, Entrenador, Propetario, foto_path FROM Equipo WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Equipo equipo = new Equipo();
                equipo.setId(rs.getString("id"));
                equipo.setNombre(rs.getString("Nombre"));
                equipo.setAnoFundacion(rs.getInt("Anio_fundacion"));
                equipo.setPais(rs.getString("Pais"));
                equipo.setEntrenador(rs.getString("Entrenador"));
                equipo.setDueno(rs.getString("Propetario")); // Nota: en tu BD es "Propetario"
                equipo.setFotoPath(rs.getString("foto_path"));
                return equipo;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    // Obtiene un jugador por su ID desde la base de datos
    public static Equipo obtenerEquipoPorId(String id) {
        String sql = "SELECT id, Nombre, Anio_fundacion, Pais, Entrenador, Propetario, foto_path FROM Equipo WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Equipo equipo = new Equipo();
                equipo.setId(rs.getString("id"));
                equipo.setNombre(rs.getString("Nombre"));
                equipo.setAnoFundacion(rs.getInt("Anio_fundacion"));
                equipo.setPais(rs.getString("Pais"));
                equipo.setEntrenador(rs.getString("Entrenador"));
                equipo.setDueno(rs.getString("Propetario")); // Nota: en tu BD es "Propetario"
                equipo.setFotoPath(rs.getString("foto_path"));
                return equipo;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
}

package SQL;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import logico.Equipo;

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
			PreparedStatement preparedStatement = conexion.prepareStatement(consultaRegistrarEquipo);
			
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
    
    //Devuelve un arreglo con todos los equipo listos para ser listados
    public static ArrayList<Equipo> registrarEquipo() {
    	
    	ArrayList<Equipo> listaEquipos = new ArrayList<>();
    	
		try {
			String consulta = "SELECT ID_Equipo, Nombre, Anio_fundacion, Pais, Entrenador, Propetario FROM Equipo";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			
			ResultSet resultado = preparedStatement.executeQuery();
			
			
			while (resultado.next()) {
	            String id = resultado.getString("ID_Equipo");
	            String nombre = resultado.getString("Nombre");
	            int anioFundacion = resultado.getInt("Anio_fundacion");
	            String pais = resultado.getString("Pais");
	            String entrenador = resultado.getString("Entrenador");
	            String propietario = resultado.getString("Propetario");

	     
	            Equipo equipo = new Equipo(id, nombre, anioFundacion, pais, entrenador, propietario, null);
	            listaEquipos.add(equipo);
	        }
			
			return listaEquipos;
	        
	    } catch(SQLException exception) {
			JOptionPane.showMessageDialog(null, exception.toString());
			return null;
		}
	    
	}
    
    
    
    
}

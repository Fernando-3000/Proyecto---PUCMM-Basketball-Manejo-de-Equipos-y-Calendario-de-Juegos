package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	
	public static Connection getConexion() {
		
		String conexionURL = "jdbc:sqlserver://100.90.107.110:1433;"
				+ "database=BasketballSystem;"
				+ "user=pruebaPOO;"
				+ "password=12345;"
				+ "loginTimeout=30;"
				+ "TrustServerCertificate=true;";
		
		try {
			Connection con = DriverManager.getConnection(conexionURL);
			return con;
			
		} catch(SQLException ex) {
			//Si no se pudo realizar conexion
			System.out.println(ex.toString()); 
			return null;
			
		}
	
	}
}

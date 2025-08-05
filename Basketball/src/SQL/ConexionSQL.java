package SQL;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class ConexionSQL extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConexionSQL frame = new ConexionSQL();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConexionSQL() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String datosEquipos = "";
				
				try {
					//Utilizamos Statement para hacer la conexion de la clase Conexion
					Statement sql = Conexion.getConexion().createStatement();
					//Hacemos la consulta SQL
					String consulta = "SELECT * FROM Equipo";
					//Usamos ResultSet para guardar los datos de la consulta en un arreglo
					ResultSet resultado = sql.executeQuery(consulta);
					
					while(resultado.next()) {
						datosEquipos += resultado.getString(2) + "\n";
					}
					
					JOptionPane.showMessageDialog(null, datosEquipos);
					
					
				} catch(SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.toString());
				}
			}
		});
		contentPane.add(btnConectar);

	}

}

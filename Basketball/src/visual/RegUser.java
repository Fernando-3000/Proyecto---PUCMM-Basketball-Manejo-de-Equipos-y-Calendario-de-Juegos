package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import SQL.Conexion;
import logico.User;
import logico.SerieNacional;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class RegUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtUser;
	private JTextField txtPass;
	private JTextField txtPassConfirm;
	private JComboBox comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegUser dialog = new RegUser();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegUser() {
		setTitle("Registrar usuario");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 358, 196);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNombreUsuario = new JLabel("Nombre Usuario:");
		lblNombreUsuario.setBounds(12, 13, 97, 14);
		contentPanel.add(lblNombreUsuario);
		
		txtUser = new JTextField();
		txtUser.setBounds(12, 30, 127, 20);
		contentPanel.add(txtUser);
		txtUser.setColumns(10);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Seleccionar", "Administrador", "Anotador"}));
		comboBox.setBounds(12, 80, 127, 20);
		contentPanel.add(comboBox);
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setBounds(12, 63, 97, 14);
		contentPanel.add(lblTipo);
		
		txtPass = new JTextField();
		txtPass.setBounds(182, 30, 147, 20);
		contentPanel.add(txtPass);
		txtPass.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(181, 13, 97, 14);
		contentPanel.add(lblPassword);
		
		JLabel lblConfirmarPassword = new JLabel("Confirmar Password:");
		lblConfirmarPassword.setBounds(181, 63, 167, 14);
		contentPanel.add(lblConfirmarPassword);
		
		txtPassConfirm = new JPasswordField();
		txtPassConfirm.setColumns(10);
		txtPassConfirm.setBounds(182, 80, 147, 20);
		contentPanel.add(txtPassConfirm);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Registrar");
				okButton.setFont(new Font("Tahoma", Font.BOLD, 13));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//User usuarioEncontrado = SerieNacional.getInstance().buscarUser(txtUser.getText());
						boolean usuarioEncontrado = false;
						Connection conexion = Conexion.getConexion(); //Conectando a la base de datos
						String usuario = txtUser.getText();
						String contrasena = txtPass.getText();
						String tipo_Usuario = comboBox.getSelectedItem().toString();
						
						
						try {
							
							//Validando que el id del no este registrado
							String consultaValidarUser = "SELECT Nombre_usuario FROM Usuario WHERE Nombre_usuario = ?";
							PreparedStatement sqlCon = conexion.prepareStatement(consultaValidarUser);
							sqlCon.setString(1, usuario);
							
							ResultSet resultado = sqlCon.executeQuery();
							usuarioEncontrado = resultado.next(); // Si encontro el Usuario
									
						
							if (!usuarioEncontrado)
							{
								if (datosCompletos())
								{
									/*
									User user = new User(comboBox.getSelectedItem().toString(),txtUser.getText(),txtPass.getText());
								    SerieNacional.getInstance().regUser(user);
								    OperacionExitosa operacion = new OperacionExitosa();
								    operacion.setVisible(true);
								    operacion.setModal(true);
								    dispose();
								    */
									//Insertar nuevo usuario registrado
								    String consultaRegistrarUsuario = "INSERT INTO Usuario (Nombre_usuario, Password, Tipo) VALUES (?, ?, ?)";
									sqlCon = conexion.prepareStatement(consultaRegistrarUsuario);
									sqlCon.setString(1, usuario);
									sqlCon.setString(2, contrasena);
									sqlCon.setString(3, tipo_Usuario);
																		
									int filasAfectadas = sqlCon.executeUpdate();
						            
						            if (filasAfectadas > 0) {
										new OperacionEspecifica("Se ha registrado con exito").setVisible(true);
						                dispose();
						            } else {
						                new OperacionEspecifica("No se pudo registrar el usuario");
						            }
								}
								else
								{
									OperacionEspecifica operacion = new OperacionEspecifica("Rellene todos los campos.");
								    operacion.setVisible(true);
								    operacion.setModal(true);
								}
							}
							else
							{
								OperacionEspecifica operacion = new OperacionEspecifica("Nombre de usuario en uso");
							    operacion.setVisible(true);
							    operacion.setModal(true);
							}
							
						} catch(SQLException ex) {
							JOptionPane.showMessageDialog(null, ex.toString());
						}
							
					}
				});
				okButton.setActionCommand("Registrar");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private boolean datosCompletos() {
	    return !txtUser.getText().trim().isEmpty()
	        && !txtPass.getText().trim().isEmpty()
	        && !txtPassConfirm.getText().trim().isEmpty()
	        && comboBox.getSelectedItem() != null
	        && !comboBox.getSelectedItem().toString().equals("Seleccione")
	        && txtPass.getText().equals(txtPassConfirm.getText());
	}
}



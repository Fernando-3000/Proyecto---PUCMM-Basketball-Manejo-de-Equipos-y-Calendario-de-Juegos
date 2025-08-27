package visual;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import SQL.Conexion;
import logico.User;
import logico.SerieNacional;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import SQL.DatabaseManager;

public class Login extends JDialog {

	private JPanel contentPane;
	private JTextField txtUser;
	private JPasswordField txtContra;
	private JButton btnLogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Login frame = new Login();
				frame.setVisible(true);
				frame.setModal(true);

			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 249, 217);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//Titulo
		setTitle("Inicio de Sesión");
		//Icono de ventana (pelota basketball)
		setIconImage(new javax.swing.ImageIcon("src/recursos/Basketball(LogoPrograma).png").getImage());

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(14, 13, 57, 14);
		panel.add(lblUsuario);

		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setBounds(14, 63, 105, 14);
		panel.add(lblContrasea);

		txtUser = new JTextField();
		txtUser.setBounds(14, 31, 191, 20);
		panel.add(txtUser);
		txtUser.setColumns(10);

		txtContra = new JPasswordField();
		txtContra.setBounds(14, 81, 191, 20);
		panel.add(txtContra);
		txtContra.setColumns(10);

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Conectando a la base de datos y validando el usuario en la misma
				char[] contrasenaChars = txtContra.getPassword();
				String usuario = txtUser.getText();
				String contrasena = new String(contrasenaChars);

				boolean validacion = DatabaseManager.validarInicioSesion(usuario, contrasena);

				if (validacion) {
					PrincipalVisual frame = new PrincipalVisual();
					dispose();
					frame.setVisible(true);
				} else {
					JDialog dialog = new JDialog();
					dialog.setAlwaysOnTop(true);
					JOptionPane.showMessageDialog(dialog, "Usuario Invalido", "Error", JOptionPane.ERROR_MESSAGE);
					dialog.dispose();
				}

			}
		});
		btnLogin.setBounds(14, 117, 191, 23);
		panel.add(btnLogin);
	}
}

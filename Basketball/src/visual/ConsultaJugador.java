package visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import SQL.DatabaseManager;
import logico.Equipo;
import logico.Jugador;

public class ConsultaJugador extends JDialog {
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPosicion;
    private JTextField txtNumero;
    private JTextField txtEquipo;

    public ConsultaJugador(String idJugador) {
        setModal(true);
        setTitle("Consultar Jugador");
        setBounds(100, 100, 704, 429);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Panel central
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel, BorderLayout.CENTER);

        int xLabel = 30, xField = 150, y = 40, dy = 40, fieldWidth = 200;

        // ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(xLabel, y, 100, 25);
        txtId = new JTextField();
        txtId.setBounds(xField, y, fieldWidth, 25);
        txtId.setEditable(false);
        panel.add(lblId); panel.add(txtId);
        y += dy;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(xLabel, y, 100, 25);
        txtNombre = new JTextField();
        txtNombre.setBounds(xField, y, fieldWidth, 25);
        txtNombre.setEditable(false);
        panel.add(lblNombre); panel.add(txtNombre);
        y += dy;

        // Posición
        JLabel lblPosicion = new JLabel("Posición:");
        lblPosicion.setBounds(xLabel, y, 100, 25);
        txtPosicion = new JTextField();
        txtPosicion.setBounds(xField, y, fieldWidth, 25);
        txtPosicion.setEditable(false);
        panel.add(lblPosicion); panel.add(txtPosicion);
        y += dy;

        // Número
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(xLabel, y, 100, 25);
        txtNumero = new JTextField();
        txtNumero.setBounds(xField, y, fieldWidth, 25);
        txtNumero.setEditable(false);
        panel.add(lblNumero); panel.add(txtNumero);
        y += dy;
    
        
        // Equipo
        JLabel lblEquipo = new JLabel("Equipo ID:");
        lblEquipo.setBounds(xLabel, y, 100, 25);
        txtEquipo = new JTextField();
        txtEquipo.setBounds(xField, y, fieldWidth, 25);
        txtEquipo.setEditable(false);
        panel.add(lblEquipo); panel.add(txtEquipo);

        // Botón Cerrar
        JPanel buttonPane = new JPanel();
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // 🔹 Cargar jugador desde la base de datos
        if (idJugador != null && !idJugador.trim().isEmpty()) {
            cargarJugadorDesdeBD(idJugador);
        } else {
            JOptionPane.showMessageDialog(this, "ID de jugador no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    //Carga los datos del jugador en el formulario
    private void cargarJugadorDesdeBD(String idJugador) {
    	String id_EquipoJug = null; //Si no se selecciono equipo el jugador se guarda sin equipo (equipo = null)
    	Jugador jugador = DatabaseManager.obtenerJugadorPorId(idJugador);
		
    	txtId.setText(jugador.getId());
		txtNombre.setText(jugador.getNombre());
		txtPosicion.setText(jugador.getPosicion());
		txtNumero.setText(String.valueOf(jugador.getNumero()));
		txtEquipo.setText(jugador.getID_Equipo());
    }
 

}
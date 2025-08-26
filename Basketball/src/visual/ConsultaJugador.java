package visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import SQL.DatabaseManager;
import logico.Equipo;
import logico.Jugador;

public class ConsultaJugador extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPosicion;
    private JTextField txtNumero;
    private JTextField txtEquipo;

    /**
     * Constructor: recibe el ID del jugador y muestra sus datos
     */
    public ConsultaJugador(String idJugador) {
        super();

        if (idJugador == null || idJugador.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "ID de jugador no válido.",
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setModal(true);
        setTitle("Consulta de Jugador");
        setResizable(false);
        setBounds(100, 100, 500, 380);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // === Panel principal ===
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(panel, BorderLayout.CENTER);

        int xLabel = 30, xField = 150, y = 30, dy = 40, fieldWidth = 220;

        // === Fuente común ===
        Font labelFont = new Font("Tahoma", Font.PLAIN, 13);
        Font valueFont = new Font("Tahoma", Font.BOLD, 13);

        // --- ID ---
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(labelFont);
        lblId.setBounds(xLabel, y, 100, 25);
        panel.add(lblId);

        txtId = new JTextField();
        txtId.setFont(valueFont);
        txtId.setBounds(xField, y, fieldWidth, 28);
        txtId.setEditable(false);
        panel.add(txtId);
        y += dy;

        // --- Nombre ---
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(labelFont);
        lblNombre.setBounds(xLabel, y, 100, 25);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(valueFont);
        txtNombre.setBounds(xField, y, fieldWidth, 28);
        txtNombre.setEditable(false);
        panel.add(txtNombre);
        y += dy;

        // --- Posición ---
        JLabel lblPosicion = new JLabel("Posición:");
        lblPosicion.setFont(labelFont);
        lblPosicion.setBounds(xLabel, y, 100, 25);
        panel.add(lblPosicion);

        txtPosicion = new JTextField();
        txtPosicion.setFont(valueFont);
        txtPosicion.setBounds(xField, y, fieldWidth, 28);
        txtPosicion.setEditable(false);
        panel.add(txtPosicion);
        y += dy;

        // --- Número ---
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setFont(labelFont);
        lblNumero.setBounds(xLabel, y, 100, 25);
        panel.add(lblNumero);

        txtNumero = new JTextField();
        txtNumero.setFont(valueFont);
        txtNumero.setBounds(xField, y, fieldWidth, 28);
        txtNumero.setEditable(false);
        panel.add(txtNumero);
        y += dy;

        // --- Equipo ---
        JLabel lblEquipoLabel = new JLabel("Equipo:");
        lblEquipoLabel.setFont(labelFont);
        lblEquipoLabel.setBounds(xLabel, y, 100, 25);
        panel.add(lblEquipoLabel);

        txtEquipo = new JTextField();
        txtEquipo.setFont(valueFont);
        txtEquipo.setBounds(xField, y, fieldWidth, 28);
        txtEquipo.setEditable(false);
        panel.add(txtEquipo);

        // === Botón Cerrar ===
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // === Cargar datos desde la base de datos ===
        cargarJugadorDesdeBD(idJugador);
    }

    /**
     * Carga los datos del jugador desde la base de datos
     */
    private void cargarJugadorDesdeBD(String idJugador) {
        Jugador jugador = DatabaseManager.obtenerJugadorPorId(idJugador);

        if (jugador == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró ningún jugador con ID: " + idJugador,
                "Jugador no encontrado", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // --- Cargar datos básicos ---
        txtId.setText(jugador.getId());
        txtNombre.setText(jugador.getNombre() + " " + jugador.getApellido());
        txtPosicion.setText(jugador.getPosicion());
        txtNumero.setText(String.valueOf(jugador.getNumero()));

        // --- Cargar equipo ---
        String idEquipo = jugador.getID_Equipo();
        if (idEquipo != null && !idEquipo.isEmpty()) {
            Equipo equipo = DatabaseManager.obtenerEquipoPorId(idEquipo);
            txtEquipo.setText(equipo != null ? equipo.getNombre() : idEquipo);
        } else {
            txtEquipo.setText("Sin equipo");
        }
    }
}
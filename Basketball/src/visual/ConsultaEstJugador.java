package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logico.EstJugador;
import logico.Jugador;
import javax.swing.JOptionPane;

public class ConsultaEstJugador extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JTextField txtCantJuegos;
    private JTextField txtTriples;
    private JTextField txtDobles;
    private JTextField txtNormales;
    private JTextField txtPuntosTot;
    private JTextField txtRobos;
    private JTextField txtTapones;
    private JTextField txtAsistencias;
    private JTextField txtFaltas;
    private JTextField txtMVP;

    public ConsultaEstJugador(Jugador jugador) {
        super();

        if (jugador == null) {
            JOptionPane.showMessageDialog(this,
                "No se puede mostrar estadísticas: jugador no válido.",
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setResizable(false);
        setModal(true);
        setTitle("Estadísticas | " + jugador.getNombre() + " " + jugador.getApellido());
        setBounds(100, 100, 380, 480);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // === Fuente común ===
        Font labelFont = new Font("Tahoma", Font.PLAIN, 13);
        Font valueFont = new Font("Tahoma", Font.BOLD, 13);

        // === Cargar estadísticas ===
        EstJugador estadisticas = jugador.getEstadisticas();
        if (estadisticas == null) {
            JOptionPane.showMessageDialog(this,
                "El jugador no tiene estadísticas registradas.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            estadisticas = new EstJugador();
        }

        // === Campos de estadísticas ===
        addLabelAndField("Cantidad de Juegos:", 20, estadisticas.getCantJuegos());
        addLabelAndField("Triples:", 60, estadisticas.getTriples());
        addLabelAndField("Dobles:", 100, estadisticas.getDobles());
        addLabelAndField("Lanzamientos Libres:", 140, estadisticas.getNormales());
        addLabelAndField("Puntos Totales:", 180, estadisticas.getPuntosTot());
        addLabelAndField("Robos:", 220, estadisticas.getRobos());
        addLabelAndField("Tapones:", 260, estadisticas.getTapones());
        addLabelAndField("Asistencias:", 300, estadisticas.intgetAsistencias());
        addLabelAndField("Faltas:", 340, estadisticas.getFaltas());
        addLabelAndField("Veces MVP:", 380, estadisticas.getMvp());

        // === Botón Cerrar ===
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        // Cargar datos
        loadEstadistica(jugador);
    }

    private void addLabelAndField(String labelText, int y, int value) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label.setBounds(20, y, 160, 16);
        contentPanel.add(label);

        JTextField textField = new JTextField(String.valueOf(value));
        textField.setEditable(false);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Tahoma", Font.BOLD, 13));
        textField.setBounds(190, y - 3, 120, 25);
        contentPanel.add(textField);

        // Asociar campo para actualizarlo después
        switch (labelText) {
            case "Cantidad de Juegos:":
                txtCantJuegos = textField;
                break;
            case "Triples:":
                txtTriples = textField;
                break;
            case "Dobles:":
                txtDobles = textField;
                break;
            case "Lanzamientos Libres:":
                txtNormales = textField;
                break;
            case "Puntos Totales:":
                txtPuntosTot = textField;
                break;
            case "Robos:":
                txtRobos = textField;
                break;
            case "Tapones:":
                txtTapones = textField;
                break;
            case "Asistencias:":
                txtAsistencias = textField;
                break;
            case "Faltas:":
                txtFaltas = textField;
                break;
            case "Veces MVP:":
                txtMVP = textField;
                break;
        }
    }

    public void loadEstadistica(Jugador jugador) {
        if (jugador == null || jugador.getEstadisticas() == null) {
            return;
        }

        EstJugador est = jugador.getEstadisticas();
        txtCantJuegos.setText(String.valueOf(est.getCantJuegos()));
        txtTriples.setText(String.valueOf(est.getTriples()));
        txtDobles.setText(String.valueOf(est.getDobles()));
        txtNormales.setText(String.valueOf(est.getNormales()));
        txtPuntosTot.setText(String.valueOf(est.getPuntosTot()));
        txtRobos.setText(String.valueOf(est.getRobos()));
        txtTapones.setText(String.valueOf(est.getTapones()));
        txtAsistencias.setText(String.valueOf(est.intgetAsistencias()));
        txtFaltas.setText(String.valueOf(est.getFaltas()));
        txtMVP.setText(String.valueOf(est.getMvp()));
    }
}
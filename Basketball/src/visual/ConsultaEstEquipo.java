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

import logico.Equipo;
import logico.EstEquipo;
import javax.swing.JOptionPane;

public class ConsultaEstEquipo extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JTextField txtCantJuegos;
    private JTextField txtTriples;
    private JTextField txtDobles;
    private JTextField txtNormales;
    private JTextField txtPuntosTot;
    private JTextField txtJuegosGanados;
    private JTextField txtJuegosPerdidos;
    private JTextField txtTorneosGanados;

    public ConsultaEstEquipo(Equipo equipo) {
        super();

        if (equipo == null) {
            JOptionPane.showMessageDialog(this,
                "No se puede mostrar estadísticas: equipo no válido.",
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setResizable(false);
        setModal(true);
        setTitle("Estadísticas | " + equipo.getNombre());
        setBounds(100, 100, 380, 450);
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
        EstEquipo estadistica = equipo.getEstadistica();
        if (estadistica == null) {
            JOptionPane.showMessageDialog(this,
                "El equipo no tiene estadísticas registradas.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            estadistica = new EstEquipo();
        }

        // === Campos de estadísticas ===
        addLabelAndField("Cantidad de Juegos:", 20, estadistica.getCantJuegos());
        addLabelAndField("Triples:", 60, estadistica.getTriples());
        addLabelAndField("Dobles:", 100, estadistica.getDobles());
        addLabelAndField("Lanzamientos Libres:", 140, estadistica.getNormales());
        addLabelAndField("Puntos Totales:", 180, estadistica.getPuntosTot());
        addLabelAndField("Juegos Ganados:", 220, estadistica.getJuegosGanados());
        addLabelAndField("Juegos Perdidos:", 260, estadistica.getJuegosPerdidos());
        addLabelAndField("Torneos Ganados:", 300, estadistica.getTorneosGanados());

        // === Botón Cerrar ===
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCerrar);

        // Cargar datos
        loadEstadistica(equipo);
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
            case "Juegos Ganados:":
                txtJuegosGanados = textField;
                break;
            case "Juegos Perdidos:":
                txtJuegosPerdidos = textField;
                break;
            case "Torneos Ganados:":
                txtTorneosGanados = textField;
                break;
        }
    }

    public void loadEstadistica(Equipo equipo) {
        if (equipo == null || equipo.getEstadistica() == null) {
            return;
        }

        EstEquipo estadistica = equipo.getEstadistica();
        txtCantJuegos.setText(String.valueOf(estadistica.getCantJuegos()));
        txtTriples.setText(String.valueOf(estadistica.getTriples()));
        txtDobles.setText(String.valueOf(estadistica.getDobles()));
        txtNormales.setText(String.valueOf(estadistica.getNormales()));
        txtPuntosTot.setText(String.valueOf(estadistica.getPuntosTot()));
        txtJuegosGanados.setText(String.valueOf(estadistica.getJuegosGanados()));
        txtJuegosPerdidos.setText(String.valueOf(estadistica.getJuegosPerdidos()));
        txtTorneosGanados.setText(String.valueOf(estadistica.getTorneosGanados()));
    }
}
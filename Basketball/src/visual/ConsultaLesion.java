package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logico.Jugador;
import logico.Lesion;

public class ConsultaLesion extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JTextField txtId;
    private JTextField txtIdJugador;
    private JTextField txtNomJugador;
    private JTextField txtTipoLesion;
    private JTextField txtFechaLesion;
    private JTextField txtFechaRec;
    private JTextField txtDiasReposo;
    private JLabel lblEstado;
    private JTextArea txtDescripcion;
    private JButton btnCerrar;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructor: muestra los detalles de una lesión
     * @param jugador (opcional, puede ser null si se obtiene de la lesión)
     * @param lesion Lesión a consultar
     */
    public ConsultaLesion(Jugador jugador, Lesion lesion) {
        super();

        if (lesion == null) {
            JOptionPane.showMessageDialog(this,
                "No se puede consultar una lesión nula.",
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // Si no se pasa jugador, usar el de la lesión
        if (jugador == null) {
            jugador = lesion.getJugador();
        }

        if (jugador == null) {
            JOptionPane.showMessageDialog(this,
                "La lesión no está asociada a un jugador válido.",
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setResizable(false);
        setModal(true);
        setTitle("Consulta de Lesión");
        setBounds(100, 100, 550, 420);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // === Fuente común ===
        Font labelFont = new Font("Tahoma", Font.PLAIN, 13);
        Font valueFont = new Font("Tahoma", Font.BOLD, 13);

        // === ID de la lesión ===
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(labelFont);
        lblId.setBounds(30, 20, 30, 16);
        contentPanel.add(lblId);

        txtId = new JTextField(lesion.getId());
        txtId.setEditable(false);
        txtId.setFont(valueFont);
        txtId.setBounds(70, 17, 150, 25);
        contentPanel.add(txtId);

        // === Estado de la lesión ===
        JLabel lblEstadoLabel = new JLabel("Estado:");
        lblEstadoLabel.setFont(labelFont);
        lblEstadoLabel.setBounds(300, 20, 50, 16);
        contentPanel.add(lblEstadoLabel);

        lblEstado = new JLabel(lesion.isEstado() ? "Activa" : "Recuperado");
        lblEstado.setFont(valueFont);
        lblEstado.setForeground(lesion.isEstado() ? Color.RED : Color.GREEN.darker());
        lblEstado.setBounds(360, 20, 120, 16);
        contentPanel.add(lblEstado);

        // === Jugador ===
        JLabel lblJugador = new JLabel("Jugador:");
        lblJugador.setFont(labelFont);
        lblJugador.setBounds(30, 60, 60, 16);
        contentPanel.add(lblJugador);

        txtIdJugador = new JTextField(jugador.getId());
        txtIdJugador.setEditable(false);
        txtIdJugador.setFont(valueFont);
        txtIdJugador.setBounds(100, 57, 100, 25);
        contentPanel.add(txtIdJugador);

        txtNomJugador = new JTextField(jugador.getNombre() + " " + jugador.getApellido());
        txtNomJugador.setEditable(false);
        txtNomJugador.setFont(valueFont);
        txtNomJugador.setBounds(210, 57, 270, 25);
        contentPanel.add(txtNomJugador);

        // === Tipo de lesión ===
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(labelFont);
        lblTipo.setBounds(30, 100, 60, 16);
        contentPanel.add(lblTipo);

        txtTipoLesion = new JTextField(lesion.getTipoDeLesion());
        txtTipoLesion.setEditable(false);
        txtTipoLesion.setFont(valueFont);
        txtTipoLesion.setBounds(100, 97, 380, 25);
        contentPanel.add(txtTipoLesion);

        // === Fechas ===
        JLabel lblFechaLesion = new JLabel("Fecha lesión:");
        lblFechaLesion.setFont(labelFont);
        lblFechaLesion.setBounds(30, 140, 90, 16);
        contentPanel.add(lblFechaLesion);

        txtFechaLesion = new JTextField(lesion.getFechaLes().format(formatter));
        txtFechaLesion.setEditable(false);
        txtFechaLesion.setFont(valueFont);
        txtFechaLesion.setBounds(130, 137, 120, 25);
        contentPanel.add(txtFechaLesion);

        JLabel lblFechaRec = new JLabel("Recuperación:");
        lblFechaRec.setFont(labelFont);
        lblFechaRec.setBounds(270, 140, 90, 16);
        contentPanel.add(lblFechaRec);

        txtFechaRec = new JTextField(lesion.getFechaRecPrevista().format(formatter));
        txtFechaRec.setEditable(false);
        txtFechaRec.setFont(valueFont);
        txtFechaRec.setBounds(370, 137, 110, 25);
        contentPanel.add(txtFechaRec);

        // === Días de reposo ===
        JLabel lblDias = new JLabel("Días:");
        lblDias.setFont(labelFont);
        lblDias.setBounds(30, 180, 60, 16);
        contentPanel.add(lblDias);

        long dias = ChronoUnit.DAYS.between(lesion.getFechaLes(), lesion.getFechaRecPrevista());
        txtDiasReposo = new JTextField(String.valueOf(dias));
        txtDiasReposo.setEditable(false);
        txtDiasReposo.setFont(valueFont);
        txtDiasReposo.setBounds(100, 177, 60, 25);
        contentPanel.add(txtDiasReposo);

        // === Descripción ===
        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(labelFont);
        lblDesc.setBounds(30, 220, 80, 16);
        contentPanel.add(lblDesc);

        txtDescripcion = new JTextArea(lesion.getDescripcionCorta());
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtDescripcion.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(txtDescripcion);
        scrollPane.setBounds(30, 240, 450, 80);
        contentPanel.add(scrollPane);

        // === Botón Cerrar ===
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCerrar);
    }
}
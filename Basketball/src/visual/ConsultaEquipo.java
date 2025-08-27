package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

import SQL.DatabaseManager;
import logico.Equipo;
import javax.swing.JOptionPane;

public class ConsultaEquipo extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEntrenador;
    private JTextField txtDueno;
    private JTextField cmbxPais;
    private JTextField spnAnoFund;
    private JLabel lblFoto;
    private JPanel panel;
    private JButton btnVerJugadores;
    private JButton btnVerEstadisticas;

    /**
     * Constructor: recibe el ID del equipo y consulta la BD
     */
    public ConsultaEquipo(String idEquipo) {
        setModal(true);
        setTitle("Consultar Equipo");
        setBounds(100, 100, 502, 461);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        contentPanel.setLayout(null);

        // --- Etiquetas y campos ---
        JLabel lblId = new JLabel("Id:");
        lblId.setBounds(66, 17, 16, 16);
        contentPanel.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(88, 13, 388, 22);
        txtId.setEditable(false);
        txtId.setColumns(10);
        contentPanel.add(txtId);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(31, 43, 50, 16);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(88, 40, 179, 22);
        txtNombre.setEditable(false);
        txtNombre.setColumns(10);
        contentPanel.add(txtNombre);

        // ✅ Corregido: "Año de fundación"
        JLabel lblAnoFund = new JLabel("Año de fundación:");
        lblAnoFund.setBounds(277, 42, 105, 16);
        contentPanel.add(lblAnoFund);

        spnAnoFund = new JTextField();
        spnAnoFund.setBounds(385, 40, 90, 22);
        spnAnoFund.setEditable(false);
        spnAnoFund.setColumns(10);
        contentPanel.add(spnAnoFund);

        JLabel ldlPais = new JLabel("Pais:");
        ldlPais.setBounds(54, 72, 28, 16);
        contentPanel.add(ldlPais);

        cmbxPais = new JTextField();
        cmbxPais.setBounds(88, 66, 388, 22);
        cmbxPais.setEditable(false);
        cmbxPais.setColumns(10);
        contentPanel.add(cmbxPais);

        JLabel lblEntrenador = new JLabel("Entrenador:");
        lblEntrenador.setBounds(14, 95, 68, 16);
        contentPanel.add(lblEntrenador);

        txtEntrenador = new JTextField();
        txtEntrenador.setBounds(88, 92, 155, 22);
        txtEntrenador.setEditable(false);
        txtEntrenador.setColumns(10);
        contentPanel.add(txtEntrenador);

        // ✅ Corregido: "Dueño"
        JLabel lblDueno = new JLabel("Dueño:");
        lblDueno.setBounds(259, 95, 52, 16);
        contentPanel.add(lblDueno);

        txtDueno = new JTextField();
        txtDueno.setBounds(321, 92, 155, 22);
        txtDueno.setEditable(false);
        txtDueno.setColumns(10);
        contentPanel.add(txtDueno);

        lblFoto = new JLabel("Foto del emblema:");
        lblFoto.setBounds(21, 120, 113, 16);
        contentPanel.add(lblFoto);

        // --- Panel de imagen ---
        panel = new JPanel();
        panel.setBounds(11, 141, 225, 223);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        panel.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel("Sin imagen", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
        panel.add(imageLabel, BorderLayout.CENTER);
        contentPanel.add(panel);

        // --- Botones ---
        btnVerJugadores = new JButton("Ver Listado de Jugadores");
        btnVerJugadores.setBounds(259, 141, 216, 70);
        btnVerJugadores.addActionListener(e -> {
            new ListadoJugadores(idEquipo).setVisible(true);
        });
        contentPanel.add(btnVerJugadores);

        JButton btnVerListadoDe = new JButton("Ver Calendario de Juegos");
        btnVerListadoDe.setBounds(259, 219, 216, 70);
        btnVerListadoDe.addActionListener(e -> {
            new ListadoJuegos(idEquipo).setVisible(true);
        });
        contentPanel.add(btnVerListadoDe);

        btnVerEstadisticas = new JButton("Ver Estadísticas");
        btnVerEstadisticas.setBounds(259, 294, 216, 70);
        btnVerEstadisticas.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad en desarrollo.", "Próximamente", JOptionPane.INFORMATION_MESSAGE);
        });
        contentPanel.add(btnVerEstadisticas);

        // --- Botón Volver ---
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Volver");
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        // --- Cargar datos desde BD ---
        if (idEquipo != null && !idEquipo.trim().isEmpty()) {
            cargarDesdeBD(idEquipo);
        } else {
            JOptionPane.showMessageDialog(this, "ID de equipo no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
     * Carga los datos del equipo desde la base de datos
     */
    private void cargarDesdeBD(String idEquipo) {
        Equipo equipo = DatabaseManager.obtenerEquipoPorId(idEquipo);

        // ✅ Validación si el equipo no existe
        if (equipo == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró el equipo con ID: " + idEquipo,
                "Equipo no encontrado", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        txtId.setText(equipo.getId());
        txtNombre.setText(equipo.getNombre());
        spnAnoFund.setText(String.valueOf(equipo.getAnoFundacion()));
        cmbxPais.setText(equipo.getPais());
        txtEntrenador.setText(equipo.getEntrenador());
        txtDueno.setText(equipo.getDueno());

        // ✅ Mostrar imagen si existe
        BufferedImage imagen = equipo.getImagenLogo();
        if (imagen != null) {
            mostrarImagen(imagen);
        } else {
            panel.removeAll();
            panel.add(new JLabel("Sin imagen", SwingConstants.CENTER), BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }

    /**
     * Muestra la imagen del logo escalada correctamente
     */
    private void mostrarImagen(BufferedImage imagen) {
		panel.removeAll();
		panel.setLayout(new BorderLayout());
		try {
			Image scaled = imagen.getScaledInstance(panel.getWidth() - 2, panel.getHeight() - 2, Image.SCALE_SMOOTH);
	        JLabel label = new JLabel(new ImageIcon(scaled));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        label.setVerticalAlignment(SwingConstants.CENTER);
	        panel.add(label, BorderLayout.CENTER);
		} catch (Exception e) {
			JLabel label = new JLabel("Error al cargar", SwingConstants.CENTER);
			panel.add(label, BorderLayout.CENTER);
		}
		panel.revalidate();
		panel.repaint();
	}
}
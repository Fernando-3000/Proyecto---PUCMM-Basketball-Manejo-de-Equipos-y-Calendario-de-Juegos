package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

import SQL.DatabaseManager;
import logico.Equipo;
import logico.Jugador;

public class ConsultaJugador extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtPosicion;
    private JTextField txtPeso;
    private JTextField txtAltura;
    private JTextField txtNumero;
    private JTextField txtEquipo;
    private JPanel panelFoto;

    /**
     * Constructor: recibe el ID del jugador y consulta la BD
     */
    public ConsultaJugador(String idJugador) {
        setModal(true);
        setTitle("Consultar Jugador");
        setBounds(100, 100, 500, 480);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        contentPanel.setLayout(null);

        // --- Labels y campos ---
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(30, 20, 30, 16);
        contentPanel.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(80, 18, 380, 22);
        txtId.setEditable(false);
        contentPanel.add(txtId);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 55, 60, 16);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(80, 52, 150, 22);
        txtNombre.setEditable(false);
        contentPanel.add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setBounds(250, 55, 60, 16);
        contentPanel.add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setBounds(310, 52, 150, 22);
        txtApellido.setEditable(false);
        contentPanel.add(txtApellido);

        JLabel lblPosicion = new JLabel("Posición:");
        lblPosicion.setBounds(20, 90, 60, 16);
        contentPanel.add(lblPosicion);

        txtPosicion = new JTextField();
        txtPosicion.setBounds(80, 87, 380, 22);
        txtPosicion.setEditable(false);
        contentPanel.add(txtPosicion);

        JLabel lblPeso = new JLabel("Peso (kg):");
        lblPeso.setBounds(20, 125, 70, 16);
        contentPanel.add(lblPeso);

        txtPeso = new JTextField();
        txtPeso.setBounds(90, 122, 80, 22);
        txtPeso.setEditable(false);
        contentPanel.add(txtPeso);

        JLabel lblAltura = new JLabel("Altura (cm):");
        lblAltura.setBounds(190, 125, 80, 16);
        contentPanel.add(lblAltura);

        txtAltura = new JTextField();
        txtAltura.setBounds(270, 122, 80, 22);
        txtAltura.setEditable(false);
        contentPanel.add(txtAltura);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(370, 125, 60, 16);
        contentPanel.add(lblNumero);

        txtNumero = new JTextField();
        txtNumero.setBounds(430, 122, 30, 22);
        txtNumero.setEditable(false);
        contentPanel.add(txtNumero);

        JLabel lblEquipo = new JLabel("Equipo:");
        lblEquipo.setBounds(20, 160, 50, 16);
        contentPanel.add(lblEquipo);

        txtEquipo = new JTextField();
        txtEquipo.setBounds(80, 157, 380, 22);
        txtEquipo.setEditable(false);
        contentPanel.add(txtEquipo);

        // --- Panel foto ---
        JLabel lblFoto = new JLabel("Foto:");
        lblFoto.setBounds(20, 190, 50, 16);
        contentPanel.add(lblFoto);

        panelFoto = new JPanel();
        panelFoto.setBounds(20, 210, 220, 220);
        panelFoto.setBackground(Color.WHITE);
        panelFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        panelFoto.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel("Sin foto", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
        panelFoto.add(imageLabel, BorderLayout.CENTER);
        contentPanel.add(panelFoto);

        // --- Botón volver ---
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnVolver.setBounds(360, 380, 100, 40);
        btnVolver.addActionListener(e -> dispose());
        contentPanel.add(btnVolver);

        // --- Cargar datos ---
        if (idJugador != null && !idJugador.trim().isEmpty()) {
            cargarDesdeBD(idJugador);
        } else {
            JOptionPane.showMessageDialog(this, "ID de jugador no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void cargarDesdeBD(String idJugador) {
        Jugador jugador = DatabaseManager.obtenerJugadorPorId(idJugador);

        if (jugador == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el jugador con ID: " + idJugador,
                    "Jugador no encontrado", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        txtId.setText(jugador.getId());
        txtNombre.setText(jugador.getNombre());
        txtApellido.setText(jugador.getApellido());
        txtPosicion.setText(jugador.getPosicion());
        txtPeso.setText(String.valueOf(jugador.getPesoKg()));
        txtAltura.setText(String.valueOf(jugador.getAlturaCm()));
        txtNumero.setText(String.valueOf(jugador.getNumero()));

        Equipo equipo = jugador.getEquipo();
        txtEquipo.setText(equipo != null ? equipo.getNombre() : "Sin equipo");
       
        BufferedImage imagen = jugador.getFotoBuffered();
        if (imagen != null) {
            mostrarImagen(imagen);
        } else {
        	panelFoto.removeAll();
        	panelFoto.add(new JLabel("Sin imagen", SwingConstants.CENTER), BorderLayout.CENTER);
        	panelFoto.revalidate();
        	panelFoto.repaint();
        }
    }
    
    private void mostrarImagen(BufferedImage imagen) {
		panelFoto.removeAll();
		panelFoto.setLayout(new BorderLayout());
		try {
			Image scaled = imagen.getScaledInstance(panelFoto.getWidth() - 2, panelFoto.getHeight() - 2, Image.SCALE_SMOOTH);
	        JLabel label = new JLabel(new ImageIcon(scaled));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        label.setVerticalAlignment(SwingConstants.CENTER);
	        panelFoto.add(label, BorderLayout.CENTER);
		} catch (Exception e) {
			JLabel label = new JLabel("Error al cargar", SwingConstants.CENTER);
			panelFoto.add(label, BorderLayout.CENTER);
		}
		panelFoto.revalidate();
		panelFoto.repaint();
	}
}

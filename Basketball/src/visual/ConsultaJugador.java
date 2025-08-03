package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import logico.Jugador;
import logico.SerieNacional;

public class ConsultaJugador extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblApellido;
    private JLabel lblPosicion;
    private JLabel lblPeso;
    private JLabel lblAltura;
    private JLabel lblFoto;
    private JPanel panel;
    private JButton cancelButton;
    private File selectedFile = null;
    private JTextField txtEquipoNombre;
    private JTextField txtIdEquipo;
    private JTextField cmbxPosicion;
    private JTextField spnPeso;
    private JTextField spnAltura;
    private JTextField spnNumero;
    private JButton button;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ConsultaJugador dialog = new ConsultaJugador(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ConsultaJugador(Jugador aux) {
    	setResizable(false);
    	setModal(true);
        setTitle("Consultar Jugador");
        setBounds(100, 100, 500, 510);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);

        // ID Label and TextField
        lblId = new JLabel("Id:");
        lblId.setBounds(50, 17, 16, 16);
        txtId = new JTextField();
        txtId.setBounds(75, 13, 394, 22);
        txtId.setEditable(false);
        txtId.setColumns(10);

        // Nombre Label and TextField
        lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(16, 43, 50, 16);
        txtNombre = new JTextField();
        txtNombre.setEditable(false);
        txtNombre.setBounds(75, 40, 154, 22);
        txtNombre.setColumns(10);

        // Apellido Label and TextField
        lblApellido = new JLabel("Apellido:");
        lblApellido.setBounds(241, 43, 50, 16);
        txtApellido = new JTextField();
        txtApellido.setEditable(false);
        txtApellido.setBounds(296, 40, 173, 22);
        txtApellido.setColumns(10);

        // Posición Label and ComboBox
        lblPosicion = new JLabel("Posición:");
        lblPosicion.setBounds(16, 72, 59, 16);

        // Peso Label and Spinner
        lblPeso = new JLabel("Peso(kg):");
        lblPeso.setBounds(12, 99, 59, 16);

        // Altura Label and Spinner
        lblAltura = new JLabel("Altura(cm):");
        lblAltura.setBounds(169, 99, 66, 16);

        // Número Label and Spinner
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(337, 99, 66, 16);

        // Foto del jugador Label and Panel
        lblFoto = new JLabel("Foto del jugador:");
        lblFoto.setBounds(16, 128, 99, 16);
        panel = new JPanel();
        panel.setBounds(13, 149, 225, 223);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        panel.setLayout(null);

        // Add components to contentPanel
        contentPanel.setLayout(null);
        contentPanel.add(lblId);
        contentPanel.add(txtId);
        contentPanel.add(lblNombre);
        contentPanel.add(txtNombre);
        contentPanel.add(lblApellido);
        contentPanel.add(txtApellido);
        contentPanel.add(lblPosicion);
        contentPanel.add(lblPeso);
        contentPanel.add(lblAltura);
        contentPanel.add(lblNumero);
        contentPanel.add(panel);
        JLabel imageDisplayLabel = new JLabel("No hay imagen seleccionada", SwingConstants.CENTER);
        imageDisplayLabel.setBounds(0, 0, 220, 219);
        panel.add(imageDisplayLabel);
        imageDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(lblFoto);

        // Agregar botones para ver listado de lesiones y estadísticas
        JButton btnVerLesiones = new JButton("Ver Listado de Lesiones");
        btnVerLesiones.setBounds(253, 149, 216, 71); // Posición y tamaño del botón
        btnVerLesiones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ListadoLesiones listado = new ListadoLesiones(aux);
                listado.setVisible(true);
                listado.setModal(true);
            }
        });
        contentPanel.add(btnVerLesiones);

        JButton btnVerEstadisticas = new JButton("Ver Estadísticas");
        btnVerEstadisticas.setBounds(253, 309, 216, 63); // Posición y tamaño del botón
        btnVerEstadisticas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsultaEstJugador consulta = new ConsultaEstJugador(aux);
                consulta.setVisible(true);
                consulta.setModal(true);
            }
        });
        contentPanel.add(btnVerEstadisticas);
        
        JLabel label = new JLabel("Equipo:");
        label.setBounds(23, 390, 43, 16);
        contentPanel.add(label);
        
        txtEquipoNombre = new JTextField();
        txtEquipoNombre.setEditable(false);
        txtEquipoNombre.setColumns(10);
        txtEquipoNombre.setBounds(218, 387, 251, 22);
        contentPanel.add(txtEquipoNombre);
        
        txtIdEquipo = new JTextField();
        txtIdEquipo.setEditable(false);
        txtIdEquipo.setColumns(10);
        txtIdEquipo.setBounds(75, 387, 137, 22);
        contentPanel.add(txtIdEquipo);
        
        cmbxPosicion = new JTextField();
        cmbxPosicion.setEditable(false);
        cmbxPosicion.setColumns(10);
        cmbxPosicion.setBounds(75, 69, 394, 22);
        contentPanel.add(cmbxPosicion);
        
        spnPeso = new JTextField();
        spnPeso.setEditable(false);
        spnPeso.setColumns(10);
        spnPeso.setBounds(75, 96, 66, 22);
        contentPanel.add(spnPeso);
        
        spnAltura = new JTextField();
        spnAltura.setEditable(false);
        spnAltura.setColumns(10);
        spnAltura.setBounds(241, 96, 66, 22);
        contentPanel.add(spnAltura);
        
        spnNumero = new JTextField();
        spnNumero.setEditable(false);
        spnNumero.setColumns(10);
        spnNumero.setBounds(394, 96, 75, 22);
        contentPanel.add(spnNumero);
        
        button = new JButton("Ver Listado de Juegos");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ListadoJuegos listado = new ListadoJuegos(aux.getId());
        		listado.setVisible(true);
        		listado.setModal(true);
        	}
        });
        button.setBounds(253, 230, 216, 63);
        contentPanel.add(button);

        // Buttons Pane
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        // Cancelar Button
        cancelButton = new JButton("Volver");
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        loadJugador(aux);
    }

    private void loadJugador(Jugador aux) {
        if (aux != null) {
            txtId.setText(aux.getId());
            txtNombre.setText(aux.getNombre());
            txtApellido.setText(aux.getApellido());
            spnPeso.setText(Float.toString(aux.getPesoKg()));
            spnAltura.setText(Float.toString(aux.getAlturaCm()));
            spnNumero.setText(Integer.toString(aux.getNumero()));
            cmbxPosicion.setText(aux.getPosicion());
            txtIdEquipo.setText(aux.getEquipo() != null ? aux.getEquipo().getId() : "");
	        txtEquipoNombre.setText(aux.getEquipo() != null ? aux.getEquipo().getNombre() : "");
            selectedFile = aux.getFoto();
            if (selectedFile != null) {
                displayImage(selectedFile);
            } else {
                panel.removeAll();
                JLabel imageDisplayLabel = new JLabel("No hay imagen disponible", SwingConstants.CENTER);
                imageDisplayLabel.setBounds(10, 10, 205, 203);
                imageDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(imageDisplayLabel);
            }
        }
    }

    private void displayImage(File file) {
        try {
            ImageIcon icon = new ImageIcon(file.getPath());
            if (icon.getIconWidth() > 200 || icon.getIconHeight() > 200) {
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
            }
            panel.removeAll();
            JLabel imageDisplayLabel = new JLabel(icon);
            imageDisplayLabel.setBounds(10, 10, 205, 203);
            panel.add(imageDisplayLabel);
        } catch (Exception e) {
            e.printStackTrace();
            panel.removeAll();
            JLabel imageDisplayLabel = new JLabel("Error al cargar la imagen", SwingConstants.CENTER);
            imageDisplayLabel.setBounds(10, 10, 205, 203);
            panel.add(imageDisplayLabel);
        }
    }
}
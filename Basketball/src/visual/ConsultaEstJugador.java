package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.EstJugador;
import logico.Jugador;

import java.awt.Color;
import java.awt.Font;

public class ConsultaEstJugador extends JDialog {
    /**
	 * 
	 */
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

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ConsultaEstJugador dialog = new ConsultaEstJugador(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ConsultaEstJugador(Jugador aux) {
    	setResizable(false);
    	setModal(true);
        setTitle("Estadistica | ");
        setBounds(100, 100, 317, 409); 
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        contentPanel.setLayout(null);

        JLabel lblCantJuegos = new JLabel("Cantidad de Juegos:");
        lblCantJuegos.setBounds(20, 20, 150, 16);
        contentPanel.add(lblCantJuegos);

        txtCantJuegos = new JTextField();
        txtCantJuegos.setEditable(false);
        txtCantJuegos.setBounds(180, 17, 100, 22);
        contentPanel.add(txtCantJuegos);

        JLabel lblTriples = new JLabel("Triples:");
        lblTriples.setBounds(20, 50, 150, 16);
        contentPanel.add(lblTriples);

        txtTriples = new JTextField();
        txtTriples.setEditable(false);
        txtTriples.setBounds(180, 47, 100, 22);
        contentPanel.add(txtTriples);

        JLabel lblDobles = new JLabel("Dobles:");
        lblDobles.setBounds(20, 80, 150, 16);
        contentPanel.add(lblDobles);

        txtDobles = new JTextField();
        txtDobles.setEditable(false);
        txtDobles.setBounds(180, 77, 100, 22);
        contentPanel.add(txtDobles);

        JLabel lblNormales = new JLabel("Normales:");
        lblNormales.setBounds(20, 110, 150, 16);
        contentPanel.add(lblNormales);

        txtNormales = new JTextField();
        txtNormales.setEditable(false);
        txtNormales.setBounds(180, 107, 100, 22);
        contentPanel.add(txtNormales);

        JLabel lblPuntosTot = new JLabel("Puntos Totales:");
        lblPuntosTot.setBounds(20, 140, 150, 16);
        contentPanel.add(lblPuntosTot);

        txtPuntosTot = new JTextField();
        txtPuntosTot.setEditable(false);
        txtPuntosTot.setBounds(180, 137, 100, 22);
        contentPanel.add(txtPuntosTot);

        JLabel lblRobos = new JLabel("Robos:");
        lblRobos.setBounds(20, 170, 150, 16);
        contentPanel.add(lblRobos);

        txtRobos = new JTextField();
        txtRobos.setEditable(false);
        txtRobos.setBounds(180, 167, 100, 22);
        contentPanel.add(txtRobos);

        JLabel lblTapones = new JLabel("Tapones:");
        lblTapones.setBounds(20, 200, 150, 16);
        contentPanel.add(lblTapones);

        txtTapones = new JTextField();
        txtTapones.setEditable(false);
        txtTapones.setBounds(180, 197, 100, 22);
        contentPanel.add(txtTapones);

        JLabel lblAsistencias = new JLabel("Asistencias:");
        lblAsistencias.setBounds(20, 230, 150, 16);
        contentPanel.add(lblAsistencias);

        txtAsistencias = new JTextField();
        txtAsistencias.setEditable(false);
        txtAsistencias.setBounds(180, 227, 100, 22);
        contentPanel.add(txtAsistencias);

        JLabel lblFaltas = new JLabel("Faltas:");
        lblFaltas.setBounds(20, 260, 150, 16);
        contentPanel.add(lblFaltas);

        txtFaltas = new JTextField();
        txtFaltas.setEditable(false);
        txtFaltas.setBounds(180, 257, 100, 22);
        contentPanel.add(txtFaltas);

        JLabel lblMVP = new JLabel("Veces MVP:");
        lblMVP.setBounds(20, 290, 150, 16);
        contentPanel.add(lblMVP);

        txtMVP = new JTextField();
        txtMVP.setEditable(false);
        txtMVP.setBounds(180, 287, 100, 22);
        contentPanel.add(txtMVP);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton cancelButton = new JButton("Volver");
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
        
        loadEstadistica(aux);
    }
    
    public void loadEstadistica(Jugador jug) {
        if (jug == null || jug.getEstadisticas() == null)
            return;
        
        EstJugador estadisticas = jug.getEstadisticas();
        
        setTitle("Estadística | " + jug.getNombre() + " " + jug.getApellido());
        
        txtCantJuegos.setText(Integer.toString(estadisticas.getCantJuegos()));
        txtTriples.setText(Integer.toString(estadisticas.getTriples()));
        txtDobles.setText(Integer.toString(estadisticas.getDobles()));
        txtNormales.setText(Integer.toString(estadisticas.getNormales()));
        txtPuntosTot.setText(Integer.toString(estadisticas.getPuntosTot()));
        txtRobos.setText(Integer.toString(estadisticas.getRobos()));
        txtTapones.setText(Integer.toString(estadisticas.getTapones()));
        txtAsistencias.setText(Integer.toString(estadisticas.getAsistencias()));
        txtFaltas.setText(Integer.toString(estadisticas.getFaltas()));
        txtMVP.setText(Integer.toString(estadisticas.getMvp()));
    }
}
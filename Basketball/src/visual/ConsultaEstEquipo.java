package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.Equipo;
import logico.EstEquipo;

import java.awt.Color;
import java.awt.Font;

public class ConsultaEstEquipo extends JDialog {
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
    private JTextField txtJuegosGanados;
    private JTextField txtJuegosPerdidos;
    private JTextField txtTorneosGanados;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ConsultaEstEquipo dialog = new ConsultaEstEquipo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ConsultaEstEquipo(Equipo aux) {
    	setResizable(false);
    	setModal(true);
        setTitle("Estad\u00EDstica |");
        setBounds(100, 100, 312, 351); 
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

         JLabel lblJuegosGanados = new JLabel("Juegos Ganados:");
        lblJuegosGanados.setBounds(20, 170, 150, 16);
        contentPanel.add(lblJuegosGanados);

        txtJuegosGanados = new JTextField();
        txtJuegosGanados.setEditable(false);
        txtJuegosGanados.setBounds(180, 167, 100, 22);
        contentPanel.add(txtJuegosGanados);

         JLabel lblJuegosPerdidos = new JLabel("Juegos Perdidos:");
        lblJuegosPerdidos.setBounds(20, 200, 150, 16);
        contentPanel.add(lblJuegosPerdidos);

        txtJuegosPerdidos = new JTextField();
        txtJuegosPerdidos.setEditable(false);
        txtJuegosPerdidos.setBounds(180, 197, 100, 22);
        contentPanel.add(txtJuegosPerdidos);

         JLabel lblTorneosGanados = new JLabel("Torneos Ganados:");
        lblTorneosGanados.setBounds(20, 230, 150, 16);
        contentPanel.add(lblTorneosGanados);

        txtTorneosGanados = new JTextField();
        txtTorneosGanados.setEditable(false);
        txtTorneosGanados.setBounds(180, 227, 100, 22);
        contentPanel.add(txtTorneosGanados);

         JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

         JButton cancelButton = new JButton("Volver");
         cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
        
        loadEstadistica(aux);
    }
    
    public void loadEstadistica(Equipo aux) {
        if (aux == null || aux.getEstadistica() == null)
            return;
        
        EstEquipo estadistica = aux.getEstadistica();
        
        setTitle("Estadística | " + aux.getNombre());
   
        txtCantJuegos.setText(Integer.toString(estadistica.getCantJuegos()));
        txtTriples.setText(Integer.toString(estadistica.getTriples()));
        txtDobles.setText(Integer.toString(estadistica.getDobles()));
        txtNormales.setText(Integer.toString(estadistica.getNormales()));
        txtPuntosTot.setText(Integer.toString(estadistica.getPuntosTot()));
        txtJuegosGanados.setText(Integer.toString(estadistica.getJuegosGanados()));
        txtJuegosPerdidos.setText(Integer.toString(estadistica.getJuegosPerdidos()));
        txtTorneosGanados.setText(Integer.toString(estadistica.getTorneosGanados()));
    }
}
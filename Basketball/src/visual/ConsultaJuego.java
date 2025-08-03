package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.Jugador;
import logico.Juego;
import logico.Equipo;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConsultaJuego extends JDialog {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtEquipoCasa;
    private JTextField txtEquipoVisita;
    private JTextField txtMarcadorCasa;
    private JTextField txtMarcadorVisita;
    private JTextField txtGanador;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ConsultaJuego dialog = new ConsultaJuego(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ConsultaJuego(Juego aux) {
        setModal(true);
        setResizable(false);
        setTitle("Consulta de Juego");
        setBounds(100, 100, 370, 298);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        contentPanel.setLayout(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 20, 100, 16);
        contentPanel.add(lblId);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBounds(180, 17, 150, 22);
        contentPanel.add(txtId);

        JLabel lblEquipoCasa = new JLabel("Equipo Local:");
        lblEquipoCasa.setBounds(20, 50, 100, 16);
        contentPanel.add(lblEquipoCasa);

        txtEquipoCasa = new JTextField();
        txtEquipoCasa.setEditable(false);
        txtEquipoCasa.setBounds(180, 47, 150, 22);
        contentPanel.add(txtEquipoCasa);

        JLabel lblEquipoVisita = new JLabel("Equipo Visitante:");
        lblEquipoVisita.setBounds(20, 80, 100, 16);
        contentPanel.add(lblEquipoVisita);

        txtEquipoVisita = new JTextField();
        txtEquipoVisita.setEditable(false);
        txtEquipoVisita.setBounds(180, 77, 150, 22);
        contentPanel.add(txtEquipoVisita);

        JLabel lblMarcadorCasa = new JLabel("Marcador Local:");
        lblMarcadorCasa.setBounds(20, 110, 100, 16);
        contentPanel.add(lblMarcadorCasa);

        txtMarcadorCasa = new JTextField();
        txtMarcadorCasa.setEditable(false);
        txtMarcadorCasa.setBounds(180, 107, 150, 22);
        contentPanel.add(txtMarcadorCasa);

        JLabel lblMarcadorVisita = new JLabel("Marcador Visitante:");
        lblMarcadorVisita.setBounds(20, 140, 150, 16);
        contentPanel.add(lblMarcadorVisita);

        txtMarcadorVisita = new JTextField();
        txtMarcadorVisita.setEditable(false);
        txtMarcadorVisita.setBounds(180, 137, 150, 22);
        contentPanel.add(txtMarcadorVisita);

        JLabel lblGanador = new JLabel("Ganador:");
        lblGanador.setBounds(20, 170, 100, 16);
        contentPanel.add(lblGanador);

        txtGanador = new JTextField();
        txtGanador.setBounds(180, 167, 150, 22);
        txtGanador.setEditable(false); // Solo lectura
        contentPanel.add(txtGanador);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnCancel = new JButton("Volver");
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCancel);
        
        // Cargar los datos del juego
        loadJuego(aux);
    }
    
    /**
     * Carga los datos del juego en los campos de la ventana
     */
    public void loadJuego(Juego aux) {
        if (aux == null)
            return;
            
        txtId.setText(aux.getId());
        txtEquipoCasa.setText(aux.getHome().getId() + " " + aux.getHome().getNombre());
        txtEquipoVisita.setText(aux.getAway().getId() + " " + aux.getAway().getNombre());
        txtMarcadorCasa.setText(Integer.toString(aux.getMarcadorCasa()));
        txtMarcadorVisita.setText(Integer.toString(aux.getMarcadorAway()));
        txtGanador.setText(aux.getGanador());
    }
}
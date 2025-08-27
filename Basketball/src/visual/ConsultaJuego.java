package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.Juego;
import SQL.DatabaseManager;

public class ConsultaJuego extends JDialog {
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtEquipoCasa;
    private JTextField txtEquipoVisita;
    private JTextField txtMarcadorCasa;
    private JTextField txtMarcadorVisita;
    private JTextField txtGanador;

    /**
     * Launch the application (para pruebas)
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
    public ConsultaJuego(Juego juego) {
        setModal(true);
        setResizable(false);
        setTitle("Consulta de Juego");
        setBounds(100, 100, 400, 320);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // === Etiquetas y Campos ===

        JLabel lblId = new JLabel("ID del Juego:");
        lblId.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblId.setBounds(30, 20, 120, 16);
        contentPanel.add(lblId);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtId.setBounds(180, 17, 170, 25);
        contentPanel.add(txtId);

        JLabel lblEquipoCasa = new JLabel("Equipo Local:");
        lblEquipoCasa.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEquipoCasa.setBounds(30, 60, 120, 16);
        contentPanel.add(lblEquipoCasa);

        txtEquipoCasa = new JTextField();
        txtEquipoCasa.setEditable(false);
        txtEquipoCasa.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtEquipoCasa.setBounds(180, 57, 170, 25);
        contentPanel.add(txtEquipoCasa);

        JLabel lblEquipoVisita = new JLabel("Equipo Visitante:");
        lblEquipoVisita.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEquipoVisita.setBounds(30, 95, 120, 16);
        contentPanel.add(lblEquipoVisita);

        txtEquipoVisita = new JTextField();
        txtEquipoVisita.setEditable(false);
        txtEquipoVisita.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtEquipoVisita.setBounds(180, 92, 170, 25);
        contentPanel.add(txtEquipoVisita);

        JLabel lblMarcadorCasa = new JLabel("Marcador Local:");
        lblMarcadorCasa.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblMarcadorCasa.setBounds(30, 130, 120, 16);
        contentPanel.add(lblMarcadorCasa);

        txtMarcadorCasa = new JTextField();
        txtMarcadorCasa.setEditable(false);
        txtMarcadorCasa.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtMarcadorCasa.setBounds(180, 127, 170, 25);
        contentPanel.add(txtMarcadorCasa);

        JLabel lblMarcadorVisita = new JLabel("Marcador Visitante:");
        lblMarcadorVisita.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblMarcadorVisita.setBounds(30, 165, 130, 16);
        contentPanel.add(lblMarcadorVisita);

        txtMarcadorVisita = new JTextField();
        txtMarcadorVisita.setEditable(false);
        txtMarcadorVisita.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtMarcadorVisita.setBounds(180, 162, 170, 25);
        contentPanel.add(txtMarcadorVisita);

        JLabel lblGanador = new JLabel("Ganador:");
        lblGanador.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblGanador.setBounds(30, 200, 120, 16);
        contentPanel.add(lblGanador);

        txtGanador = new JTextField();
        txtGanador.setEditable(false);
        txtGanador.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtGanador.setForeground(Color.BLUE);
        txtGanador.setBounds(180, 197, 170, 25);
        contentPanel.add(txtGanador);

        // === Bot�n Cerrar ===
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnCancel = new JButton("Cerrar");
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCancel.addActionListener(e -> dispose());
        buttonPane.add(btnCancel);

        // === Cargar datos del juego ===
        loadJuego(juego);
    }

    /**
     * Carga los datos del juego en los campos
     */
    public void loadJuego(Juego juego) {
        if (juego == null) return;

        txtId.setText("JG-" + juego.getId());
        txtEquipoCasa.setText(juego.getHome().getNombre());
        txtEquipoVisita.setText(juego.getAway().getNombre());
        txtMarcadorCasa.setText(String.valueOf(juego.getMarcadorCasa()));
        txtMarcadorVisita.setText(String.valueOf(juego.getMarcadorAway()));
        txtGanador.setText(juego.getGanador() != null ? juego.getGanador() : "No definido");
    }
}
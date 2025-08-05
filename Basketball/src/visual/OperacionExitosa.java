package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OperacionExitosa extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            OperacionExitosa dialog = new OperacionExitosa();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public OperacionExitosa() {
    	setResizable(false);
    	setModal(true);
    	setAlwaysOnTop(true);
    	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    	setFont(new Font("Dialog", Font.PLAIN, 16));
        setTitle("Mensaje");
        setBounds(100, 100, 329, 137); 
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Panel principal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

         JLabel lblMensaje = new JLabel("Operacion Exitosa", JLabel.CENTER);
        lblMensaje.setBackground(new Color(240, 240, 240));
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setForeground(Color.DARK_GRAY);
        contentPanel.add(lblMensaje, BorderLayout.CENTER);

         JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

         JButton btnOK = new JButton("OK");
        btnOK.addActionListener(e -> dispose()); // Cierra la ventana al hacer clic
        buttonPane.add(btnOK);
    }
}
package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Jugador;
import logico.Lesion;
import logico.SerieNacional;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.TextArea;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class ConsultaLesion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField txtIdJugador;
	private JTextField txtNomJugador;
	private LocalDate today;
	private LocalDate recuperacion;
	private JTextField cmbxTipoLesion;
	private JTextField spnDiasReposo;
	private JTextField txtFechaRec;
	private JRadioButton rdLesion;
	private TextArea txtDescripcion;
	private JButton cancelButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConsultaLesion dialog = new ConsultaLesion(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConsultaLesion(Jugador jug, Lesion aux) {
		setResizable(false);
		setModal(true);
		today = LocalDate.now();
		recuperacion = today;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		setTitle("Consultar Lesion");
		setBounds(100, 100, 509, 322);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("Id:");
		label.setBounds(86, 16, 16, 16);
		contentPanel.add(label);
		
		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setColumns(10);
		txtId.setBounds(108, 9, 226, 22);
		contentPanel.add(txtId);
		
		JLabel lblTipoDeLesion = new JLabel("Tipo de lesi\u00F3n:");
		lblTipoDeLesion.setBounds(17, 65, 90, 16);
		contentPanel.add(lblTipoDeLesion);
		
		JLabel lblDasDeReposo = new JLabel("D\u00EDas de reposo:");
		lblDasDeReposo.setBounds(12, 197, 90, 16);
		contentPanel.add(lblDasDeReposo);
		JLabel lblFechaDeRecuperacin = new JLabel("Fecha de recuperaci\u00F3n:");
		lblFechaDeRecuperacin.setBounds(250, 197, 143, 16);
		contentPanel.add(lblFechaDeRecuperacin);
		
		JLabel label_1 = new JLabel("Jugador:");
		label_1.setBounds(51, 38, 52, 16);
		contentPanel.add(label_1);
		
		JLabel lblDescripcin = new JLabel("Descripci\u00F3n:");
		lblDescripcin.setBounds(31, 94, 70, 16);
		contentPanel.add(lblDescripcin);
		
		txtDescripcion = new TextArea();
		txtDescripcion.setEditable(false);
		txtDescripcion.setBounds(108, 89, 373, 99);
		contentPanel.add(txtDescripcion);
		
		txtIdJugador = new JTextField();
		txtIdJugador.setEditable(false);
		txtIdJugador.setColumns(10);
		txtIdJugador.setBounds(108, 35, 122, 22);
		contentPanel.add(txtIdJugador);
		
		txtNomJugador = new JTextField();
		txtNomJugador.setEditable(false);
		txtNomJugador.setColumns(10);
		txtNomJugador.setBounds(234, 35, 247, 22);
		contentPanel.add(txtNomJugador);
		
		cmbxTipoLesion = new JTextField();
		cmbxTipoLesion.setEditable(false);
		cmbxTipoLesion.setColumns(10);
		cmbxTipoLesion.setBounds(108, 62, 373, 22);
		contentPanel.add(cmbxTipoLesion);
		
		spnDiasReposo = new JTextField();
		spnDiasReposo.setEditable(false);
		spnDiasReposo.setColumns(10);
		spnDiasReposo.setBounds(108, 194, 74, 22);
		contentPanel.add(spnDiasReposo);
		
		rdLesion = new JRadioButton("Activa");
		rdLesion.setBackground(Color.WHITE);
		rdLesion.setEnabled(false);
		rdLesion.setSelected(true);
		rdLesion.setBounds(391, 9, 90, 25);
		contentPanel.add(rdLesion);
		
		JLabel label_2 = new JLabel("Estado:");
		label_2.setBounds(346, 13, 52, 16);
		contentPanel.add(label_2);
		
		txtFechaRec = new JTextField();
		txtFechaRec.setText("08/04/2025");
		txtFechaRec.setHorizontalAlignment(SwingConstants.CENTER);
		txtFechaRec.setEditable(false);
		txtFechaRec.setColumns(10);
		txtFechaRec.setBounds(391, 194, 90, 22);
		contentPanel.add(txtFechaRec);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				cancelButton = new JButton("Volver");
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadLesion(aux);
	}
	
	public void loadLesion(Lesion aux)
	{
		if (aux == null)
			return;
		
		txtId.setText(aux.getId());
		txtIdJugador.setText(aux.getJugador().getId());
		txtNomJugador.setText(aux.getJugador().getId());
		cmbxTipoLesion.setText(aux.getTipoDeLesion());
		today = aux.getFechaLes();
		recuperacion = aux.getFechaRecPrevista();
		spnDiasReposo.setText(Integer.toString((int)ChronoUnit.DAYS.between(today, recuperacion)));
		txtDescripcion.setText(aux.getDescripcionCorta());
		rdLesion.setSelected(aux.isEstado());
		if (rdLesion.isSelected())
			rdLesion.setText("Activa");
		else
			rdLesion.setText("No Activa");
	}
}

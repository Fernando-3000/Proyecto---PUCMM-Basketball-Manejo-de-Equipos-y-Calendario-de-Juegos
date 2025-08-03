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
import java.awt.ComponentOrientation;
import java.util.Locale;
import javax.swing.JRadioButton;
import javax.swing.DefaultComboBoxModel;

public class RegLesion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField txtFechaRec;
	private JTextField txtIdJugador;
	private JTextField txtNomJugador;
	private JComboBox<String> cmbxTipoLesion;
	private JSpinner spnDiasReposo;
	private LocalDate today;
	private LocalDate recuperacion;
	private JButton okButton;
	private JButton cancelButton;
	private TextArea txtDescripcion;
	private JRadioButton rdLesion;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegLesion dialog = new RegLesion(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegLesion(Jugador jug, Lesion aux) {
		setResizable(false);
		setModal(true);
		today = LocalDate.now();
		recuperacion = today;
		
		if (aux == null)
			setTitle("Registrar Lesion");
		else
			setTitle("Modificar Lesion");
		
		setBounds(100, 100, 513, 318);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("Id:");
		label.setBounds(91, 17, 16, 16);
		contentPanel.add(label);
		
		txtId = new JTextField();
		SerieNacional.getInstance();
		txtId.setText("LE-"+SerieNacional.getInstance().getGeneradorLesion());
		txtId.setEditable(false);
		txtId.setColumns(10);
		txtId.setBounds(113, 10, 226, 22);
		contentPanel.add(txtId);
		
		JLabel lblTipoDeLesion = new JLabel("Tipo de lesi\u00F3n:");
		lblTipoDeLesion.setBounds(23, 65, 90, 16);
		contentPanel.add(lblTipoDeLesion);
		
		cmbxTipoLesion = new JComboBox<String>();
		cmbxTipoLesion.setModel(new DefaultComboBoxModel(new String[] {"Seleccionar", "Esguince", "Fractura", "Lesi\u00F3n ligamentosa", "Desgarro muscular", "Contusi\u00F3n", "Luxaci\u00F3n", "Distensi\u00F3n", "Tensi\u00F3n muscular", "Lesi\u00F3n \u00F3sea", "Lesi\u00F3n articular", "Lesi\u00F3n tendinosa", "Otra"}));
		cmbxTipoLesion.setBounds(113, 63, 373, 22);
		contentPanel.add(cmbxTipoLesion);
		
		JLabel lblDasDeReposo = new JLabel("D\u00EDas de reposo:");
		lblDasDeReposo.setBounds(17, 205, 90, 16);
		contentPanel.add(lblDasDeReposo);
		
		spnDiasReposo = new JSpinner();
		spnDiasReposo.setModel(new SpinnerNumberModel(new Long(0), new Long(0), null, new Long(1)));
		spnDiasReposo.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				recuperacion = today.plusDays((long) Integer.parseInt(spnDiasReposo.getValue().toString()));
				txtFechaRec.setText(recuperacion.format(formatter));
			}
		});
		spnDiasReposo.setBounds(113, 202, 52, 22);
		contentPanel.add(spnDiasReposo);
		JLabel lblFechaDeRecuperacin = new JLabel("Fecha de recuperaci\u00F3n:");
		lblFechaDeRecuperacin.setBounds(254, 205, 143, 16);
		contentPanel.add(lblFechaDeRecuperacin);
		
		JLabel label_1 = new JLabel("Jugador:");
		label_1.setBounds(56, 39, 52, 16);
		contentPanel.add(label_1);
		
		txtFechaRec = new JTextField();
		txtFechaRec.setHorizontalAlignment(SwingConstants.CENTER);
		txtFechaRec.setEditable(false);
		txtFechaRec.setColumns(10);
		txtFechaRec.setBounds(396, 202, 90, 22);
		contentPanel.add(txtFechaRec);
		txtFechaRec.setText(recuperacion.format(formatter));
		
		JLabel lblDescripcin = new JLabel("Descripci\u00F3n:");
		lblDescripcin.setBounds(35, 95, 74, 16);
		contentPanel.add(lblDescripcin);
		
		txtDescripcion = new TextArea();
		txtDescripcion.setLocale(new Locale("es", "DO"));
		txtDescripcion.setBounds(113, 90, 373, 100);
		contentPanel.add(txtDescripcion);
		
		txtIdJugador = new JTextField();
		txtIdJugador.setEditable(false);
		txtIdJugador.setText(jug.getId());
		txtIdJugador.setColumns(10);
		txtIdJugador.setBounds(113, 36, 122, 22);
		contentPanel.add(txtIdJugador);
		
		txtNomJugador = new JTextField();
		txtNomJugador.setEditable(false);
		txtNomJugador.setText(jug.getNombre() + jug.getApellido());
		txtNomJugador.setColumns(10);
		txtNomJugador.setBounds(239, 36, 247, 22);
		contentPanel.add(txtNomJugador);
		
		rdLesion = new JRadioButton("Activa");
		rdLesion.setEnabled(false);
		rdLesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdLesion.isSelected())
					rdLesion.setText("Activa");
				else
					rdLesion.setText("No Activa");
			}
		});
		rdLesion.setSelected(true);
		rdLesion.setBounds(396, 9, 90, 25);
		contentPanel.add(rdLesion);
		
		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setBounds(351, 13, 52, 16);
		contentPanel.add(lblEstado);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Registrar");
				okButton.setFont(new Font("Tahoma", Font.BOLD, 13));
				if (aux != null)
					okButton.setText("Modificar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (aux == null)
						{
							if (jug != null && datosCompletos())
							{
							    String tipoLesion = cmbxTipoLesion.getSelectedItem() != null ? cmbxTipoLesion.getSelectedItem().toString() : "Seleccionar";
								Lesion les = new Lesion(txtId.getText(),
														SerieNacional.getInstance().searchJugadorById(jug.getId(), SerieNacional.getInstance().getMisJugadores()),
														today,
														tipoLesion,
														recuperacion,
														txtDescripcion.getText(),
														rdLesion.isSelected());
								SerieNacional.getInstance().searchJugadorById(jug.getId(), SerieNacional.getInstance().getMisJugadores()).getMisLesiones().add(les);
								ListadoLesiones.loadAll(jug, null);
								OperacionExitosa operacion = new OperacionExitosa();
							    operacion.setVisible(true);
							    operacion.setModal(true);
								dispose();		
							}
							else
							{
								OperacionEspecifica operacion = new OperacionEspecifica("Rellene todos los campos.");
							    operacion.setVisible(true);
							    operacion.setModal(true);
							}
						}
						else
						{
							String tipoLesion = cmbxTipoLesion.getSelectedItem() != null ? cmbxTipoLesion.getSelectedItem().toString() : "Seleccionar";
							aux.setTipoDeLesion(tipoLesion);
							aux.setFechaRecPrevista(recuperacion);
							aux.setDescripcionCorta(txtDescripcion.getText());
							aux.setEstado(rdLesion.isSelected());
							SerieNacional.getInstance().modificarLesion(aux);
							OperacionExitosa operacion = new OperacionExitosa();
						    operacion.setVisible(true);
						    operacion.setModal(true);
							dispose();	
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancelar");
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
		
		rdLesion.setEnabled(true);
		txtId.setText(aux.getId());
		txtFechaRec.setText(aux.getFechaRecPrevista().format(formatter));
		txtIdJugador.setText(aux.getJugador().getId());
		txtNomJugador.setText(aux.getJugador().getId());
		cmbxTipoLesion.setSelectedItem(aux.getTipoDeLesion());
		today = aux.getFechaLes();
		recuperacion = aux.getFechaRecPrevista();
		spnDiasReposo.setValue(ChronoUnit.DAYS.between(today, recuperacion));
		txtDescripcion.setText(aux.getDescripcionCorta());
		rdLesion.setSelected(aux.isEstado());
	}
	
	private boolean datosCompletos() {
	    return cmbxTipoLesion.getSelectedItem() != null
	        && !cmbxTipoLesion.getSelectedItem().toString().equals("Seleccionar")
	        && Integer.parseInt(spnDiasReposo.getValue().toString()) > 0
	        && txtDescripcion.getText() != null
	        && !txtDescripcion.getText().trim().isEmpty();
	}
}

package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import logico.Equipo;
import logico.SerieNacional; // Corrected to use SerieNacional instead of Torneo
import logico.User;

import SQL.DatabaseManager;

public class ListadoEquipos extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private static DefaultTableModel model;
	private static Object[] row;
	private Equipo equipoSeleccionado = null;
	private JTextField searchField;
	private JButton volverBtn;
	private JButton modificarBtn;
	private JButton registrarBtn;
	private JButton btnEliminar;
	private JPanel mainPanel;
	private JPanel searchPanel;
	private JScrollPane scrollPane;
	private JButton consultarBtn;

	public ListadoEquipos() {
		setModal(true);
		setResizable(false);
		setTitle("Listado de Equipos");
		setSize(800, 600);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		searchPanel = new JPanel(new BorderLayout());
		searchField = new JTextField("Buscar...");
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setEnabled(false);

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtrarTabla();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtrarTabla();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filtrarTabla();
			}

			private void filtrarTabla() {
				String text = searchField.getText();
				if (text.equals("Buscar...") || text.isEmpty()) {
					loadAll(null);
				} else {
					loadAll(text);
				}
			}
		});

		searchField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (searchField.getText().equals("Buscar...")) {
					searchField.setEnabled(true);
					searchField.setText("");
					modificarBtn.setEnabled(false);
					consultarBtn.setEnabled(false);
					btnEliminar.setEnabled(false);
					equipoSeleccionado = null;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (searchField.getText().equals("")) {
					searchField.setEnabled(false);
					searchField.setText("Buscar...");
				}
			}
		});

		searchPanel.add(new JLabel("Barra de busqueda:   "), BorderLayout.WEST);
		searchPanel.add(searchField, BorderLayout.CENTER);

		String[] columnNames = { "ID", "Equipo", "Entrenador", "Pais", "Cant. Juegos" };
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnIdentifiers(columnNames);

		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index != -1) {
					String equipoId = table.getValueAt(index, 0).toString();
					// Buscar Equipo por ID
					equipoSeleccionado = DatabaseManager.obtenerEquipoPorId(equipoId);
					modificarBtn.setEnabled(true);
					consultarBtn.setEnabled(true);
					btnEliminar.setEnabled(true);
				}
			}
		});

		scrollPane = new JScrollPane(table);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JLabel label = new JLabel("");
		buttonPanel.add(label);

		mainPanel.add(searchPanel, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		Font boldFont = new Font("Dialog", Font.BOLD, 12);

		registrarBtn = new JButton("Registrar");
		registrarBtn.setFont(boldFont);
		registrarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modificarBtn.setEnabled(false);
				consultarBtn.setEnabled(false);
				btnEliminar.setEnabled(false);
				equipoSeleccionado = null;
				RegEquipo regEquipo = new RegEquipo(null);
				regEquipo.setVisible(true);
				regEquipo.setModal(true);
			}
		});

		modificarBtn = new JButton("Modificar");
		modificarBtn.setEnabled(false);
		modificarBtn.setFont(boldFont);
		modificarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (equipoSeleccionado != null) {
					RegEquipo regEquipo = new RegEquipo(equipoSeleccionado);
					regEquipo.setVisible(true);
					regEquipo.setModal(true);
					modificarBtn.setEnabled(false);
					consultarBtn.setEnabled(false);
					btnEliminar.setEnabled(false);
					equipoSeleccionado = null;
					String text = searchField.getText();
					if (text.equals("Buscar...") || text.isEmpty()) {
						loadAll(null);
					} else {
						loadAll(text);
					}
				}
			}
		});

		consultarBtn = new JButton("Consultar");
		consultarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (equipoSeleccionado != null) {
					String id_Equipo = equipoSeleccionado.getId();
					ConsultaEquipo consulta = new ConsultaEquipo(id_Equipo);
					consulta.setVisible(true);
					consulta.setModal(true);
					modificarBtn.setEnabled(false);
					consultarBtn.setEnabled(false);
					btnEliminar.setEnabled(false);
					equipoSeleccionado = null;
					String text = searchField.getText();
					if (text.equals("Buscar...") || text.isEmpty()) {
						loadAll(null);
					} else {
						loadAll(text);
					}
				}
			}
		});

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setEnabled(false);
		btnEliminar.setFont(boldFont);
		btnEliminar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (equipoSeleccionado != null) {
					if (DatabaseManager.eliminarEquipo(equipoSeleccionado.getId())) {
						OperacionExitosa operacion = new OperacionExitosa();
						operacion.setVisible(true);
						operacion.setModal(true);
					} else {
						new OperacionEspecifica("No se pudo eliminar el usuario");
					}

					modificarBtn.setEnabled(false);
					consultarBtn.setEnabled(false);
					btnEliminar.setEnabled(false);
					equipoSeleccionado = null;
					String text = searchField.getText();
					if (text.equals("Buscar...") || text.isEmpty()) {
						loadAll(null);
					} else {
						loadAll(text);
					}
				}
			}
		});

		consultarBtn.setFont(new Font("Dialog", Font.BOLD, 12));
		consultarBtn.setEnabled(false);
		buttonPanel.add(consultarBtn);
		buttonPanel.add(modificarBtn);
		buttonPanel.add(registrarBtn);
		buttonPanel.add(btnEliminar);

		volverBtn = new JButton("Volver");
		volverBtn.setFont(boldFont);
		volverBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(volverBtn);
		getContentPane().add(mainPanel);

		loadAll(null);
		String miUser = DatabaseManager.tipoUserConectado;
		if (miUser != null) {
			if (!miUser.equals("Administrador")) {
				modificarBtn.setVisible(false);
				registrarBtn.setVisible(false);
				btnEliminar.setVisible(false);
			}
		}

	}

	public static void loadAll(String filtro) {
		if (model == null) {
			String[] columnNames = { "ID", "Equipo", "Entrenador", "Ciudad", "Cant. Juegos" };
			model = new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.setColumnIdentifiers(columnNames);
		}

		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		// Usando metodo para listar equipo en la base de datos
		ArrayList<Equipo> equipos = DatabaseManager.listarEquipo();
		// Equipo equipo = DatabaseManager.obtenerEquipoPorId("EQ-12");

		for (Equipo equipo : equipos) {
			if (filtro == null) {
				row[0] = equipo.getId();
				row[1] = equipo.getNombre();
				row[2] = equipo.getEntrenador();
				row[3] = equipo.getPais();
				row[4] = equipo.getPais();
				model.addRow(row);
			} else {
				if (equipo.getId().toLowerCase().contains(filtro.toLowerCase())
						|| equipo.getNombre().toLowerCase().contains(filtro.toLowerCase())
						|| equipo.getEntrenador().toLowerCase().contains(filtro.toLowerCase())
						|| equipo.getPais().toLowerCase().contains(filtro.toLowerCase())) {

					row[0] = equipo.getId();
					row[1] = equipo.getNombre();
					row[2] = equipo.getEntrenador();
					row[3] = equipo.getPais();
					row[4] = equipo.getJuegos().size();
					model.addRow(row);
				}
			}
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new ListadoEquipos().setVisible(true);
		});
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public void seleccionarEquipoJug(RegJugador ventana) {
		consultarBtn.setVisible(false);
		modificarBtn.setVisible(false);
		setTitle("Seleccionar Equipo");
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index != -1) {
					String equipoId = table.getValueAt(index, 0).toString();
					Equipo equipoSeleccionado = DatabaseManager.obtenerEquipoPorId(equipoId);
					ventana.setEquipoSeleccionado(equipoSeleccionado);
					dispose();
				}
			}
		});
	}
}
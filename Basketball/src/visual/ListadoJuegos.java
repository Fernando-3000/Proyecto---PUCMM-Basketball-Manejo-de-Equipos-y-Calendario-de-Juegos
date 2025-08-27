package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import logico.Equipo;
import logico.Juego;
import logico.Jugador;
import SQL.DatabaseManager;

public class ListadoJuegos extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel modeloTabla;
	private Object[] row;
	private Juego juegoSeleccionado = null;
	private JTextField searchField;
	private JPanel searchPanel;
	private JButton btnConsultar;
	private JButton btnGenerar;
	private JButton btnVolver;
	private String filtroEquipoOJugador;
	private TableRowSorter<DefaultTableModel> sorter;
	private PsimulacionJuego ventana;

	public ListadoJuegos(String filtro) {
		this.filtroEquipoOJugador = filtro;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setTitle("Listado de Juegos");
		setBounds(100, 100, 850, 520);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout());

		searchPanel = new JPanel(new BorderLayout());
		searchField = new JTextField("Buscar...");
		searchField.setPreferredSize(new Dimension(250, 35));
		searchField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		searchField.setEnabled(false);

		searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				filtrar();
			}

			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				filtrar();
			}

			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				filtrar();
			}

			private void filtrar() {
				String text = searchField.getText().trim();
				if (text.isEmpty() || text.equals("Buscar...")) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}
		});

		searchField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (searchField.getText().equals("Buscar...")) {
					searchField.setText("");
					searchField.setEnabled(true);
					juegoSeleccionado = null;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setText("Buscar...");
					searchField.setEnabled(false);
				}
			}
		});

		searchPanel.add(new JLabel("Buscar: "), BorderLayout.WEST);
		searchPanel.add(searchField, BorderLayout.CENTER);
		contentPanel.add(searchPanel, BorderLayout.NORTH);

		String[] columnas = { "ID", "Equipo de Casa", "Equipo de Visita", "Ganador" };
		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(modeloTabla);
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.setRowHeight(25);
		sorter = new TableRowSorter<>(modeloTabla);
		table.setRowSorter(sorter);
		// Centrar todo el texto de las celdas
		DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
		centerRenderer1.setHorizontalAlignment(JLabel.CENTER);

		for(int i = 0; i < table.getColumnCount(); i++) {
			// Si la columna es de imagen, usa otro renderer
			if (i != 4) {
				table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);
			}
		}
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index != -1) {
					String idStr = table.getValueAt(index, 0).toString();
					if (idStr.startsWith("JG-")) {
						idStr = idStr.substring(3); // "1"
					}
					try {
						int id = Integer.parseInt(idStr); // convertir a int
						juegoSeleccionado = DatabaseManager.obtenerJuegoPorId(id);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "ID de juego inválido: " + idStr, "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnConsultar = new JButton("Consultar");
		btnConsultar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnConsultar.addActionListener(e -> {
			if (juegoSeleccionado != null) {
				ConsultaJuego consulta = new ConsultaJuego(juegoSeleccionado);
				consulta.setModal(true);
				consulta.setVisible(true);
			}
		});
		btnConsultar.setVisible(false);
		buttonPane.add(btnConsultar);

		btnGenerar = new JButton("Generar");
		btnGenerar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnGenerar.addActionListener(e -> {
			ArrayList<Equipo> equipos = DatabaseManager.listarEquipo();
			if (equipos.size() < 5) {
				JOptionPane.showMessageDialog(this, "Se necesitan al menos 5 equipos para generar juegos.",
						"Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			ArrayList<Juego> nuevosJuegos = new ArrayList<>();
			for (int i = 0; i < equipos.size(); i++) {
				for (int j = 0; j < equipos.size(); j++) {
					if (i != j) {
						int id = DatabaseManager.obtenerProximoIdJuego();
						Juego juego = new Juego(id, equipos.get(i), equipos.get(j));
						nuevosJuegos.add(juego);
					}
				}
			}

			boolean todosGuardados = true;
			for (Juego juego : nuevosJuegos) {
				if (DatabaseManager.obtenerJuegoPorId(juego.getId()) == null) {
					boolean exito = DatabaseManager.registrarJuego(juego.getHome().getId(), juego.getAway().getId(),
							null);
					if (!exito)
						todosGuardados = false;
				}
			}

			if (todosGuardados) {
				JOptionPane.showMessageDialog(this, "Juegos generados correctamente.", "�xito",
						JOptionPane.INFORMATION_MESSAGE);
				btnGenerar.setVisible(false);
				btnConsultar.setVisible(true);
				loadAll();
			} else {
				JOptionPane.showMessageDialog(this, "Error al guardar algunos juegos.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		if (!DatabaseManager.listarJuegos().isEmpty()) {
			btnGenerar.setVisible(false);
			btnConsultar.setVisible(true);
		}

		if (filtro != null) {
			btnGenerar.setVisible(false);
			Equipo equ = DatabaseManager.obtenerEquipoPorId(filtro);
			Jugador jug = DatabaseManager.obtenerJugadorPorId(filtro);
			String nombre = equ != null ? equ.getNombre()
					: (jug != null ? jug.getNombre() + " " + jug.getApellido() : filtro);
			setTitle("Listado de Juegos | " + nombre);
		}

		btnVolver = new JButton("Volver");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnVolver.addActionListener(e -> dispose());
		buttonPane.add(btnVolver);

		if (filtro == null) {
			buttonPane.add(btnGenerar);
		}

		loadAll();
	}

	public void loadAll() {
		modeloTabla.setRowCount(0);
		row = new Object[4];

		ArrayList<Juego> juegos = (filtroEquipoOJugador == null) ? DatabaseManager.listarJuegos()
				: DatabaseManager.listarJuegosPorEquipo(filtroEquipoOJugador);

		for (Juego juego : juegos) {
			row[0] = "JG-" + juego.getId(); // solo para mostrar el id con prefijo
			row[1] = juego.getHome().getNombre();
			row[2] = juego.getAway().getNombre();
			row[3] = (juego.getGanador() == null || juego.getGanador().trim().isEmpty()) ? "Pendiente"
					: juego.getGanador();
			modeloTabla.addRow(row.clone());
		}
	}

	public void loadPendingGames() {
		modeloTabla.setRowCount(0);
		row = new Object[4];
		ArrayList<Juego> juegos = DatabaseManager.listarJuegos();

		for (Juego juego : juegos) {
			if (juego.getGanador() == null || juego.getGanador().trim().isEmpty()) {
				row[0] = juego.getId();
				row[1] = juego.getHome().getNombre();
				row[2] = juego.getAway().getNombre();
				row[3] = "Pendiente";
				modeloTabla.addRow(row.clone());
			}
		}
	}

	public void seleccionarJuego(PsimulacionJuego ventana) {
		this.ventana = ventana;
		btnConsultar.setVisible(false);
		btnGenerar.setVisible(false);
		setTitle("Seleccionar Juego");
		loadPendingGames();

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index != -1) {
					int id = Integer.parseInt(table.getValueAt(index, 0).toString().replace("JG-", ""));
					Juego juego = DatabaseManager.obtenerJuegoPorId(id);
					if (juego != null && (juego.getGanador() == null || juego.getGanador().trim().isEmpty())) {
						ventana.setJuegoSeleccionado(juego);
						dispose();
					}
				}
			}
		});
	}
}
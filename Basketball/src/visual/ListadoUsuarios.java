package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import logico.Jugador;
import logico.SerieNacional;
import logico.User;
import SQL.DatabaseManager;

public class ListadoUsuarios extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object[] row;
	private AbstractButton txtNombreUsuario;
	private AbstractButton txtTipoUsuario;
	private JButton btnRegistrar;
	private JButton btnEliminar;
	private JButton btnVolver;
	private JScrollPane scrollPane;
	private User userSeleccionado = null;
	private JTextField searchField;
	private JPanel searchPanel;

	public static void main(String[] args) {
		try {
			ListadoUsuarios dialog = new ListadoUsuarios();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListadoUsuarios() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setTitle("Listado de Usuarios");
		setBounds(100, 100, 611, 324);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		contentPanel.setLayout(new BorderLayout());

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
					loadAll();
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
					btnEliminar.setEnabled(false);
					userSeleccionado = null;
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
		contentPanel.add(searchPanel, BorderLayout.NORTH);

		String[] columnNames = { "Nombre de Usuario", "Tipo de Usuario" };

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
					String username = table.getValueAt(index, 0).toString();
					//Buscar el usuario por username y devuelve un objeto usuario
					userSeleccionado = DatabaseManager.buscarUsuario(username);
					btnEliminar.setEnabled(true);
				}
			}
		});
		scrollPane = new JScrollPane(table);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnVolver = new JButton("Volver");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setEnabled(false);
		btnEliminar.setFont(new Font("Tahoma", Font.BOLD, 13));
		buttonPane.add(btnEliminar);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (userSeleccionado != null) {
					// Eliminar usuario en la base de datos
					String nombre_Usuario = userSeleccionado.getUserName();
					if (DatabaseManager.eliminarUsuario(nombre_Usuario)) {
						OperacionExitosa operacion = new OperacionExitosa();
						operacion.setVisible(true);
						operacion.setModal(true);
						loadAll();
					} else {
						new OperacionEspecifica("No se pudo eliminar el usuario");
					}
				}
			}
		});

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 13));
		buttonPane.add(btnRegistrar);
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEliminar.setEnabled(true);
				RegUser usuario = new RegUser();
				usuario.setVisible(true);
				usuario.setModal(true);
				loadAll();
			}
		});
		buttonPane.add(btnVolver);
		loadAll();
	}

	public void loadAll() {
		loadAll(null);
	}

	public void loadAll(String filtro) {
		if (model == null) {
			String[] columnNames = { "Nombre de Usuario", "Tipo de Usuario" };
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

		for (User user : DatabaseManager.listarUsuarios()) {
			if (filtro == null) {
				row[0] = user.getUserName();
				row[1] = user.getTipo();
				model.addRow(row);
			} else {
				if (user.getUserName().toLowerCase().contains(filtro.toLowerCase())
						|| user.getTipo().toLowerCase().contains(filtro.toLowerCase())) {
					row[0] = user.getUserName();
					row[1] = user.getTipo();
					model.addRow(row);
				}
			}
		}
	}
}
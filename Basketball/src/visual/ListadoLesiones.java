package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import logico.Jugador;
import logico.Lesion;
import logico.SerieNacional;
import logico.User;
import SQL.DatabaseManager;

public class ListadoLesiones extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;
    private Lesion lesionSeleccionada = null;
    private JTextField searchField;
    private JButton volverBtn, modificarBtn, consultarBtn, registrarBtn;
    private JPanel mainPanel, searchPanel, buttonPanel;
    private JScrollPane scrollPane;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private TableRowSorter<DefaultTableModel> sorter;
    private Jugador jugadorFiltro;

    /**
     * Constructor: muestra el listado de lesiones
     * @param jugadorFiltro Si no es null, filtra por jugador
     */
    public ListadoLesiones(Jugador jugadorFiltro) {
        this.jugadorFiltro = jugadorFiltro;
        setModal(true);
        setResizable(false);
        setTitle(jugadorFiltro != null ? "Lesiones de " + jugadorFiltro.getNombre() : "Listado de Lesiones");
        setSize(900, 550);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // === Panel principal ===
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // === Barra de búsqueda ===
        searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField("Buscar...");
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        searchField.setEnabled(false);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrar(); }

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
                    lesionSeleccionada = null;
                    actualizarBotones();
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
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // === Tabla ===
        String[] columnas = {"ID Lesión", "Jugador", "Tipo", "Fecha Lesión", "Recuperación", "Estado"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === Selección de fila ===
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String id = table.getValueAt(row, 0).toString();
                    lesionSeleccionada = DatabaseManager.obtenerLesionPorId(id);
                    actualizarBotones();
                }
            }
        });

        // === Botones ===
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        Font btnFont = new Font("Tahoma", Font.BOLD, 13);

        registrarBtn = new JButton("Registrar");
        registrarBtn.setFont(btnFont);
        registrarBtn.addActionListener(e -> {
            if (jugadorFiltro != null) {
                lesionSeleccionada = null;
                actualizarBotones();
                RegLesion reg = new RegLesion(jugadorFiltro, null);
                reg.setVisible(true);
                reg.setModal(true);
                loadAll();
            }
        });
        buttonPanel.add(registrarBtn);

        modificarBtn = new JButton("Modificar");
        modificarBtn.setFont(btnFont);
        modificarBtn.setEnabled(false);
        modificarBtn.addActionListener(e -> {
            if (lesionSeleccionada != null) {
                RegLesion reg = new RegLesion(lesionSeleccionada.getJugador(), lesionSeleccionada);
                reg.setVisible(true);
                reg.setModal(true);
                lesionSeleccionada = null;
                actualizarBotones();
                loadAll();
            }
        });
        buttonPanel.add(modificarBtn);

        consultarBtn = new JButton("Consultar");
        consultarBtn.setFont(btnFont);
        consultarBtn.setEnabled(false);
        consultarBtn.addActionListener(e -> {
            if (lesionSeleccionada != null) {
                ConsultaLesion cons = new ConsultaLesion(lesionSeleccionada.getJugador(), lesionSeleccionada);
                cons.setVisible(true);
                cons.setModal(true);
            }
        });
        buttonPanel.add(consultarBtn);

        volverBtn = new JButton("Volver");
        volverBtn.setFont(btnFont);
        volverBtn.addActionListener(e -> dispose());
        buttonPanel.add(volverBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // === Cargar datos ===
        loadAll();

        // === Control de permisos ===
        User user = SerieNacional.getLoginUser();
        if (user != null && !user.getTipo().equals("Administrador")) {
            modificarBtn.setVisible(false);
            registrarBtn.setVisible(false);
        }

        // === Visibilidad del botón Registrar ===
        if (jugadorFiltro == null) {
            registrarBtn.setVisible(false);
        }
    }

    /**
     * Carga todas las lesiones desde la base de datos
     */
    public void loadAll() {
        model.setRowCount(0);
        Object[] row = new Object[6];

        ArrayList<Lesion> lesiones;
        if (jugadorFiltro != null) {
            lesiones = DatabaseManager.listarLesionesPorJugador(jugadorFiltro.getId());
        } else {
            lesiones = DatabaseManager.listarTodasLesiones();
        }

        for (Lesion l : lesiones) {
            row[0] = l.getId();
            row[1] = l.getJugador().getNombre() + " " + l.getJugador().getApellido();
            row[2] = l.getTipoDeLesion();
            row[3] = l.getFechaLes().format(formatter);
            row[4] = l.getFechaRecPrevista().format(formatter);
            row[5] = l.isEstado() ? "Activa" : "Recuperado";
            model.addRow(row);
        }
    }

    /**
     * Actualiza el estado de los botones según la selección
     */
    private void actualizarBotones() {
        boolean seleccionada = lesionSeleccionada != null;
        modificarBtn.setEnabled(seleccionada);
        consultarBtn.setEnabled(seleccionada);
    }
}
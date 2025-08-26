package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

import logico.Equipo;
import logico.Juego;
import logico.Jugador;
import logico.SerieNacional;
import SQL.DatabaseManager;

public class PsimulacionJuego extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private int tiempoR;
    private int tiempoP = 15 * 60;
    private int periodo = 1;
    private Timer temporizador;
    private boolean temporizadorEjecucion = false;

    private JButton inicioPausaBtn;
    private JLabel tiempoLabel;
    private JTextField txtIdJuego;
    private JLabel lblPeriodo;
    private JButton btnTapon, btnAsistencia, btnFalta, btnRobo, btn3pts, btn2pts, btn1pts, btnLesion;
    private JLabel labelEquipo, lblEquipo2, lblMarcador;

    private Juego juegoSeleccionado = null;
    private Jugador jugadorSeleccionado = null;

    private Equipo equipo1;
    private Equipo equipo2;

    private int puntajeEquipo1 = 0;
    private int puntajeEquipo2 = 0;

    private DefaultTableModel modelE1;
    private DefaultTableModel modelE2;
    private Object[] row;

    private JButton btnSeleccionar;
    private JPanel panelE1, panelE2;
    private JTable tableE1, tableE2;
    private JScrollPane scrollPaneE1, scrollPaneE2;
    private JLabel lblAcciones;

    public PsimulacionJuego() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarJuego();
                dispose();
            }
        });

        setTitle("Simulación de Juego");
        setResizable(false);
        setModal(true);
        setBounds(100, 100, 977, 650);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(255, 250, 250));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // === Componentes ===
        labelEquipo = new JLabel("Equipo1");
        labelEquipo.setFont(new Font("Tahoma", Font.PLAIN, 30));
        labelEquipo.setBounds(12, 31, 465, 43);
        contentPanel.add(labelEquipo);

        lblEquipo2 = new JLabel("Equipo2");
        lblEquipo2.setHorizontalAlignment(SwingConstants.RIGHT);
        lblEquipo2.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblEquipo2.setBounds(489, 31, 457, 43);
        contentPanel.add(lblEquipo2);

        txtIdJuego = new JTextField();
        txtIdJuego.setEditable(false);
        txtIdJuego.setBounds(415, 13, 129, 22);
        contentPanel.add(txtIdJuego);

        JLabel lblHome = new JLabel("HOME");
        lblHome.setBounds(12, 13, 46, 14);
        contentPanel.add(lblHome);

        JLabel lblAway = new JLabel("AWAY");
        lblAway.setHorizontalAlignment(SwingConstants.RIGHT);
        lblAway.setBounds(900, 14, 46, 14);
        contentPanel.add(lblAway);

        // Marcador
        JLabel lblMarcadorLabel = new JLabel("MARCADOR");
        lblMarcadorLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
        lblMarcadorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblMarcadorLabel.setBounds(386, 98, 194, 14);
        contentPanel.add(lblMarcadorLabel);

        lblMarcador = new JLabel("0 : 0");
        lblMarcador.setFont(new Font("Tahoma", Font.BOLD, 55));
        lblMarcador.setHorizontalAlignment(SwingConstants.CENTER);
        lblMarcador.setBounds(386, 111, 194, 62);
        contentPanel.add(lblMarcador);

        // Temporizador
        JLabel lblTiempo = new JLabel("TIEMPO");
        lblTiempo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
        lblTiempo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTiempo.setBounds(386, 394, 194, 14);
        contentPanel.add(lblTiempo);

        tiempoLabel = new JLabel("--:--");
        tiempoLabel.setFont(new Font("Tahoma", Font.BOLD, 55));
        tiempoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tiempoLabel.setBounds(386, 407, 194, 62);
        contentPanel.add(tiempoLabel);

        lblPeriodo = new JLabel("Periodo: 1");
        lblPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblPeriodo.setHorizontalAlignment(SwingConstants.CENTER);
        lblPeriodo.setBounds(415, 471, 129, 14);
        contentPanel.add(lblPeriodo);

        // Botón Iniciar/Pausar
        inicioPausaBtn = new JButton("Iniciar");
        inicioPausaBtn.setEnabled(false);
        inicioPausaBtn.setFont(new Font("Tahoma", Font.BOLD, 19));
        inicioPausaBtn.setBounds(401, 508, 168, 38);
        inicioPausaBtn.addActionListener(e -> {
            if (inicioPausaBtn.getText().equals("Finalizar")) {
                guardarJuego();
                dispose();
            } else {
                btnSeleccionar.setVisible(false);
                cargarJugadores(equipo1, equipo2, true);
                if (!temporizadorEjecucion) {
                    iniciarTemporizador();
                } else {
                    pausarTemporizador();
                }
            }
        });
        contentPanel.add(inicioPausaBtn);

        // Botón Seleccionar Juego
        btnSeleccionar = new JButton("Seleccionar juego");
        btnSeleccionar.setBounds(414, 41, 130, 23);
        btnSeleccionar.addActionListener(e -> {
            ListadoJuegos listado = new ListadoJuegos(null);
            listado.seleccionarJuego(this);
            listado.setVisible(true);
            listado.setModal(true);
            if (juegoSeleccionado != null) {
                txtIdJuego.setText(juegoSeleccionado.getId());
                cargarJugadores(juegoSeleccionado.getHome(), juegoSeleccionado.getAway(), false);
                inicioPausaBtn.setEnabled(true);
            }
        });
        contentPanel.add(btnSeleccionar);

        // Paneles de equipos
        panelE1 = crearPanelEquipo(12, 87, 346, 480);
        panelE2 = crearPanelEquipo(605, 87, 341, 480);

        // Panel de acciones
        JPanel panelAcciones = new JPanel();
        panelAcciones.setBackground(new Color(0, 0, 128));
        panelAcciones.setBounds(0, 205, 971, 156);
        panelAcciones.setLayout(null);
        contentPanel.add(panelAcciones);

        lblAcciones = new JLabel("ACCIONES");
        lblAcciones.setForeground(Color.WHITE);
        lblAcciones.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
        lblAcciones.setHorizontalAlignment(SwingConstants.CENTER);
        lblAcciones.setBounds(429, 13, 105, 14);
        panelAcciones.add(lblAcciones);

        // Botones de acciones
        btn3pts = crearBotonAccion("3pts", 371, 66, panelAcciones);
        btn2pts = crearBotonAccion("2pts", 371, 93, panelAcciones);
        btn1pts = crearBotonAccion("1pts", 371, 120, panelAcciones);
        btnRobo = crearBotonAccion("Robo", 371, 38, panelAcciones);
        btnTapon = crearBotonAccion("Tapón", 488, 38, panelAcciones);
        btnAsistencia = crearBotonAccion("Asistencia", 488, 65, panelAcciones);
        btnFalta = crearBotonAccion("Falta", 488, 92, panelAcciones);
        btnLesion = crearBotonAccion("Lesión", 488, 119, panelAcciones);

        // Modelos de tabla
        modelE1 = new DefaultTableModel(new String[]{"ID", "Nombre", "Posición"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelE2 = new DefaultTableModel(new String[]{"ID", "Nombre", "Posición"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel crearPanelEquipo(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(x, y, width, height);
        panel.setLayout(new BorderLayout());
        contentPanel.add(panel);

        JLabel title = new JLabel(" Jugadores:");
        title.setFont(new Font("Tahoma", Font.BOLD, 13));
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getComponent() == panel) deseleccionarJugador();
            }
        });

        if (panel == panelE1) scrollPaneE1 = scroll;
        else if (panel == panelE2) scrollPaneE2 = scroll;

        return panel;
    }

    private JButton crearBotonAccion(String text, int x, int y, JPanel parent) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 105, 23);
        btn.setEnabled(false);
        btn.addActionListener(e -> registrarAccion(text));
        parent.add(btn);
        return btn;
    }

    private void registrarAccion(String accion) {
        if (jugadorSeleccionado == null) return;

        switch (accion) {
            case "3pts":
                jugadorSeleccionado.getEstadisticas().setTriples(jugadorSeleccionado.getEstadisticas().getTriples() + 1);
                equipoDelJugador().getEstadistica().setTriples(equipoDelJugador().getEstadistica().getTriples() + 1);
                sumarPuntos(jugadorSeleccionado.getEquipo().getId().equals(equipo1.getId()) ? 3 : -3);
                break;
            case "2pts":
                jugadorSeleccionado.getEstadisticas().setDobles(jugadorSeleccionado.getEstadisticas().getDobles() + 1);
                equipoDelJugador().getEstadistica().setDobles(equipoDelJugador().getEstadistica().getDobles() + 1);
                sumarPuntos(jugadorSeleccionado.getEquipo().getId().equals(equipo1.getId()) ? 2 : -2);
                break;
            case "1pts":
                jugadorSeleccionado.getEstadisticas().setNormales(jugadorSeleccionado.getEstadisticas().getNormales() + 1);
                equipoDelJugador().getEstadistica().setNormales(equipoDelJugador().getEstadistica().getNormales() + 1);
                sumarPuntos(jugadorSeleccionado.getEquipo().getId().equals(equipo1.getId()) ? 1 : -1);
                break;
            case "Robo":
                jugadorSeleccionado.getEstadisticas().setRobos(jugadorSeleccionado.getEstadisticas().getRobos() + 1);
                break;
            case "Tapón":
                jugadorSeleccionado.getEstadisticas().setTapones(jugadorSeleccionado.getEstadisticas().getTapones() + 1);
                break;
            case "Asistencia":
                jugadorSeleccionado.getEstadisticas().setAsistencias(jugadorSeleccionado.getEstadisticas().intgetAsistencias() + 1);
                break;
            case "Falta":
                jugadorSeleccionado.getEstadisticas().setFaltas(jugadorSeleccionado.getEstadisticas().getFaltas() + 1);
                break;
            case "Lesión":
                RegLesion lesion = new RegLesion(jugadorSeleccionado, null);
                lesion.setVisible(true);
                lesion.setModal(true);
                DatabaseManager.obtenerJugadorPorId(jugadorSeleccionado.getId());
                cargarJugadores(equipo1, equipo2, true);
                break;
        }

        DatabaseManager.actualizarJugador(jugadorSeleccionado);
        DatabaseManager.actualizarEquipo(equipoDelJugador());
        deseleccionarJugador();
    }

    private Equipo equipoDelJugador() {
        return jugadorSeleccionado.getEquipo().getId().equals(equipo1.getId()) ? equipo1 : equipo2;
    }

    private void sumarPuntos(int puntos) {
        if (puntos > 0) puntajeEquipo1 += puntos;
        else puntajeEquipo2 -= puntos;
        actualizarMarcadorLabel();
    }

    private void deseleccionarJugador() {
        if (tableE1 != null) tableE1.clearSelection();
        if (tableE2 != null) tableE2.clearSelection();
        jugadorSeleccionado = null;
        habilitarBotones(false);
    }

    private void habilitarBotones(boolean habilitar) {
        btnTapon.setEnabled(habilitar);
        btnAsistencia.setEnabled(habilitar);
        btnFalta.setEnabled(habilitar);
        btnRobo.setEnabled(habilitar);
        btn3pts.setEnabled(habilitar);
        btn2pts.setEnabled(habilitar);
        btn1pts.setEnabled(habilitar);
        btnLesion.setEnabled(habilitar);
    }

    private void iniciarTemporizador() {
        if (inicioPausaBtn.getText().equals("Iniciar")) tiempoR = tiempoP;
        temporizadorEjecucion = true;
        inicioPausaBtn.setText("Pausar");

        temporizador = new Timer(1000, e -> {
            if (tiempoR > 0) {
                tiempoR--;
                actualizarTiempoLabel();
                verificarPeriodo();
            } else {
                mostrarMensajePeriodo();
                temporizador.stop();
                temporizadorEjecucion = false;
                inicioPausaBtn.setText("Finalizar");
            }
        });
        temporizador.start();
    }

    private void pausarTemporizador() {
        if (temporizador != null) temporizador.stop();
        temporizadorEjecucion = false;
        inicioPausaBtn.setText("Reanudar");
    }

    private void actualizarTiempoLabel() {
        tiempoLabel.setText(String.format("%02d:%02d", tiempoR / 60, tiempoR % 60));
    }

    private void actualizarMarcadorLabel() {
        lblMarcador.setText(puntajeEquipo1 + " : " + puntajeEquipo2);
    }

    private void verificarPeriodo() {
        if (tiempoR == 0 && periodo < 4) {
            mostrarMensajePeriodo();
        }
    }

    private void mostrarMensajePeriodo() {
        JOptionPane.showMessageDialog(this, "Fin del periodo " + periodo + ". ¡Reanuda para continuar!");
        periodo++;
        if (periodo <= 4) {
            tiempoR = tiempoP;
            lblPeriodo.setText("Periodo: " + periodo);
            inicioPausaBtn.setText("Reanudar");
        } else {
            inicioPausaBtn.setText("Finalizar");
        }
    }

    private void guardarJuego() {
        if (juegoSeleccionado == null || (puntajeEquipo1 == 0 && puntajeEquipo2 == 0)) return;

        juegoSeleccionado.setMarcadorCasa(puntajeEquipo1);
        juegoSeleccionado.setMarcadorAway(puntajeEquipo2);

        String ganador;
        if (puntajeEquipo1 > puntajeEquipo2) {
            ganador = equipo1.getNombre();
            equipo1.getEstadistica().setJuegosGanados(equipo1.getEstadistica().getJuegosGanados() + 1);
            equipo2.getEstadistica().setJuegosPerdidos(equipo2.getEstadistica().getJuegosPerdidos() + 1);
        } else if (puntajeEquipo2 > puntajeEquipo1) {
            ganador = equipo2.getNombre();
            equipo2.getEstadistica().setJuegosGanados(equipo2.getEstadistica().getJuegosGanados() + 1);
            equipo1.getEstadistica().setJuegosPerdidos(equipo1.getEstadistica().getJuegosPerdidos() + 1);
        } else {
            ganador = "Empate";
        }

        juegoSeleccionado.setGanador(ganador);
        DatabaseManager.actualizarJuego(juegoSeleccionado.getId(), ganador);
        DatabaseManager.actualizarEquipo(equipo1);
        DatabaseManager.actualizarEquipo(equipo2);

        for (Jugador j : equipo1.getJugadores()) {
            j.getJuegos().add(juegoSeleccionado);
            j.getEstadisticas().setCantJuegos(j.getEstadisticas().getCantJuegos() + 1);
            DatabaseManager.actualizarJugador(j);
        }
        for (Jugador j : equipo2.getJugadores()) {
            j.getJuegos().add(juegoSeleccionado);
            j.getEstadisticas().setCantJuegos(j.getEstadisticas().getCantJuegos() + 1);
            DatabaseManager.actualizarJugador(j);
        }

        JOptionPane.showMessageDialog(this, "Juego guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cargarJugadores(Equipo home, Equipo away, boolean habilitado) {
        this.equipo1 = DatabaseManager.obtenerEquipoPorId(home.getId());
        this.equipo2 = DatabaseManager.obtenerEquipoPorId(away.getId());

        labelEquipo.setText(equipo1.getNombre());
        lblEquipo2.setText(equipo2.getNombre());

        loadForGame(equipo1, modelE1);
        loadForGame(equipo2, modelE2);

        tableE1 = new JTable(modelE1);
        tableE1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneE1.setViewportView(tableE1);
        tableE1.setEnabled(habilitado);

        tableE2 = new JTable(modelE2);
        tableE2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneE2.setViewportView(tableE2);
        tableE2.setEnabled(habilitado);

        tableE1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tableE1.getSelectedRow();
                if (index != -1) {
                    tableE2.clearSelection();
                    String id = tableE1.getValueAt(index, 0).toString();
                    jugadorSeleccionado = DatabaseManager.obtenerJugadorPorId(id);
                    habilitarBotones(true);
                }
            }
        });

        tableE2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tableE2.getSelectedRow();
                if (index != -1) {
                    tableE1.clearSelection();
                    String id = tableE2.getValueAt(index, 0).toString();
                    jugadorSeleccionado = DatabaseManager.obtenerJugadorPorId(id);
                    habilitarBotones(true);
                }
            }
        });
    }

    public void loadForGame(Equipo aux, DefaultTableModel model) {
        model.setRowCount(0);
        row = new Object[3];
        ArrayList<Jugador> jugadores = aux.getJugadores();
        for (Jugador j : jugadores) {
            if (j.getEstadoSalud()) {
                row[0] = j.getId();
                row[1] = j.getNombre() + " " + j.getApellido();
                row[2] = j.getPosicion();
                model.addRow(row);
            }
        }
    }

    public Juego getJuegoSeleccionado() {
        return juegoSeleccionado;
    }

    public void setJuegoSeleccionado(Juego juego) {
        this.juegoSeleccionado = juego;
        if (juego != null) {
            txtIdJuego.setText(juego.getId());
            cargarJugadores(juego.getHome(), juego.getAway(), false);
        }
    }
}
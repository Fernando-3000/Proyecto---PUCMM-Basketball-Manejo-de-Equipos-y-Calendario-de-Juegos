package visual;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

import logico.SerieNacional;
import SQL.DatabaseManager;

public class PrincipalVisual extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JMenuBar menuBar;
    private JMenu mnEquipo;
    private JMenu mnJugador;
    private JMenu mnCalendario;
    private JMenu mnSimulacion;
    private JMenuItem mntmListadoEquipo;
    private JMenuItem mntmRegEquipo;
    private JMenuItem mntmListadoJugador;
    private JMenuItem mntmRegJugador;
    private JMenuItem mntmListadoJuegos;
    private JMenuItem mntmIniciarSimulacion;
    private JMenu mnUsuario;
    private JMenuItem mntmRegUsuario;
    private JMenuItem mntmListadoUsuario;
    private JMenu mnOtros;
    private JMenuItem mntmmRespaldo;
    private JMenuItem mntmmRefrescarEstadisticas;

    // Referencias a gráficas
    private GraficaBarra grafica;
    private GraficaEfectividad ge;
    private BarraWinrate gwin;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PrincipalVisual frame = new PrincipalVisual();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public PrincipalVisual() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DatabaseManager.cerrarConexion();
            }
        });

        setTitle("Serie Nacional de Basketball");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);
        setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnEquipo = new JMenu("  Equipos  ");
        menuBar.add(mnEquipo);

        mntmListadoEquipo = new JMenuItem("Listado");
        mntmListadoEquipo.addActionListener(e -> {
            ListadoEquipos listado = new ListadoEquipos();
            listado.setVisible(true);
            listado.setModal(true);
        });
        mnEquipo.add(mntmListadoEquipo);

        mntmRegEquipo = new JMenuItem("Registrar");
        mntmRegEquipo.addActionListener(e -> {
            RegEquipo registrar = new RegEquipo(null);
            registrar.setVisible(true);
            registrar.setModal(true);
        });
        mnEquipo.add(mntmRegEquipo);

        mnJugador = new JMenu("  Jugadores  ");
        menuBar.add(mnJugador);

        mntmListadoJugador = new JMenuItem("Listado");
        mntmListadoJugador.addActionListener(e -> {
            ListadoJugadores listado = new ListadoJugadores(null);
            listado.setVisible(true);
            listado.setModal(true);
        });
        mnJugador.add(mntmListadoJugador);

        mntmRegJugador = new JMenuItem("Registrar");
        mntmRegJugador.addActionListener(e -> {
            RegJugador registrar = new RegJugador(null);
            registrar.setVisible(true);
            registrar.setModal(true);
        });
        mnJugador.add(mntmRegJugador);

        mnCalendario = new JMenu("  Calendario de Juegos  ");
        menuBar.add(mnCalendario);

        mntmListadoJuegos = new JMenuItem("Listado");
        mntmListadoJuegos.addActionListener(e -> {
            ListadoJuegos listado = new ListadoJuegos(null);
            listado.setVisible(true);
            listado.setModal(true);
        });
        mnCalendario.add(mntmListadoJuegos);

        mnSimulacion = new JMenu("  Simulación de Juego  ");
        menuBar.add(mnSimulacion);

        mntmIniciarSimulacion = new JMenuItem("Iniciar");
        mntmIniciarSimulacion.addActionListener(e -> {
            PsimulacionJuego simulacion = new PsimulacionJuego();
            simulacion.setVisible(true);
            simulacion.setModal(true);
        });
        mnSimulacion.add(mntmIniciarSimulacion);

        mnUsuario = new JMenu("  Usuarios  ");
        menuBar.add(mnUsuario);

        mntmListadoUsuario = new JMenuItem("Listado");
        mntmListadoUsuario.addActionListener(e -> {
            ListadoUsuarios listado = new ListadoUsuarios();
            listado.setVisible(true);
            listado.setModal(true);
        });
        mnUsuario.add(mntmListadoUsuario);

        mntmRegUsuario = new JMenuItem("Registrar");
        mntmRegUsuario.addActionListener(e -> {
            RegUser usuario = new RegUser(null);
            usuario.setVisible(true);
            usuario.setModal(true);
        });
        mnUsuario.add(mntmRegUsuario);

        mnOtros = new JMenu("  Otros  ");
        menuBar.add(mnOtros);

        mntmmRefrescarEstadisticas = new JMenuItem("Refrescar Estadísticas");
        mntmmRefrescarEstadisticas.addActionListener(e -> refrescarGraficas());
        mnOtros.add(mntmmRefrescarEstadisticas);

        mntmmRespaldo = new JMenuItem("Respaldo");
        mnOtros.add(mntmmRespaldo);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        refrescarGraficas();

        // Limitar menú según tipo de usuario
        if (DatabaseManager.tipoUserConectado == null || !DatabaseManager.tipoUserConectado.equals("Administrador")) {
            mnUsuario.setVisible(false);
            mntmRegEquipo.setVisible(false);
            mntmRegJugador.setVisible(false);
            mntmRegUsuario.setVisible(false);
            mntmListadoUsuario.setVisible(false);
        }
    }

    /**
     * Refresca las gráficas de estadísticas
     */
    private void refrescarGraficas() {
        contentPane.removeAll();
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        grafica = new GraficaBarra();
        grafica.setBounds(226, 16, 453, 429);
        panel.add(grafica);

        ge = new GraficaEfectividad();
        ge.setBounds(702, 13, 538, 429);
        panel.add(ge);

        gwin = new BarraWinrate();
        gwin.setBounds(12, 458, 1228, 150);
        panel.add(gwin);

        panel.revalidate();
        panel.repaint();
        contentPane.revalidate();
        contentPane.repaint();
    }
}
package visual;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Component;

import logico.Equipo;
import logico.Juego;
import logico.Jugador;
import logico.SerieNacional;
import java.util.ArrayList;
import javax.swing.ScrollPaneConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PsimulacionJuego extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private int tiempoR;
	private int tiempoP = 15*60;
	private int periodo = 1;
	private Timer temporizador;
	private boolean TemporizadorEjecucion = false;

	private JButton InicioPausaBtn;
	private JLabel TiempoLabel;
	private JTextField txtIdJuego;
	private JLabel lblPeriodo;
	private JButton btnTapon;
	private JButton btnAsistencia;
	private JButton btnFalta;
	private JButton btnRobo;
	private JButton Btn3pts;
	private JButton Btn2pts;
	private JButton Btn1pts;
	private JLabel labelEquipo;
	private JLabel lblEquipo2;
	private JLabel lblMarcador;
	
	private Juego juegoSeleccionado = null;
	private Jugador jugadorSeleccionado = null;
	private boolean seleccionDesdeE1 = false;

	private Equipo equipo1;
	private Equipo equipo2;
	
	private int puntajeEquipo1 = 0;
	private int puntajeEquipo2 = 0;
	
	private static DefaultTableModel modelE1;
	private static DefaultTableModel modelE2;
	private static Object[] row;
	private JButton btnSeleccionar;
	private JPanel panelE1;
	private JPanel panelE2;
	private JTable tableE1;
	private JTable tableE2;
	private JScrollPane scrollPaneE1;
	private JScrollPane scrollPaneE2;
	private JLabel lblAcciones;
	private JButton btnLesion;

	public static void main(String[] args) {
		try {
			PsimulacionJuego dialog = new PsimulacionJuego();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PsimulacionJuego() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (juegoSeleccionado != null && (puntajeEquipo1 > 0 || puntajeEquipo2 > 0)) {
					String ganador;
					
					juegoSeleccionado.setMarcadorCasa(puntajeEquipo1);
					juegoSeleccionado.setMarcadorAway(puntajeEquipo2);
					
					if (juegoSeleccionado.getMarcadorCasa() > juegoSeleccionado.getMarcadorAway())
					{
						ganador = juegoSeleccionado.getHome().getId();
					}
					else
					{
						if (juegoSeleccionado.getMarcadorCasa() > juegoSeleccionado.getMarcadorCasa())
						{
							ganador = juegoSeleccionado.getAway().getId();
						}
						else
						{
							ganador = "Empate";
						}
					}
					
					juegoSeleccionado.setGanador(ganador);
					SerieNacional.getInstance().modificarJuego(juegoSeleccionado);
					
					Juego jue = SerieNacional.getInstance().searchJuegoById(juegoSeleccionado.getId(),
																		    SerieNacional.getInstance().getMisJuegos());
					OperacionEspecifica operacion = new OperacionEspecifica("Juego guardado.");
					Equipo equ1 = SerieNacional.getInstance().searchEquipoById(juegoSeleccionado.getHome().getId(), SerieNacional.getInstance().getMisEquipos());
					Equipo equ2 = SerieNacional.getInstance().searchEquipoById(juegoSeleccionado.getAway().getId(), SerieNacional.getInstance().getMisEquipos());
					
					equ1.getJuegos().add(jue);
					for (Jugador jugI: equ1.getJugadores())
					{
						jugI.getJuegos().add(jue);
						SerieNacional.getInstance().modificarJugador(jugI);
					}
					
					equ2.getJuegos().add(jue);
					for (Jugador jugI: equ2.getJugadores())
					{
						jugI.getJuegos().add(jue);
						SerieNacional.getInstance().modificarJugador(jugI);
					}
					
					SerieNacional.getInstance().modificarEquipo(equ1);
					SerieNacional.getInstance().modificarEquipo(equ2);		
					
					operacion.setVisible(true);
					operacion.setModal(true);
				}
			}
		});
		setTitle("Simulaci\u00F3n de juego");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 977, 626);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 250));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		// Añadir MouseListener al panel principal para detectar clics fuera de las tablas
		contentPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Deseleccionar jugador solo si el clic fue directamente en el contentPanel
				if (e.getComponent() == contentPanel) {
					deseleccionarJugador();
				}
			}
		});

		labelEquipo = new JLabel("Equipo1");
		labelEquipo.setBounds(12, 31, 465, 43);
		labelEquipo.setFont(new Font("Tahoma", Font.PLAIN, 30));
		contentPanel.add(labelEquipo);

		lblEquipo2 = new JLabel("Equipo2");
		lblEquipo2.setBounds(489, 31, 457, 43);
		lblEquipo2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEquipo2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		contentPanel.add(lblEquipo2);

		JLabel lblPuntuacion1 = new JLabel("TIEMPO");
		lblPuntuacion1.setBounds(386, 394, 194, 14);
		lblPuntuacion1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPuntuacion1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		contentPanel.add(lblPuntuacion1);

		TiempoLabel = new JLabel("--:--");
		TiempoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		TiempoLabel.setBounds(386, 407, 194, 62);
		TiempoLabel.setFont(new Font("Tahoma", Font.BOLD, 55));
		contentPanel.add(TiempoLabel);

		lblPeriodo = new JLabel("Periodo: 1");
		lblPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPeriodo.setBounds(415, 471, 129, 14);
		lblPeriodo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblPeriodo);

		JLabel lblNewLabel_3 = new JLabel("HOME");
		lblNewLabel_3.setBounds(12, 13, 46, 14);
		contentPanel.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("AWAY");
		lblNewLabel_4.setBounds(900, 14, 46, 14);
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPanel.add(lblNewLabel_4);

		InicioPausaBtn = new JButton("Iniciar");
		InicioPausaBtn.setEnabled(false);
		InicioPausaBtn.setBounds(401, 508, 168, 38);
		InicioPausaBtn.setFont(new Font("Tahoma", Font.BOLD, 19));
		InicioPausaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (InicioPausaBtn.getText().equals("Finalizar"))
				{	
					String ganador;
					
					juegoSeleccionado.setMarcadorCasa(puntajeEquipo1);
					juegoSeleccionado.setMarcadorAway(puntajeEquipo2);
					
					if (juegoSeleccionado.getMarcadorCasa() > juegoSeleccionado.getMarcadorAway())
					{
						ganador = juegoSeleccionado.getHome().getId();
					}
					else
					{
						if (juegoSeleccionado.getMarcadorCasa() > juegoSeleccionado.getMarcadorCasa())
						{
							ganador = juegoSeleccionado.getAway().getId();
						}
						else
						{
							ganador = "Empate";
						}
					}
					
					juegoSeleccionado.setGanador(ganador);
					SerieNacional.getInstance().modificarJuego(juegoSeleccionado);
					
					Juego jue = SerieNacional.getInstance().searchJuegoById(juegoSeleccionado.getId(),
																		    SerieNacional.getInstance().getMisJuegos());
					OperacionEspecifica operacion = new OperacionEspecifica("Juego guardado.");
					Equipo equ1 = SerieNacional.getInstance().searchEquipoById(juegoSeleccionado.getHome().getId(), SerieNacional.getInstance().getMisEquipos());
					Equipo equ2 = SerieNacional.getInstance().searchEquipoById(juegoSeleccionado.getAway().getId(), SerieNacional.getInstance().getMisEquipos());
					
					equ1.getJuegos().add(jue);
					for (Jugador jugI: equ1.getJugadores())
					{
						jugI.getJuegos().add(jue);
						SerieNacional.getInstance().modificarJugador(jugI);
					}
					
					equ2.getJuegos().add(jue);
					for (Jugador jugI: equ2.getJugadores())
					{
						jugI.getJuegos().add(jue);
						SerieNacional.getInstance().modificarJugador(jugI);
					}
					
					SerieNacional.getInstance().modificarEquipo(equ1);
					SerieNacional.getInstance().modificarEquipo(equ2);		
					
					operacion.setVisible(true);
					operacion.setModal(true);
					dispose();
				}
				else
				{
					btnSeleccionar.setVisible(false);
					cargarJugadores(juegoSeleccionado.getHome(), juegoSeleccionado.getAway(), true);
					if (!TemporizadorEjecucion) {
						iniciarTemporizador();
					} else {
						pausarTemporizador();
					}
				}
			}
		});
		contentPanel.add(InicioPausaBtn);
		
		btnSeleccionar = new JButton("Selecionar juego");
		btnSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ListadoJuegos listado = new ListadoJuegos(null);
				listado.seleccionarJuego(PsimulacionJuego.this);
				listado.setVisible(true);
				listado.setModal(true);
				
				if (juegoSeleccionado != null)
					cargarJugadores(juegoSeleccionado.getHome(), juegoSeleccionado.getAway(), false);
				
				InicioPausaBtn.setEnabled(true);
			}
		});
		btnSeleccionar.setBounds(414, 41, 130, 23);
		contentPanel.add(btnSeleccionar);
		
		txtIdJuego = new JTextField();
		txtIdJuego.setEditable(false);
		txtIdJuego.setBounds(415, 13, 129, 22);
		txtIdJuego.setColumns(10);
		contentPanel.add(txtIdJuego);

		JLabel label_1 = new JLabel("MARCADOR");
		label_1.setBounds(386, 98, 194, 14);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		contentPanel.add(label_1);
		
		lblMarcador = new JLabel("0 : 0");
		lblMarcador.setHorizontalAlignment(SwingConstants.CENTER);
		lblMarcador.setFont(new Font("Tahoma", Font.BOLD, 55));
		lblMarcador.setBounds(386, 111, 194, 62);
		contentPanel.add(lblMarcador);
		
		panelE1 = new JPanel();
		panelE1.setBackground(Color.WHITE);
		panelE1.setBounds(12, 87, 346, 480);
		panelE1.setLayout(new BorderLayout());
		contentPanel.add(panelE1);
		
		// Añadir MouseListener al panel del equipo 1 para detectar clics fuera de la tabla
		panelE1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Solo deseleccionar si el clic fue en el panel pero no en la tabla
				if (e.getComponent() == panelE1) {
					deseleccionarJugador();
				}
			}
		});
		
		panelE2 = new JPanel();
		panelE2.setBackground(Color.WHITE);
		panelE2.setBounds(605, 87, 341, 480);
		panelE2.setLayout(new BorderLayout());
		contentPanel.add(panelE2);
		
		// Añadir MouseListener al panel del equipo 2 para detectar clics fuera de la tabla
		panelE2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Solo deseleccionar si el clic fue en el panel pero no en la tabla
				if (e.getComponent() == panelE2) {
					deseleccionarJugador();
				}
			}
		});
		
		String[] columnNames = {"ID", " # ", "Nombre"};
		
		JLabel labelTituloEquipo1 = new JLabel(" Jugadores:");
		labelTituloEquipo1.setBackground(new Color(248, 248, 255));
		labelTituloEquipo1.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelTituloEquipo1.setHorizontalAlignment(SwingConstants.LEFT);
		panelE1.add(labelTituloEquipo1, BorderLayout.NORTH);
		
		scrollPaneE1 = new JScrollPane();
		scrollPaneE1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelE1.add(scrollPaneE1, BorderLayout.CENTER);
				
		JLabel labelTituloEquipo2 = new JLabel(" Jugadores:");
		labelTituloEquipo2.setBackground(new Color(248, 248, 255));
		labelTituloEquipo2.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelTituloEquipo2.setHorizontalAlignment(SwingConstants.LEFT);
		panelE2.add(labelTituloEquipo2, BorderLayout.NORTH);
		
		scrollPaneE2 = new JScrollPane();
		scrollPaneE2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelE2.add(scrollPaneE2, BorderLayout.CENTER);
				
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 0, 128));
		panel_2.setBounds(0, 205, 971, 156);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		// Añadir MouseListener al panel de botones para detectar clics
		panel_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Solo deseleccionar si el clic fue en el panel pero no en un botón
				if (e.getComponent() == panel_2) {
					deseleccionarJugador();
				}
			}
		});
		
		Btn3pts = new JButton("3pts");
		Btn3pts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jugadorSeleccionado != null)		
				{				
					jugadorSeleccionado.getEstadisticas().setTriples(jugadorSeleccionado.getEstadisticas().getTriples() + 1);
					Equipo equ = SerieNacional.getInstance().searchEquipoById(jugadorSeleccionado.getEquipo().getId(),
																			  SerieNacional.getInstance().getMisEquipos());
					
					equ.getEstadistica().setTriples(equ.getEstadistica().getTriples() + 1);
					
					SerieNacional.getInstance().modificarEquipo(equ);
					SerieNacional.getInstance().modificarJugador(jugadorSeleccionado);
					
					if (equ.getId().equals(equipo1.getId()))
					{
						puntajeEquipo1 += 3;
					}
					else
					{
						if (equ.getId().equals(equipo2.getId()))
							puntajeEquipo2 += 3;
					}
					actualizarMarcadorLabel();
					habilitarBotones(false);
					tableE2.clearSelection();
					tableE1.clearSelection();
				}
			}
		});
		Btn3pts.setEnabled(false);
		Btn3pts.setBounds(371, 66, 105, 23);
		panel_2.add(Btn3pts);
		
		btnTapon = new JButton("Tap\u00F3n");
		btnTapon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jugadorSeleccionado != null)		
				{				
					jugadorSeleccionado.getEstadisticas().setTapones(jugadorSeleccionado.getEstadisticas().getTapones() + 1);
					SerieNacional.getInstance().modificarJugador(jugadorSeleccionado);
					habilitarBotones(false);
					tableE2.clearSelection();
					tableE1.clearSelection();
				}
			}
		});
		btnTapon.setEnabled(false);
		btnTapon.setBounds(488, 38, 108, 23);
		panel_2.add(btnTapon);
		
		Btn2pts = new JButton("2pts");
		Btn2pts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jugadorSeleccionado != null)		
				{				
					jugadorSeleccionado.getEstadisticas().setDobles(jugadorSeleccionado.getEstadisticas().getDobles() + 1);
					Equipo equ = SerieNacional.getInstance().searchEquipoById(jugadorSeleccionado.getEquipo().getId(),
																			  SerieNacional.getInstance().getMisEquipos());
					
					equ.getEstadistica().setDobles(equ.getEstadistica().getDobles() + 1);
					
					SerieNacional.getInstance().modificarEquipo(equ);
					SerieNacional.getInstance().modificarJugador(jugadorSeleccionado);
					
					if (equ.getId().equals(equipo1.getId()))
					{
						puntajeEquipo1 += 2;
					}
					else
					{
						if (equ.getId().equals(equipo2.getId()))
							puntajeEquipo2 += 2;
					}
					actualizarMarcadorLabel();
					habilitarBotones(false);
					tableE2.clearSelection();
					tableE1.clearSelection();
				}
			}
		});
		Btn2pts.setEnabled(false);
		Btn2pts.setBounds(371, 93, 105, 23);
		panel_2.add(Btn2pts);
		
		btnAsistencia = new JButton("Asistencia");
		btnAsistencia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jugadorSeleccionado != null)		
				{				
					jugadorSeleccionado.getEstadisticas().setAsistencias(jugadorSeleccionado.getEstadisticas().getAsistencias() + 1);
					SerieNacional.getInstance().modificarJugador(jugadorSeleccionado);
					habilitarBotones(false);
					tableE2.clearSelection();
					tableE1.clearSelection();
				}
			}
		});
		btnAsistencia.setEnabled(false);
		btnAsistencia.setBounds(488, 65, 108, 23);
		panel_2.add(btnAsistencia);
		
		Btn1pts = new JButton("1pts");
		Btn1pts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jugadorSeleccionado != null)		
				{				
					jugadorSeleccionado.getEstadisticas().setNormales(jugadorSeleccionado.getEstadisticas().getNormales() + 1);
					Equipo equ = SerieNacional.getInstance().searchEquipoById(jugadorSeleccionado.getEquipo().getId(),
																			  SerieNacional.getInstance().getMisEquipos());
					
					equ.getEstadistica().setNormales(equ.getEstadistica().getNormales() + 1);
					
					SerieNacional.getInstance().modificarEquipo(equ);
					SerieNacional.getInstance().modificarJugador(jugadorSeleccionado);
					
					if (equ.getId().equals(equipo1.getId()))
					{
						puntajeEquipo1++;
					}
					else
					{
						if (equ.getId().equals(equipo2.getId()))
							puntajeEquipo2++;
					}
					actualizarMarcadorLabel();
					habilitarBotones(false);
					tableE2.clearSelection();
					tableE1.clearSelection();
				}
			}
		});
		Btn1pts.setEnabled(false);
		Btn1pts.setBounds(371, 120, 105, 23);
		panel_2.add(Btn1pts);
		
		btnFalta = new JButton("Falta");
		btnFalta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jugadorSeleccionado != null)		
				{				
					jugadorSeleccionado.getEstadisticas().setFaltas(jugadorSeleccionado.getEstadisticas().getFaltas() + 1);
					SerieNacional.getInstance().modificarJugador(jugadorSeleccionado);
					habilitarBotones(false);
					tableE2.clearSelection();
					tableE1.clearSelection();
				}
			}
		});
		btnFalta.setEnabled(false);
		btnFalta.setBounds(488, 92, 108, 23);
		panel_2.add(btnFalta);
		
		btnRobo = new JButton("Robo");
		btnRobo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jugadorSeleccionado != null)		
				{				
					jugadorSeleccionado.getEstadisticas().setRobos(jugadorSeleccionado.getEstadisticas().getRobos() + 1);
					SerieNacional.getInstance().modificarJugador(jugadorSeleccionado);
					habilitarBotones(false);
					tableE2.clearSelection();
					tableE1.clearSelection();
				}
			}
		});
		btnRobo.setEnabled(false);
		btnRobo.setBounds(371, 38, 105, 23);
		panel_2.add(btnRobo);
		
		lblAcciones = new JLabel("ACCIONES");
		lblAcciones.setForeground(new Color(255, 255, 255));
		lblAcciones.setHorizontalAlignment(SwingConstants.CENTER);
		lblAcciones.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblAcciones.setBounds(429, 13, 105, 14);
		panel_2.add(lblAcciones);
		
		btnLesion = new JButton("Lesi\u00F3n");
		btnLesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Jugador jug = SerieNacional.getInstance().searchJugadorById(jugadorSeleccionado.getId(),
						  												   	SerieNacional.getInstance().getMisJugadores());
				RegLesion lesion = new RegLesion(jug, null);
				lesion.setVisible(true);
				lesion.setModal(true);
				
				SerieNacional.getInstance().modificarJugador(jug);
				cargarJugadores(equipo1, equipo2, true);
				habilitarBotones(false);
			}
		});
		btnLesion.setEnabled(false);
		btnLesion.setBounds(488, 119, 108, 23);
		panel_2.add(btnLesion);
		
		// Inicializar modelos de tabla
		if (modelE1 == null) {
			String[] columnsE1 = {"ID", " # ", "Nombre"};
			modelE1 = new DefaultTableModel(columnsE1, 0) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		
		if (modelE2 == null) {
			String[] columnsE2 = {"ID", " # ", "Nombre"};
			modelE2 = new DefaultTableModel(columnsE2, 0) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
	}
	
	// Método para deseleccionar el jugador
	private void deseleccionarJugador() {
		// Limpiar selecciones de ambas tablas
		if (tableE1 != null) {
			tableE1.clearSelection();
		}
		if (tableE2 != null) {
			tableE2.clearSelection();
		}
		
		// Resetear el jugador seleccionado
		jugadorSeleccionado = null;
		
		// Desactivar todos los botones
		habilitarBotones(false);
	}
	
	// Método para habilitar o deshabilitar botones de acción
	private void habilitarBotones(boolean habilitar) {
		btnTapon.setEnabled(habilitar);
		btnAsistencia.setEnabled(habilitar);
		btnFalta.setEnabled(habilitar);
		btnRobo.setEnabled(habilitar);
		Btn3pts.setEnabled(habilitar);
		Btn2pts.setEnabled(habilitar);
		Btn1pts.setEnabled(habilitar);
		btnLesion.setEnabled(habilitar);
	}
		
	private void iniciarTemporizador() {
		if (InicioPausaBtn.getText().equals("Iniciar"))
			tiempoR = tiempoP;
		
		TemporizadorEjecucion = true;
		InicioPausaBtn.setText("Pausar");

		temporizador = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tiempoR > 0) {
					tiempoR--;
					actualizarTiempoLabel();
					verificarPeriodo();
				} else {
					mostrarMensajePeriodo();
					temporizador.stop();
					TemporizadorEjecucion = false;
					InicioPausaBtn.setText("Finalizar");
				}
			}
		});
		temporizador.start();
	}

	private void pausarTemporizador() {
		temporizador.stop();
		TemporizadorEjecucion = false;
		InicioPausaBtn.setText("Reanudar");
	}

	private void actualizarTiempoLabel() {
		int minutos = tiempoR / 60;
		int segundos = tiempoR % 60;
		TiempoLabel.setText(String.format("%02d:%02d", minutos, segundos));
	}
	
	private void actualizarMarcadorLabel() {
		lblMarcador.setText(String.format("%02d:%02d", puntajeEquipo1, puntajeEquipo2));
	}

	private void verificarPeriodo() {
		if (tiempoR == 0 && periodo <= 4) {
			mostrarMensajePeriodo();
		}
	}

	private void mostrarMensajePeriodo() {
		periodo++;
		if (periodo <= 4) {
			tiempoR = tiempoP;
			lblPeriodo.setText("Periodo: " + String.valueOf(periodo));
		} else {
			temporizador.stop();
			TemporizadorEjecucion = false;
			InicioPausaBtn.setText("Finalizar");
		}
	}

	public void cargarJugadores(Equipo home, Equipo away, boolean habilitado) {
		this.equipo1 = SerieNacional.getInstance().searchEquipoById(home.getId(), SerieNacional.getInstance().getMisEquipos());
		this.equipo2 = SerieNacional.getInstance().searchEquipoById(away.getId(), SerieNacional.getInstance().getMisEquipos());
		
		if (home != null) {
			labelEquipo.setText(home.getNombre());
		}
		
		if (away != null) {
			lblEquipo2.setText(away.getNombre());
		}

		if (modelE1 == null) {
	        String[] columnNames = {"ID", " # ", "Nombre"};
	        modelE1 = new DefaultTableModel(columnNames, 0) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false;
	            }
	        };
	    }
	    
	    if (modelE2 == null) {
	        String[] columnNames = {"ID", " # ", "Nombre"};
	        modelE2 = new DefaultTableModel(columnNames, 0) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false;
	            }
	        };
	    }
		
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
                if(index != -1) {
                    // Deseleccionar la otra tabla
                    if (tableE2 != null && tableE2.getSelectionModel().getMinSelectionIndex() != -1) {
                        tableE2.clearSelection();
                    }
                    
                    String jugId = tableE1.getValueAt(index, 0).toString();
                    jugadorSeleccionado = SerieNacional.getInstance().searchJugadorById(jugId, 
                                             SerieNacional.getInstance().getMisJugadores());
                    seleccionDesdeE1 = true;
                    
                    // Habilitar botones para este jugador
                    habilitarBotones(true);
                }
	        }
	    });
		
		tableE2.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
                int index = tableE2.getSelectedRow();
                if(index != -1) {
                    // Deseleccionar la otra tabla
                    if (tableE1 != null && tableE1.getSelectionModel().getMinSelectionIndex() != -1) {
                        tableE1.clearSelection();
                    }
                    
                    String jugId = tableE2.getValueAt(index, 0).toString();
                    jugadorSeleccionado = SerieNacional.getInstance().searchJugadorById(jugId, 
                                             SerieNacional.getInstance().getMisJugadores());
                    seleccionDesdeE1 = false;
                    
                    // Habilitar botones para este jugador
                    habilitarBotones(true);
                }
	        }
	    });
	}

	public Juego getJuegoSeleccionado() {
		return juegoSeleccionado;
	}

	public void setJuegoSeleccionado(Juego juegoSeleccionado) {
		this.juegoSeleccionado = juegoSeleccionado;
		if (juegoSeleccionado != null)
		{
			txtIdJuego.setText(juegoSeleccionado.getId());
			cargarJugadores(juegoSeleccionado.getHome(), juegoSeleccionado.getAway(), false);
		}
	}
	
	public void loadForGame(Equipo aux, DefaultTableModel model) {
     	
    	if (model == null) {
    		String[] columnNames = {"ID", "Nombre", " # "};
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
        ArrayList<Jugador> jugadores;
        
        if (aux == null)
        	jugadores = SerieNacional.getInstance().getMisJugadores();
        else
        	jugadores = aux.getJugadores();
        
        for(Jugador jugador : jugadores) {
            if(jugador.getEstadoSalud()) {
                row[0] = jugador.getId();
                row[1] = jugador.getNombre().charAt(0) + ". " + jugador.getApellido();
                row[2] = jugador.getPosicion();
                model.addRow(row);
            }
        }
    }
}
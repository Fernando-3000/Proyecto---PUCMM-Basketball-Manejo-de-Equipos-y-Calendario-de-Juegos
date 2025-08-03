package visual;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import logico.Equipo;
import logico.Juego;
import logico.Jugador;
import logico.SerieNacional;

public class ListadoJuegos extends JDialog {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private static DefaultTableModel modeloTabla;
    private static Object[] row;
    private Juego juegoSeleccionado = null;
    private JTextField searchField;
    private JPanel searchPanel;
    private JButton btnConsultar;
    private JButton btnGenerar;
    private JButton btnVolver;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ListadoJuegos dialog = new ListadoJuegos(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create the dialog.
     */
    public ListadoJuegos(String filtro) {
    	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setTitle("Listado de Juegos");
        setBounds(100, 100, 800, 500);  
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());
        
        searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField("Buscar...");
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setEnabled(false);
        
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent arg0) {
                if (searchField.getText().equals("Buscar...")) {
                    searchField.setEnabled(true);
                    searchField.setText("");
                    juegoSeleccionado = null;
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
        
        searchPanel.add(new JLabel("Barra de búsqueda:   "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        
        String[] columnas = {"ID", "Equipo de Casa", "Equipo de Visita", "Ganador"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
         
        table = new JTable(modeloTabla);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if(index != -1) {
                    String juegoId = table.getValueAt(index, 0).toString();
                    juegoSeleccionado = SerieNacional.getInstance().searchJuegoById(juegoId, SerieNacional.getInstance().getMisJuegos());
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
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (juegoSeleccionado != null) {
                	ConsultaJuego consultaJuego = new ConsultaJuego(juegoSeleccionado);
                    consultaJuego.setVisible(true);
                	consultaJuego.setModal(true);
                }
            }
        });
        btnConsultar.setVisible(false);
        
        btnGenerar = new JButton("Generar");
        btnGenerar.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnGenerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
	            
            	if(SerieNacional.getInstance().getMisEquipos().size() >= 5)
            	{
	            		SerieNacional.getInstance().generarJuegos();
	            		loadAll(filtro);
	            		btnConsultar.setVisible(true);
	            		btnGenerar.setVisible(false);
            	}
            }
        });
        
        if (filtro != null)
        {
        	String str = "";
        	btnGenerar.setVisible(false);
        	setTitle("Listado de Juegos");
        	Equipo equ = SerieNacional.getInstance().searchEquipoById(filtro, SerieNacional.getInstance().getMisEquipos());
            Jugador jug = SerieNacional.getInstance().searchJugadorById(filtro, SerieNacional.getInstance().getMisJugadores());

             if (equ != null)
            	 str = equ.getNombre();
             
             if (jug != null)
            	 str = jug.getNombre()+jug.getApellido();
             
             setTitle("Listado de Juegos | " + str);
        }
        
        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();    
            }
        });
        
        buttonPane.add(btnConsultar);
        buttonPane.add(btnGenerar);
        buttonPane.add(btnVolver);
        
        if (SerieNacional.getInstance().getMisEquipos().size() >= 5)
        {
        	btnGenerar.setVisible(true);
        }

        loadAll(filtro);
        
        if (SerieNacional.getInstance().getMisJuegos().size() > 0)
        {
        	btnGenerar.setVisible(false);
        	btnConsultar.setVisible(true);
        }
    }

    public static void loadAll(String filtro) {
        if (modeloTabla == null) {
            String[] columnas = {"ID", "Equipo de Casa", "Equipo de Visita", "Ganador"};
            modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }
        
        modeloTabla.setRowCount(0);
        row = new Object[modeloTabla.getColumnCount()];
        ArrayList<Juego> juegos = SerieNacional.getInstance().getMisJuegos();;
        Equipo equ = SerieNacional.getInstance().searchEquipoById(filtro, SerieNacional.getInstance().getMisEquipos());
        Jugador jug = SerieNacional.getInstance().searchJugadorById(filtro, SerieNacional.getInstance().getMisJugadores());

        if (equ != null)
        	juegos = equ.getJuegos();
        
        if (jug != null)
        	juegos = jug.getJuegos();
        
        for (Juego juego : juegos) {
                row[0] = juego.getId();
                row[1] = juego.getHome().getNombre();
                row[2] = juego.getAway().getNombre();
                row[3] = juego.getGanador();
                modeloTabla.addRow(row);
            }
        }

	public static void loadFew() {
	    if (modeloTabla == null) {
	        String[] columnas = {"ID", "Equipo de Casa", "Equipo de Visita", "Ganador"};
	        modeloTabla = new DefaultTableModel(columnas, 0) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false;
	            }
	        };
	    }
	    
	    modeloTabla.setRowCount(0);
	    row = new Object[modeloTabla.getColumnCount()];
	    ArrayList<Juego> juegos = SerieNacional.getInstance().getMisJuegos();	    
	    for (Juego juego : juegos) {
	    	if (juego.getGanador() == null)
	    	{
	            row[0] = juego.getId();
	            row[1] = juego.getHome().getNombre();
	            row[2] = juego.getAway().getNombre();
	            row[3] = juego.getGanador();
	            modeloTabla.addRow(row);
	    	}
	    }
	}

    public void seleccionarJuego(PsimulacionJuego ventana) {
		btnConsultar.setVisible(false);
		setTitle("Seleccionar Juego");
		loadFew();
	    table.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	                int index = table.getSelectedRow();
	                if(index != -1) {
	                    String juegoId = table.getValueAt(index, 0).toString();
	                    Juego juegoSeleccionado = SerieNacional.getInstance().searchJuegoById(juegoId, 
	                                               SerieNacional.getInstance().getMisJuegos());
	                    ventana.setJuegoSeleccionado(juegoSeleccionado);
	                    dispose();
	                }
	        }
	    });
	}
}
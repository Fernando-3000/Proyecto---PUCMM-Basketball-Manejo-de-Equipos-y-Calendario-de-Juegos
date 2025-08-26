package visual;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import logico.Jugador;
import SQL.DatabaseManager;

public class GraficaEfectividad extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<Jugador> jugadores;

    /**
     * Constructor: inicializa con todos los jugadores del sistema
     */
    public GraficaEfectividad() {
        setBackground(Color.WHITE);
        cargarDatos();
    }

    /**
     * Carga los jugadores desde la base de datos
     */
    public void cargarDatos() {
        this.jugadores = new ArrayList<>(DatabaseManager.listarJugadores());
        repaint();
    }

    /**
     * Obtiene los top 10 jugadores con mayor efectividad
     * @return lista ordenada de hasta 10 jugadores
     */
    private List<Jugador> obtenerTop10() {
        if (jugadores == null || jugadores.isEmpty()) {
            return new ArrayList<>();
        }

        // Crear copia y ordenar por efectividad (descendente)
        List<Jugador> copia = new ArrayList<>(jugadores);
        copia.removeIf(j -> j.getEstadisticas() == null); // Evitar null

        copia.sort((j1, j2) -> Double.compare(
            j2.getEstadisticas().efectividad(),
            j1.getEstadisticas().efectividad()
        ));

        return copia.subList(0, Math.min(10, copia.size()));
    }

    /**
     * Encuentra el valor máximo de efectividad
     */
    private double getMaxEfectividad() {
        return jugadores == null ? 0.0 : jugadores.stream()
            .filter(j -> j.getEstadisticas() != null)
            .mapToDouble(j -> j.getEstadisticas().efectividad())
            .max()
            .orElse(1.0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<Jugador> topJugadores = obtenerTop10();
        if (topJugadores.isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString("No hay jugadores disponibles.", 20, 50);
            return;
        }

        int panelWidth = getWidth();
        int xInicio = 150;
        int anchoMaximo = Math.max(100, panelWidth - xInicio - 100);
        int y = 50;
        int alturaBarra = 25;
        int espacio = 35;

        double maxEfectividad = getMaxEfectividad();

        // Títulos
        g.setColor(Color.DARK_GRAY);
        g.setFont(g.getFont().deriveFont(14f).deriveFont(java.awt.Font.BOLD));
        g.drawString("Top 10 Jugadores por Efectividad", 20, 25);

        g.setFont(g.getFont().deriveFont(11f));
        g.drawString("Máxima efectividad: " + String.format("%.2f", maxEfectividad), 20, 40);

        // Dibujar barras
        for (int i = 0; i < topJugadores.size(); i++) {
            Jugador jugador = topJugadores.get(i);
            double efectividad = jugador.getEstadisticas().efectividad();
            int anchoBarra = maxEfectividad > 0 ? (int) ((efectividad / maxEfectividad) * anchoMaximo) : 0;

            // Nombre del jugador
            g.setColor(Color.BLACK);
            g.setFont(g.getFont().deriveFont(11f));
            String nombre = jugador.getNombre() + " " + jugador.getApellido();
            g.drawString(nombre.length() > 20 ? nombre.substring(0, 17) + "..." : nombre, 20, y + 20);

            // Barra de efectividad
            g.setColor(new Color(100, 180, 255));
            g.fillRect(xInicio, y, anchoBarra, alturaBarra);

            // Valor numérico
            g.setColor(Color.BLACK);
            g.drawString(String.format("%.2f", efectividad), xInicio + anchoBarra + 10, y + 20);

            y += alturaBarra + espacio;
        }
    }
}
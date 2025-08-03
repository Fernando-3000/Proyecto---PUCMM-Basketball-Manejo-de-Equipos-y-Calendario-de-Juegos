package visual;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import logico.Jugador;
import logico.SerieNacional;

public class GraficaEfectividad extends JPanel {
	private List<Jugador>misJugadores;
	private List<Jugador>copiaEfectividad;

	/**
	 * Create the panel.
	 */
	public GraficaEfectividad() {
		SerieNacional serieNacional = SerieNacional.getInstance();
		misJugadores = serieNacional.getMisJugadores();
		copiaEfectividad = new ArrayList<>(misJugadores);
		
	}
	public List<Jugador>copiaEfectividad(){
		return  new ArrayList<>(misJugadores);
	}
	public List<Jugador>obtenerTopsEfectividad(){
		for(int o=0; o<copiaEfectividad.size();o++) {
			for(int u=0; u<copiaEfectividad.size();u++) {
				if(copiaEfectividad.get(o).getEstadisticas().efectividad()<copiaEfectividad.get(u).getEstadisticas().efectividad()) {
					Jugador aux = copiaEfectividad.get(o);
					copiaEfectividad.set(o, copiaEfectividad.get(u));
					copiaEfectividad.set(u, aux);
				}
			}
		}
		return copiaEfectividad.subList(0, Math.min(10, copiaEfectividad.size()));
	}
	
	//grafiquita ;P
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<Jugador> topJugadores = obtenerTopsEfectividad();

        if (topJugadores.isEmpty()) return;

        int panelWidth = getWidth();
        int xInicio = 150;
        int anchoMaximo = panelWidth - xInicio - 100;
        int y = 50;
        int alturaBarra = 25;
        int espacio = 35;

        double maxEfectividad = 1.0;
        for (int e = 0; e < topJugadores.size(); e++) {
            double valor = topJugadores.get(e).getEstadisticas().efectividad();
            if (valor > maxEfectividad) {
                maxEfectividad = valor;
            }
        }
        g.setColor(Color.DARK_GRAY);
        g.drawString("Top 10 Jugadores con Mayor Efectividad", 20, 20);
        g.drawString("Máximo: " + String.format("%.2f", maxEfectividad), 20, 35);

        for (int i = 0; i < topJugadores.size(); i++) {
            Jugador jugador = topJugadores.get(i);
            double efectividad = jugador.getEstadisticas().efectividad();
            int anchoBarra = (int) ((efectividad / maxEfectividad) * anchoMaximo);

            g.setColor(Color.BLACK);
            g.drawString(jugador.getNombre(), 20, y + 20);

            g.setColor(new Color(100, 180, 255));
            g.fillRect(xInicio, y, anchoBarra, alturaBarra);

            g.setColor(Color.BLACK);
            g.drawString(String.format("%.2f", efectividad), xInicio + anchoBarra + 10, y + 20);

            y += alturaBarra + espacio;
        }
    }
}

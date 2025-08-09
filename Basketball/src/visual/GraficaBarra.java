package visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logico.Jugador;
import logico.SerieNacional;

public class GraficaBarra extends JPanel {
	private BufferedImage imagenJugador;
	private List<Jugador>misJugadores;
	private List<Jugador>copiaJugadores;
	private static int tamanoTop;
	/**
	 * Create the panel.
	 */
	public GraficaBarra() {
	    SerieNacional serieNacional = SerieNacional.getInstance(); //instacia Narayan :D
	    misJugadores = serieNacional.getMisJugadores();//aqui se obtiene la lista original :p
	    copiaJugadores = new ArrayList<>(misJugadores); // aqui se almacena la copia del arraylist :3
	    tamanoTop=5;
	}
	// metodo que realiza la copia :)
	public List<Jugador> copiaJuagadores() {
		return new ArrayList<>(misJugadores); // copia superficial?	
	}
	public List<Jugador>obtenerTopsJugadores(){
		for(int x=0; x<copiaJugadores.size();x++) {
			for(int f=0; f<copiaJugadores.size();f++) {
				if(copiaJugadores.get(x).getEstadisticas().getPuntosTot()<copiaJugadores.get(f).getEstadisticas().getPuntosTot()) {
					Jugador temp = copiaJugadores.get(x);
					// aqui se hace el ordenamineto >:DD//
					copiaJugadores.set(x, copiaJugadores.get(f));
					copiaJugadores.set(f, temp);
				}
			}
		}
		return copiaJugadores.subList(0, Math.min(tamanoTop, copiaJugadores.size()));//lista de 3 jugadadores :0
	}
	//grafica
	public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int width = getWidth();
	        int height = getHeight();
	        List<Jugador> topJugadores = obtenerTopsJugadores(); // aqui se obtiene los 3 mejores jugadores
	        int barWidth =0;
	        int maxPoints=0;
	       
	        if(topJugadores.size()>0) {
	        	barWidth=width/(topJugadores.size()+1);
	        }
	      
	        for(Jugador jugador : topJugadores) {
	        	int puntos = jugador.getEstadisticas().getPuntosTot();
	        	if(puntos > maxPoints) {
	        		maxPoints = puntos;
	        	}
	        }
	        for (int i = 0; i < topJugadores.size(); i++) {
	            Jugador jugador = topJugadores.get(i);
	            int barHeight = (int) ((double) jugador.getEstadisticas().getPuntosTot() / maxPoints * (height - 50)); // Escalar la altura de la barra

	            // dibuja la barra...
	            g.setColor(Color.BLUE);
	            g.fillRect(i * barWidth + 50, height - barHeight - 30, barWidth - 20, barHeight);

	            // nombre...
	            g.setColor(Color.BLACK);
	            g.drawString(jugador.getNombre(), i * barWidth + 50, height - 10);

	            // cara del jugador
	            BufferedImage imagenJugador = null;
	            File foto = jugador.getFoto(); 
	            if (foto != null) {
	                try {
	                    imagenJugador = ImageIO.read(foto); 
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (imagenJugador != null) {
	                g.drawImage(imagenJugador, i * barWidth + 50, height - barHeight - 30 - 50, 50, 50, null); //esto es para ajustar el tama�o y la posici�n
	            	}
	        	}
	    	}
	    }
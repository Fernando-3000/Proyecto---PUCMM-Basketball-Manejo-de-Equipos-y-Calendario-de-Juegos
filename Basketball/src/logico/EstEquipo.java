package logico;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EstEquipo extends Estadistica{

	private int juegosGanados;
	private int juegosPerdidos;
	private int torneosGanados;
	
	public EstEquipo(int cantJuegos, int triples, int dobles, int normales, int puntosTot,
			int juegosGanados, int juegosPerdidos, int torneosGanados) {
		super(cantJuegos, triples, dobles, normales, puntosTot);
		this.juegosGanados = juegosGanados;
		this.juegosPerdidos = juegosPerdidos;
		this.torneosGanados = torneosGanados;
	}
	
	@Override
	public float efectividad() {
	    if (cantJuegos == 0) {
	        return 0.0f;}
	    
	    float puntosPorJuego = (float) puntosTot / cantJuegos;
	    
	    float eficienciaTiros = (triples * 3.0f + dobles * 2.0f + normales * 1.0f) / (float) puntosTot;
	    
	    float factorTorneos = 1.0f + (torneosGanados * 0.1f);
	    
	    return (winrate() * 50.0f + puntosPorJuego * 0.5f + eficienciaTiros * 30.0f) * factorTorneos;
	}
	
	public float winrate() {
	    if (cantJuegos == 0) {
	        return 0.0f;}
	    
	    float proporcionGanados = (float) juegosGanados / cantJuegos;
	    return proporcionGanados;
	}

	public int getJuegosGanados() {
		return juegosGanados;
	}

	public void setJuegosGanados(int juegosGanados) {
		this.juegosGanados = juegosGanados;
	}

	public int getJuegosPerdidos() {
		return juegosPerdidos;
	}

	public void setJuegosPerdidos(int juegosPerdidos) {
		this.juegosPerdidos = juegosPerdidos;
	}

	public int getTorneosGanados() {
		return torneosGanados;
	}

	public void setTorneosGanados(int torneosGanados) {
		this.torneosGanados = torneosGanados;
	}
}

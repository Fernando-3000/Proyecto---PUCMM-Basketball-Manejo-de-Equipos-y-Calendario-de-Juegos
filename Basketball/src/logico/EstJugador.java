package logico;

import java.io.Serializable;

public class EstJugador extends Estadistica{
	private int robos;
	private int tapones;
	private int asistencias;
	private int faltas;
	private int mvp;
	
	public EstJugador(int cantJuegos, int triples, int dobles, int normales, int puntosTot, int robos, int tapones,
			int asistencias, int faltas, int mvp) {
		super(cantJuegos, triples, dobles, normales, puntosTot);
		this.robos = robos;
		this.tapones = tapones;
		this.asistencias = asistencias;
		this.faltas = faltas;
		this.mvp = mvp;
	}
	
	@Override
	public float efectividad() {
	    if (cantJuegos == 0) {
	        return 0.0f;}
	    
	    float puntosPorJuego = (float) puntosTot / cantJuegos;
	    
	    float puntosEfectivos = triples * 3.0f + dobles * 2.0f + normales * 1.0f;
	    float eficienciaTiros = puntosEfectivos / puntosTot;

	    float contribucionesDefensivas = (robos * 2.0f + tapones * 1.8f) / cantJuegos;
	    float contribucionesOfensivas = (asistencias * 1.5f + puntosPorJuego) / cantJuegos;
	    
	    float penalizaciones = faltas / (float) cantJuegos * 1.2f;
	    
	    float bonusMVP = mvp * 5.0f;
	    
	    return (contribucionesDefensivas + contribucionesOfensivas - penalizaciones + bonusMVP) * eficienciaTiros * 10.0f;
	}

	public int getRobos() {
		return robos;
	}

	public void setRobos(int robos) {
		this.robos = robos;
	}

	public int getTapones() {
		return tapones;
	}

	public void setTapones(int tapones) {
		this.tapones = tapones;
	}

	public int getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(int asistencias) {
		this.asistencias = asistencias;
	}

	public int getFaltas() {
		return faltas;
	}

	public void setFaltas(int faltas) {
		this.faltas = faltas;
	}

	public int getMvp() {
		return mvp;
	}

	public void setMvp(int mvp) {
		this.mvp = mvp;
	}
}

package logico;

import java.io.Serializable;

public abstract class Estadistica implements Serializable{

	protected int cantJuegos;
	protected int triples;
	protected int dobles;
	protected int normales;
	protected int puntosTot;
	private static final long serialVersionUID = 1L;
	
	public Estadistica(int cantJuegos, int triples, int dobles, int normales, int puntosTot) {
		super();
		this.cantJuegos = cantJuegos;
		this.triples = triples;
		this.dobles = dobles;
		this.normales = normales;
		this.puntosTot = puntosTot;
	}

	public int getCantJuegos() {
		return cantJuegos;
	}

	public void setCantJuegos(int cantJuegos) {
		this.cantJuegos = cantJuegos;
	}

	public int getTriples() {
		return triples;
	}

	public void setTriples(int triples) {
		this.triples = triples;
	}

	public int getDobles() {
		return dobles;
	}

	public void setDobles(int dobles) {
		this.dobles = dobles;
	}

	public int getNormales() {
		return normales;
	}

	public void setNormales(int normales) {
		this.normales = normales;
	}

	public int getPuntosTot() {
		puntosTot = triples*3 + dobles*2 + normales;
		return puntosTot;
	}
	
	public abstract float efectividad();
}

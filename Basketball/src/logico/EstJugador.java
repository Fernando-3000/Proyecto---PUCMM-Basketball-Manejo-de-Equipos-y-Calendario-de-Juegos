package logico;

public class EstJugador {
    private int cantJuegos;
    private int triples;
    private int dobles;
    private int normales;
    private int puntosTot;
    private int robos;
    private int tapones;
    private int asistencias;
    private int faltas;
    private int mvp;

    // Constructor vacío
    public EstJugador() {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    // Constructor completo
    public EstJugador(int cantJuegos, int triples, int dobles, int normales, int puntosTot,
                      int robos, int tapones, int asistencias, int faltas, int mvp) {
        this.cantJuegos = cantJuegos;
        this.triples = triples;
        this.dobles = dobles;
        this.normales = normales;
        this.puntosTot = puntosTot;
        this.robos = robos;
        this.tapones = tapones;
        this.asistencias = asistencias;
        this.faltas = faltas;
        this.mvp = mvp;
    }

    // Getters y setters...
    public int getCantJuegos() { return cantJuegos; }
    public void setCantJuegos(int cantJuegos) { this.cantJuegos = cantJuegos; }

    public int getTriples() { return triples; }
    public void setTriples(int triples) { this.triples = triples; }

    public int getDobles() { return dobles; }
    public void setDobles(int dobles) { this.dobles = dobles; }

    public int getNormales() { return normales; }
    public void setNormales(int normales) { this.normales = normales; }

    public int getPuntosTot() { return puntosTot; }
    public void setPuntosTot(int puntosTot) { this.puntosTot = puntosTot; }

    public int getRobos() { return robos; }
    public void setRobos(int robos) { this.robos = robos; }

    public int getTapones() { return tapones; }
    public void setTapones(int tapones) { this.tapones = tapones; }

    public int intgetAsistencias() { return asistencias; }
    public void setAsistencias(int asistencias) { this.asistencias = asistencias; }

    public int getFaltas() { return faltas; }
    public void setFaltas(int faltas) { this.faltas = faltas; }

    public int getMvp() { return mvp; }
    public void setMvp(int mvp) { this.mvp = mvp; }

	public double efectividad() {
		// TODO Auto-generated method stub
		return 0;
	}
}
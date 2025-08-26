package logico;

import java.io.Serializable;

/**
 * Clase base para estadísticas de jugadores y equipos
 */
public abstract class Estadistica implements Serializable {

    private static final long serialVersionUID = 1L;

    //  Todos los campos NO son static
    protected int cantJuegos;
    protected int triples;      //   No debe ser static
    protected int dobles;       //   No debe ser static
    protected int normales;     //   No debe ser static
    protected int puntosTot;

    public Estadistica(int cantJuegos, int triples, int dobles, int normales) {
        this.cantJuegos = cantJuegos;
        this.triples = triples;
        this.dobles = dobles;
        this.normales = normales;
        this.puntosTot = calcularPuntos(); //  Calcula al crear
    }

    // Constructor por defecto
    public Estadistica() {
        this(0, 0, 0, 0);
    }

    // Getters y setters
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
        actualizarPuntos(); //  Actualiza puntos al cambiar
    }

    public int getDobles() {
        return dobles;
    }

    public void setDobles(int dobles) {
        this.dobles = dobles;
        actualizarPuntos(); //  Actualiza puntos al cambiar
    }

    public int getNormales() {
        return normales;
    }

    public void setNormales(int normales) {
        this.normales = normales;
        actualizarPuntos(); //  Actualiza puntos al cambiar
    }

    public int getPuntosTot() {
        return puntosTot;
    }

    //  Método privado para calcular puntos
    private int calcularPuntos() {
        return triples * 3 + dobles * 2 + normales;
    }

    //  Actualiza puntos cuando cambia cualquier valor
    private void actualizarPuntos() {
        this.puntosTot = calcularPuntos();
    }

    //  Método abstracto para efectividad
    public abstract float efectividad();
}
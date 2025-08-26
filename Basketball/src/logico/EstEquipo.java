package logico;

import java.io.Serializable;

public class EstEquipo extends Estadistica {

    private int juegosGanados;
    private int juegosPerdidos;
    private int torneosGanados;

    public EstEquipo(int cantJuegos, int triples, int dobles, int normales,
                     int juegosGanados, int juegosPerdidos, int torneosGanados) {
        super(cantJuegos, triples, dobles, normales);
        this.juegosGanados = juegosGanados;
        this.juegosPerdidos = juegosPerdidos;
        this.torneosGanados = torneosGanados;
    }

    public EstEquipo() {
        this(0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public float efectividad() {
        if (cantJuegos == 0) {
            return 0.0f;
        }

        float puntosPorJuego = (float) puntosTot / cantJuegos;

        float tirosTotales = triples + dobles + normales;
        float eficienciaTiros = tirosTotales > 0 ? 
            (triples * 3.0f + dobles * 2.0f + normales) / tirosTotales : 0.0f;

        float factorTorneos = 1.0f + (torneosGanados * 0.1f);

        return (winrate() * 50.0f + puntosPorJuego * 0.5f + eficienciaTiros * 30.0f) * factorTorneos;
    }

    public float winrate() {
        if (cantJuegos == 0) {
            return 0.0f;
        }
        return (float) juegosGanados / cantJuegos;
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
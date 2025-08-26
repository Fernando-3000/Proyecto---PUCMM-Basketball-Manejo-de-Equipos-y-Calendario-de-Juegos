package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Juego implements Serializable{
	    private String id;
	    private Equipo home;
	    private Equipo away;
	    private int marcadorCasa;
	    private int marcadorAway;
	    private String ganador;
	    private static final long serialVersionUID = 1L;
	    

	    public Juego(String id, Equipo home, Equipo away) {
	        this.id = id;
	        this.home = home;
	        this.away = away;
	        this.marcadorCasa = 0;
	        this.marcadorAway = 0;
	        this.ganador = null;
	    }

	    public String getId() {
	        return id;
	    }

	    public Equipo getHome() {
	        return home;
	    }

	    public void setHome(Equipo home) {
	        this.home = home;
	    }

	    public Equipo getAway() {
	        return away;
	    }

	    public void setAway(Equipo away) {
	        this.away = away;
	    }

	    public int getMarcadorCasa() {
	        return marcadorCasa;
	    }

	    public void setMarcadorCasa(int marcadorCasa) {
	        this.marcadorCasa = marcadorCasa;
	    }

	    public int getMarcadorAway() {
	        return marcadorAway;
	    }

	    public void setMarcadorAway(int marcadorAway) {
	        this.marcadorAway = marcadorAway;
	    }

	    public String getGanador() {
	        return ganador;
	    }

	    public void setGanador(String ganador) {
	        this.ganador = ganador;
	    }
	    
	    public void actualizarDatos(Juego aux) {
	        this.home = aux.getHome();
	        this.away = aux.getAway();
	        this.marcadorCasa = aux.getMarcadorCasa();
	        this.marcadorAway = aux.getMarcadorAway();
	        this.ganador = aux.getGanador();
	    }

		public String getNombre() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getApellido() {
			// TODO Auto-generated method stub
			return null;
		}
	}
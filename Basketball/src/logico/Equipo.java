package logico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Equipo implements Serializable{
	    private String id;
	    private String nombre;
	    private String entrenador;
	    private String pais;
	    private int anoFundacion;
	    private String dueno;
	    private File foto;
	    private ArrayList<Juego> juegos;
	    private ArrayList<Jugador> jugadores;
	    private EstEquipo estadistica;
	    private static final long serialVersionUID = 1L;

	    public Equipo(String id, String nombre, String entrenador, String pais, int anoFundacion, 
	    			  String dueno, File foto, ArrayList<Juego> juegos, ArrayList<Jugador> jugadores) {
	        this.id = id;
	        this.nombre = nombre;
	        this.entrenador = entrenador;
	        this.pais = pais;
	        this.anoFundacion = anoFundacion;
	        this.dueno = dueno;
	        this.foto = foto;
	        this.jugadores = jugadores;
	        this.juegos = juegos;
	        this.estadistica = new EstEquipo(0, 0, 0, 0, 0, 0, 0, 0);
	    }

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getEntrenador() {
			return entrenador;
		}

		public void setEntrenador(String entrenador) {
			this.entrenador = entrenador;
		}

		public String getPais() {
			return pais;
		}

		public void setPais(String pais) {
			this.pais = pais;
		}

		public int getAnoFundacion() {
			return anoFundacion;
		}

		public void setAnoFundacion(int anoFundacion) {
			this.anoFundacion = anoFundacion;
		}

		public String getDueno() {
			return dueno;
		}

		public void setDueno(String dueno) {
			this.dueno = dueno;
		}

		public File getFoto() {
			return foto;
		}

		public void setFoto(File foto) {
			this.foto = foto;
		}

		public ArrayList<Juego> getJuegos() {
			return juegos;
		}

		public void setJuegos(ArrayList<Juego> juegos) {
			this.juegos = juegos;
		}

		public ArrayList<Jugador> getJugadores() {
			return jugadores;
		}

		public void setJugadores(ArrayList<Jugador> jugadores) {
			this.jugadores = jugadores;
		}

		public EstEquipo getEstadistica() {
			return estadistica;
		}

		public void setEstadistica(EstEquipo estadistica) {
			this.estadistica = estadistica;
		}

		public String getId() {
			return id;
		}
		
		public void actualizarDatos(Equipo aux) { 
			this.nombre = aux.getNombre();
			this.entrenador = aux.getEntrenador();
			this.pais = aux.getPais();
			this.anoFundacion = aux.getAnoFundacion();
			this.foto = aux.getFoto();
			this.dueno = aux.getDueno();
			this.juegos = aux.getJuegos();
			this.jugadores = aux.getJugadores();
			this.estadistica = aux.getEstadistica();
		}
	}
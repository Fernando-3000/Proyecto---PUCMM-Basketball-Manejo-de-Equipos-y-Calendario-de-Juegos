package logico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable{
	private String id;
	private String nombre;
	private String apellido;
	private String posicion;
	private float pesoKg;
	private float alturaCm;
	private int numero;
	private File foto;
	private Equipo equipo;
	private ArrayList<Lesion>misLesiones;
	private ArrayList<Juego>juegos;
	private EstJugador estadisticas;
	private static final long serialVersionUID = 1L;

	public Jugador(String id, String nombre, String apellido, String posicion, float pesoKg, float alturaCm, int numero, File foto, Equipo equipo,
				   ArrayList<Lesion> misLesiones, ArrayList<Juego> juegos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.posicion = posicion;
		this.pesoKg = pesoKg;
		this.alturaCm = alturaCm;
		this.numero = numero;
		this.foto = foto;
		this.equipo = equipo;
		this.misLesiones = misLesiones;
		this.juegos = juegos;
		this.estadisticas = new EstJugador(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public float getPesoKg() {
		return pesoKg;
	}

	public void setPesoKg(float pesoKg) {
		this.pesoKg = pesoKg;
	}

	public float getAlturaCm() {
		return alturaCm;
	}

	public void setAlturaCm(float alturaCm) {
		this.alturaCm = alturaCm;
	}

	public File getFoto() {
		return foto;
	}

	public void setFoto(File foto) {
		this.foto = foto;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public ArrayList<Lesion> getMisLesiones() {
		return misLesiones;
	}

	public void setMisLesiones(ArrayList<Lesion> misLesiones) {
		this.misLesiones = misLesiones;
	}

	public ArrayList<Juego> getJuegos() {
		return juegos;
	}

	public void setJuegos(ArrayList<Juego> juegos) {
		this.juegos = juegos;
	}

	public EstJugador getEstadisticas() {
		return estadisticas;
	}

	public void setEstadisticas(EstJugador estadisticas) {
		this.estadisticas = estadisticas;
	}


	public boolean getEstadoSalud() {
		for (Lesion lesI : misLesiones)
		{
			if (lesI.isEstado())				
				return false;
		}

		return true;
	}
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void actualizarDatos(Jugador aux) {
	    this.nombre = aux.getNombre();
	    this.apellido = aux.getApellido();
	    this.pesoKg = aux.getPesoKg();
	    this.alturaCm = aux.getAlturaCm();
	    this.foto = aux.getFoto();
	    this.equipo = aux.getEquipo();
	    this.misLesiones = aux.getMisLesiones();
	    this.juegos = aux.getJuegos();
	    this.numero = aux.getNumero();
	    this.estadisticas = aux.getEstadisticas();
	}
}


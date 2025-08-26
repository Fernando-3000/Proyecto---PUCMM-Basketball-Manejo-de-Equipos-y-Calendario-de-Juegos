package logico;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
	private String id;
	private String nombre;
	private String apellido;
	private String posicion;
	private float pesoKg;
	private float alturaCm;
	private int numero;
	private File fotoFile;
	private BufferedImage fotoBuffered;
	private Equipo equipo;
	private String ID_Equipo;
	private ArrayList<Lesion> misLesiones;
	private ArrayList<Juego> juegos;
	private EstJugador estadisticas;
	private static final long serialVersionUID = 1L;

	public Jugador(String id, String nombre, String apellido, String posicion, float pesoKg, float alturaCm, int numero,
			File foto, Equipo equipo, ArrayList<Lesion> misLesiones, ArrayList<Juego> juegos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.posicion = posicion;
		this.pesoKg = pesoKg;
		this.alturaCm = alturaCm;
		this.numero = numero;
		this.fotoFile = foto;
		this.equipo = equipo;
		this.misLesiones = misLesiones;
		this.juegos = juegos;
		this.estadisticas = new EstJugador(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public Jugador(String ID_Jugador, String nombre, String apellido, String posicion, float pesoKg, float alturaCm, int numero,
			File foto, String ID_Equipo) {
		super();
		this.id = ID_Jugador;
		this.nombre = nombre;
		this.apellido = apellido;
		this.posicion = posicion;
		this.pesoKg = pesoKg;
		this.alturaCm = alturaCm;
		this.numero = numero;
		this.fotoFile = foto;
		this.setID_Equipo(ID_Equipo);
	}
	
	public Jugador(String id, String nombre, String apellido, String posicion, float pesoKg, float alturaCm, int numero,
			BufferedImage fotoBuffered, String ID_Equipo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.posicion = posicion;
		this.pesoKg = pesoKg;
		this.alturaCm = alturaCm;
		this.numero = numero;
		this.setFotoBuffered(fotoBuffered);
		this.setID_Equipo(ID_Equipo);
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

	public File getFotoFile() {
		return fotoFile;
	}

	public void setFotoFile(File fotoFile) {
		this.fotoFile = fotoFile;
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
		for (Lesion lesI : misLesiones) {
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
		this.fotoFile = aux.getFotoFile();
		this.equipo = aux.getEquipo();
		this.misLesiones = aux.getMisLesiones();
		this.juegos = aux.getJuegos();
		this.numero = aux.getNumero();
		this.estadisticas = aux.getEstadisticas();
	}

	public BufferedImage getFotoBuffered() {
		return fotoBuffered;
	}

	public void setFotoBuffered(BufferedImage fotoBuffered) {
		this.fotoBuffered = fotoBuffered;
	}

	public String getID_Equipo() {
		return ID_Equipo;
	}

	public void setID_Equipo(String iD_Equipo) {
		ID_Equipo = iD_Equipo;
	}

	public BufferedImage getImagen() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getGanador() {
		// TODO Auto-generated method stub
		return null;
	}
}

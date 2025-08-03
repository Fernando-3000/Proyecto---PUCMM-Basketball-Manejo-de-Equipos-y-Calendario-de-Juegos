package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Lesion implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private Jugador jugador;
	private String tipoDeLesion;
	private LocalDate fechaLes;
	private LocalDate fechaRecPrevista;
	private String descripcionCorta;
	private boolean estado;
		
	public Lesion(String id, Jugador jugador, LocalDate fechaLes, String tipoDeLesion, LocalDate fechaRecPrevista,
			String descripcionCorta, boolean estado) {
		super();
		this.id = id;
		this.jugador = jugador;
		this.fechaLes = fechaLes;
		this.tipoDeLesion = tipoDeLesion;
		this.fechaRecPrevista = fechaRecPrevista;
		this.descripcionCorta = descripcionCorta;
		this.estado = estado;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public LocalDate getFechaLes() {
		return fechaLes;
	}

	public void setFechaLes(LocalDate fechaLes) {
		this.fechaLes = fechaLes;
	}

	public String getTipoDeLesion() {
		return tipoDeLesion;
	}

	public void setTipoDeLesion(String tipoDeLesion) {
		this.tipoDeLesion = tipoDeLesion;
	}

	public LocalDate getFechaRecPrevista() {
		return fechaRecPrevista;
	}

	public void setFechaRecPrevista(LocalDate fechaRecPrevista) {
		this.fechaRecPrevista = fechaRecPrevista;
	}

	public String getDescripcionCorta() {
		return descripcionCorta;
	}

	public void setDescripcionCorta(String descripcionCorta) {
		this.descripcionCorta = descripcionCorta;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getId() {
		return id;
	}

	public void actualizarDatos(Lesion aux) {
	    this.jugador = aux.getJugador();
	    this.tipoDeLesion = aux.getTipoDeLesion();
	    this.fechaLes = aux.getFechaLes();
	    this.fechaRecPrevista = aux.getFechaRecPrevista();
	    this.descripcionCorta = aux.getDescripcionCorta();
	    this.estado = aux.isEstado();
	}
}

package logico;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String entrenador;
    private String pais;
    private int anoFundacion;
    private String dueno;
    private BufferedImage imagenLogo;
    private File imagenLogoFile;
    private ArrayList<Juego> juegos;
    private ArrayList<Jugador> jugadores;
    private EstEquipo estadistica;

    // Constructor completo: usado al cargar desde memoria
    public Equipo(String id, String nombre, String entrenador, String pais, int anoFundacion, String dueno,
                  BufferedImage imagenLogo, ArrayList<Juego> juegos, ArrayList<Jugador> jugadores) {
        this.id = id;
        this.nombre = nombre;
        this.entrenador = entrenador;
        this.pais = pais;
        this.anoFundacion = anoFundacion;
        this.dueno = dueno;
        this.imagenLogo = imagenLogo;
        this.juegos = juegos != null ? juegos : new ArrayList<>();
        this.jugadores = jugadores != null ? jugadores : new ArrayList<>();
        this.estadistica = new EstEquipo();
        actualizarEstadistica();
    }

    // Constructor para registro nuevo: con BufferedImage
    public Equipo(String id, String nombre, int anoFundacion, String pais, String entrenador, String dueno,
                  BufferedImage imagenLogo) {
        this.id = id;
        this.nombre = nombre;
        this.anoFundacion = anoFundacion;
        this.pais = pais;
        this.entrenador = entrenador;
        this.dueno = dueno;
        this.imagenLogo = imagenLogo;
        this.juegos = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.estadistica = new EstEquipo();
    }

    // Constructor con File (para registro con archivo)
    public Equipo(String id, String nombre, int anoFundacion, String pais, String entrenador, String dueno,
                  File imagenLogo) {
        this(id, nombre, anoFundacion, pais, entrenador, dueno, (BufferedImage) null);
        this.imagenLogoFile = imagenLogo;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<Juego> getJuegos() {
        return juegos;
    }

    public void setJuegos(ArrayList<Juego> juegos) {
        this.juegos = juegos != null ? juegos : new ArrayList<>();
        actualizarEstadistica();
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores != null ? jugadores : new ArrayList<>();
        actualizarEstadistica();
    }

    public EstEquipo getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(EstEquipo estadistica) {
        this.estadistica = estadistica;
    }

    public BufferedImage getImagenLogo() {
        return imagenLogo;
    }

    public void setImagenLogo(BufferedImage imagenLogo) {
        this.imagenLogo = imagenLogo;
    }

    public File getImagenLogoFile() {
        return imagenLogoFile;
    }

    public void setImagenLogoFile(File imagenLogoFile) {
        this.imagenLogoFile = imagenLogoFile;
    }

    // Métodos de utilidad

    public void actualizarDatos(Equipo aux) {
        if (aux == null) return;
        this.nombre = aux.getNombre();
        this.entrenador = aux.getEntrenador();
        this.pais = aux.getPais();
        this.anoFundacion = aux.getAnoFundacion();
        this.dueno = aux.getDueno();
        this.imagenLogo = aux.getImagenLogo();
        this.imagenLogoFile = aux.getImagenLogoFile();
        this.juegos = new ArrayList<>(aux.getJuegos());
        this.jugadores = new ArrayList<>(aux.getJugadores());
        this.estadistica = aux.getEstadistica();
    }

    /**
     * Actualiza la estadística del equipo basada en sus jugadores y juegos
     */
    public void actualizarEstadistica() {
        if (estadistica == null) {
            estadistica = new EstEquipo();
        }

        int triples = 0, dobles = 0, normales = 0;
        int juegosGanados = 0, juegosPerdidos = 0;

        for (Jugador jugador : jugadores) {
            if (jugador.getEstadisticas() != null) {
                triples += jugador.getEstadisticas().getTriples();
                dobles += jugador.getEstadisticas().getDobles();
                normales += jugador.getEstadisticas().getNormales();
            }
        }

        for (Juego juego : juegos) {
            if (juego.getGanador() != null && juego.getGanador().equals(this.nombre)) {
                juegosGanados++;
            } else if (juego.getGanador() != null) {
                juegosPerdidos++;
            }
        }

        estadistica.setTriples(triples);
        estadistica.setDobles(dobles);
        estadistica.setNormales(normales);
        estadistica.setJuegosGanados(juegosGanados);
        estadistica.setJuegosPerdidos(juegosPerdidos);
        estadistica.setCantJuegos(juegos.size());
    }

    @Override
    public String toString() {
        return nombre;
    }
}
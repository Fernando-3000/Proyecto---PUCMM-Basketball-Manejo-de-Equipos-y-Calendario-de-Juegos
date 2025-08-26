package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class SerieNacional implements Serializable {

    private static final long serialVersionUID = 1L;

    private static SerieNacional serie = null;
    private static User loginUser = null;

    // Listas de datos
    private ArrayList<User> misUsers;
    private ArrayList<Equipo> misEquipos;
    private ArrayList<Jugador> misJugadores;
    private ArrayList<Juego> misJuegos;

    // Generadores de ID
    private int generadorEquipo;
    private int generadorJugador;
    private int generadorJuego;
    private int generadorLesion;

    private SerieNacional() {
        misUsers = new ArrayList<>();
        misEquipos = new ArrayList<>();
        misJugadores = new ArrayList<>();
        misJuegos = new ArrayList<>();

        generadorEquipo = 1;
        generadorJugador = 1;
        generadorJuego = 1;
        generadorLesion = 1;
    }

    public static SerieNacional getInstance() {
        if (serie == null) {
            serie = new SerieNacional();
        }
        return serie;
    }

    public static void setSerie(SerieNacional nuevaSerie) {
        serie = nuevaSerie;
    }

    // Getters y Setters

    public ArrayList<User> getMisUsuarios() {
        return misUsers;
    }

    public void setMisUsuarios(ArrayList<User> usuarios) {
        this.misUsers = usuarios != null ? usuarios : new ArrayList<>();
    }

    public ArrayList<Equipo> getMisEquipos() {
        return misEquipos;
    }

    public void setMisEquipos(ArrayList<Equipo> equipos) {
        this.misEquipos = equipos != null ? equipos : new ArrayList<>();
    }

    public ArrayList<Jugador> getMisJugadores() {
        return misJugadores;
    }

    public void setMisJugadores(ArrayList<Jugador> jugadores) {
        this.misJugadores = jugadores != null ? jugadores : new ArrayList<>();
    }

    public ArrayList<Juego> getMisJuegos() {
        return misJuegos;
    }

    public void setMisJuegos(ArrayList<Juego> juegos) {
        this.misJuegos = juegos != null ? juegos : new ArrayList<>();
    }

    public int getGeneradorEquipo() {
        return generadorEquipo++;
    }

    public int getGeneradorJugador() {
        return generadorJugador++;
    }

    public int getGeneradorJuego() {
        return generadorJuego++;
    }

    public int getGeneradorLesion() {
        return generadorLesion++;
    }

    // Métodos de búsqueda

    public Equipo searchEquipoById(String id) {
        for (Equipo equipo : misEquipos) {
            if (equipo.getId().equals(id)) {
                return equipo;
            }
        }
        return null;
    }

    public Jugador searchJugadorById(String id) {
        for (Jugador jugador : misJugadores) {
            if (jugador.getId().equals(id)) {
                return jugador;
            }
        }
        return null;
    }

    public Juego searchJuegoById(String id) {
        for (Juego juego : misJuegos) {
            if (juego.getId().equals(id)) {
                return juego;
            }
        }
        return null;
    }

    public Lesion searchLesionByIdInPlayer(String id, ArrayList<Lesion> lesiones) {
        for (Lesion lesion : lesiones) {
            if (lesion.getId().equals(id)) {
                return lesion;
            }
        }
        return null;
    }

    public Lesion searchLesionById(String id) {
        for (Jugador jugador : misJugadores) {
            for (Lesion lesion : jugador.getMisLesiones()) {
                if (lesion.getId().equals(id)) {
                    return lesion;
                }
            }
        }
        return null;
    }

    // Métodos de modificación (para uso en memoria, aunque ahora se usa DatabaseManager)

    public void modificarEquipo(Equipo aux) {
        Equipo equipo = searchEquipoById(aux.getId());
        if (equipo != null) {
            equipo.actualizarDatos(aux);
        }
    }

    public void modificarJugador(Jugador aux) {
        Jugador jugador = searchJugadorById(aux.getId());
        if (jugador != null) {
            if (!jugador.getEquipo().getId().equals(aux.getEquipo().getId())) {
                Equipo equipoViejo = searchEquipoById(jugador.getEquipo().getId());
                Equipo equipoNuevo = searchEquipoById(aux.getEquipo().getId());
                if (equipoViejo != null) {
                    equipoViejo.getJugadores().remove(jugador);
                }
                if (equipoNuevo != null) {
                    equipoNuevo.getJugadores().add(aux);
                }
            }
            jugador.actualizarDatos(aux);
        }
    }

    public void modificarJuego(Juego aux) {
        Juego juego = searchJuegoById(aux.getId());
        if (juego != null) {
            juego.actualizarDatos(aux);
        }
    }

    public void modificarLesion(Lesion aux) {
        Lesion lesion = searchLesionById(aux.getId());
        if (lesion != null) {
            lesion.actualizarDatos(aux);
        }
    }

    public void eliminarLesion(Lesion aux) {
        Lesion lesion = searchLesionById(aux.getId());
        if (lesion != null) {
            Jugador jugador = searchJugadorById(lesion.getJugador().getId());
            if (jugador != null) {
                jugador.getMisLesiones().remove(lesion);
            }
        }
    }

    // Métodos de usuario

    public User buscarUser(String userName) {
        for (User user : misUsers) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public boolean confirmLogin(String userName, String pass) {
        for (User user : misUsers) {
            if (user.getUserName().equals(userName) && user.getPass().equals(pass)) {
                loginUser = user;
                return true;
            }
        }
        return false;
    }

    public static User getLoginUser() {
        return loginUser;
    }

    public static void setLoginUser(User user) {
        loginUser = user;
    }

    // Generar juegos (solo en memoria, aunque ahora se usa en combinación con DatabaseManager)
    public static ArrayList<Juego> generarJuegos(ArrayList<Equipo> equipos) {
        ArrayList<Juego> juegos = new ArrayList<>();

        if (equipos == null || equipos.size() < 2) {
            return juegos;
        }

        for (int i = 0; i < equipos.size(); i++) {
            for (int j = 0; j < equipos.size(); j++) {
                if (i != j) {
                    String id = "JG-" + (juegos.size() + 1);
                    juegos.add(new Juego(id, equipos.get(i), equipos.get(j)));
                }
            }
        }

        return juegos;
    }
}
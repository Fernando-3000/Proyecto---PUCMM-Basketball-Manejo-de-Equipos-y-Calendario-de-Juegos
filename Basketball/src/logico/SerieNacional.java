package logico;

import java.io.Serializable;
import java.util.ArrayList;


public class SerieNacional implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static SerieNacional serie = null;

	private ArrayList<User> misUsers;
	private static User loginUser = null;
	
	private ArrayList<Equipo> misEquipos;
	private int generadorEquipo;

	private ArrayList<Jugador> misJugadores;
	private int generadorJugador;

	private ArrayList<Juego> misJuegos;
	private int generadorJuego;
	
	private int generadorLesion;
		
	private SerieNacional() {
		super();
		misEquipos = new ArrayList<Equipo>();
		generadorEquipo = 1;
		
		misJugadores = new ArrayList<Jugador>();
		generadorJugador = 1;
		
		misJuegos = new ArrayList<Juego>();
		generadorJuego = 1;
		
		misUsers = new ArrayList<User>();
		
		generadorLesion = 1;
	}
	
	public static SerieNacional getInstance() {
		if (serie == null)
			serie = new SerieNacional();
		
		return serie;
	}
	
	public static void setSerie(SerieNacional serie) {
		SerieNacional.serie = serie;
	}

	public ArrayList<Equipo> getMisEquipos() {
		return misEquipos;
	}

	public void setMisEquipos(ArrayList<Equipo> misEquipos) {
		this.misEquipos = misEquipos;
	}

	public ArrayList<Jugador> getMisJugadores() {
		return misJugadores;
	}

	public void setMisJugadores(ArrayList<Jugador> misJugadores) {
		this.misJugadores = misJugadores;
	}

	public ArrayList<Juego> getMisJuegos() {
		return misJuegos;
	}

	public void setMisJuegos(ArrayList<Juego> misJuegos) {
		this.misJuegos = misJuegos;
	}

	public int getGeneradorEquipo() {
		return generadorEquipo;
	}

	public int getGeneradorJugador() {
		return generadorJugador;
	}

	public int getGeneradorJuego() {
		return generadorJuego;
	}

	public int getGeneradorLesion() {
		return generadorLesion;
	}
	
	public Equipo searchEquipoById(String id, ArrayList<Equipo> auxList) {
		for (Equipo auxI: auxList)
		{
			if (auxI.getId().equals(id))
				return auxI;
		}
		
		return null;
	}

	public Jugador searchJugadorById(String id, ArrayList<Jugador> auxList) {
		for (Jugador auxI: auxList)
		{
			if (auxI.getId().equals(id))
				return auxI;
		}
		
		return null;
	}	
	
	public Juego searchJuegoById(String id, ArrayList<Juego> auxList) {
		for (Juego auxI: auxList)
		{
			if (auxI.getId().equals(id))
				return auxI;
		}
		
		return null;
	}	
	
	public Lesion searchLesionByIdInPlayer(String id, ArrayList<Lesion> auxList) {
		for (Lesion auxI: auxList)
		{
			if (auxI.getId().equals(id))
				return auxI;
		}
		
		return null;
	}	
	
	public Lesion searchLesionById(String id, ArrayList<Jugador> auxList) {
		for (Jugador auxI: auxList)
		{
			for (Lesion auxJ: auxI.getMisLesiones())
				if (auxJ.getId().equals(id))
					return auxJ;
		}
		
		return null;
	}	
	
	public void guardarEquipo(Equipo aux){
		misEquipos.add(aux);
		generadorEquipo++;
	}
	
	public void guardarJugador(Jugador aux){
		Equipo equipo = searchEquipoById(aux.getEquipo().getId(), misEquipos);
		if (equipo != null)
		{
			misJugadores.add(aux);
			equipo.getJugadores().add(aux);
			generadorJugador++;
		}
	}
	
	public void guardarJuego(Juego aux){
		misJuegos.add(aux);
		generadorJuego++;
	}
	
	public void guardarLesion(Lesion aux){
		Jugador jug = searchJugadorById(aux.getJugador().getId(), misJugadores);
		if (jug != null) {
			jug.getMisLesiones().add(aux);
			generadorLesion++;
		}
	}
	
	public void modificarEquipo(Equipo aux) {
		Equipo update = searchEquipoById(aux.getId(), misEquipos);
		if (update != null)
			update.actualizarDatos(aux);
		return;
	}
	
	public void modificarJugador(Jugador aux) {
		Jugador update = searchJugadorById(aux.getId(), misJugadores);
		if (update != null)
		{
			if(!update.getEquipo().getId().equals(aux.getEquipo().getId()))
			{
				Equipo equipoViejo = searchEquipoById(update.getEquipo().getId(), misEquipos);
				Equipo equipoNuevo = searchEquipoById(aux.getEquipo().getId(), misEquipos);
				
				equipoViejo.getJugadores().remove(update);
				update.actualizarDatos(aux);
				equipoNuevo.getJugadores().add(update);
			}
			else
				update.actualizarDatos(aux);
		}
		return;
	}
	
	public void modificarJuego(Juego aux) {
		Juego update = searchJuegoById(aux.getId(), misJuegos);
		if (update != null)
			update.actualizarDatos(aux);
		return;
	}
	
	public void modificarLesion(Lesion aux) {
		Jugador jug = searchJugadorById(aux.getJugador().getId(), misJugadores);
		Lesion update = searchLesionByIdInPlayer(aux.getId(), jug.getMisLesiones());
		
		if (update != null && jug != null)
			update.actualizarDatos(aux);
		return;
	}
	
	public void eliminarLesion(Lesion aux) {
		Jugador jug = searchJugadorById(aux.getJugador().getId(), misJugadores);
		if (jug == null)
			return;
		
		Lesion del = searchLesionByIdInPlayer(aux.getId(), jug.getMisLesiones());
		jug.getMisLesiones().remove(del);
	}

	public ArrayList<User> getMisUsuarios() {
		return misUsers;
	}

	public void setMisUsuarios(ArrayList<User> misUsuarios) {
		this.misUsers = misUsuarios;
	}
	
	public void regUser(User user) {
		misUsers.add(user);	
	}
	
	public void eliminarUser(User aux) {
		misUsers.remove(aux);
	}
	
	public User buscarUser(String user) {
		for (User userI: misUsers)
		{
			if (userI.getUserName().equals(user))
				return userI;
		}
		
		return null;
	}
	
	public static User getLoginUser() {
		return loginUser;
	}

	public static void setLoginUser(User loginUser) {
		SerieNacional.loginUser = loginUser;
	}

	public boolean confirmLogin(String userName, String pass) {
		boolean login = false;
		for (User user : misUsers) {
			if(user.getUserName().equals(userName) && user.getPass().equals(pass)){
				loginUser = user;
				login = true;
			}
		}
		return login;
	}
	
	public void generarJuegos() {
	    misJuegos.clear();
	    generadorJuego = 1;
	    
	    if (misEquipos.size() < 5) {
	        return; 
	    }
	    
	    for (int i = 0; i < misEquipos.size(); i++) 
	    {
	        Equipo home = misEquipos.get(i);
	        
	        for (int j = 0; j < misEquipos.size(); j++) 
	        {
	            if (i != j) 
	            {
	                Equipo away = misEquipos.get(j);
	                
	                String idJuego = "JU-" + generadorJuego;
	                Juego nuevoJuego = new Juego(idJuego, home, away);
	                
	                misJuegos.add(nuevoJuego);
	                generadorJuego++;
	            }
	        }
	    }
	}
}

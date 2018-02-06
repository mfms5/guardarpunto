package es.sidelab.guardar_punto;

//import java.util.List;

import javax.persistence.*;

@Entity
public class Comentario {
	
	/*Atributos*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column 
	private String texto;
	
	/*Relaciones*/
	@ManyToOne
	private Juego juego;
	
	@ManyToOne
	private Usuarios user;
	
	/*Handlers*/
	public Juego getJuego() {
		return juego;
	}

	public void setJuego(Juego juego) {
		this.juego = juego;
	}

	public Usuarios getUser() {
		return user;
	}

	public void setUser(Usuarios user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}

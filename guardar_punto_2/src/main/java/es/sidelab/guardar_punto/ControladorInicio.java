package es.sidelab.guardar_punto;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ControladorInicio {

	//Repositorios
	@Autowired
	private UsuariosRepository repositoryUsuario;	
	@Autowired
	private JuegoRepository repositoryJuego;
	@Autowired
	private ReviewRepository repositoryReview;
	@Autowired
	private ComentariosRepository repositoryComentario;
	@Autowired
	private EstadoRepository repositoryEstados;
	
	//Obtener usuario logueado
	@Autowired
	private UserComponent userComponent;
	
	//Listas auxiliares
	//Lista de juegos destacados que se van a mostrar en la pagina de inicio
	private List<Juego> listaJuegosDestacados = new ArrayList<Juego>();
	
	@PostConstruct
	public void init() {		
		//repositoryUsuario.save(new Usuarios("user", "pass", "ROLE_USER"));
		
		/*** Listas auxiliares ***/
		listaJuegosDestacados.add(repositoryJuego.findOne(1));
		listaJuegosDestacados.add(repositoryJuego.findOne(2));
		listaJuegosDestacados.add(repositoryJuego.findOne(6));		
		listaJuegosDestacados.add(repositoryJuego.findOne(7));
	}
	
	//Pagina inicial. 
	//Muestra algunos juegos destacados, almacenados previamente en una lista	
	@GetMapping("/")
	public String Inicio(Model model, HttpServletRequest request) {
		model.addAttribute("listaJuegosDestacados", listaJuegosDestacados);
		String displayLogin = "block";
		String displayTusJuegos = "none";
		
		//Si hay un usuario logueado, se muestran sus juegos jugados en lugar del panel de login
		List<Juego> jugados = new ArrayList<Juego>(); //vacia si no hay ningun usuario logueado
		if(userComponent.isLoggedUser()) {
			Usuarios loggedUser = userComponent.getLoggedUser();			
			jugados = new ArrayList<Juego>(repositoryEstados.findByStateAndEstadouser("jugado", loggedUser));
			displayLogin = "none";
			displayTusJuegos = "block";
		}
		
		
		model.addAttribute("listaJuegosUsuario", jugados);
		model.addAttribute("displayLogin", displayLogin);
		model.addAttribute("displayTusJuegos", displayTusJuegos);
		
		CsrfToken token = (CsrfToken) request.getAttribute("_csrf"); 
		model.addAttribute("token", token.getToken());   
		/*CsrfToken tokenReg = (CsrfToken) request.getAttribute("_csrfReg"); 
		model.addAttribute("tokenReg", tokenReg.getToken());*/
		
		return "Inicio";
	}
	
    @GetMapping("/loginerror")
    public String loginerror() {
    	return "loginerror";
    }
	
	//Registrar nuevo usuario
	@PostMapping("/nuevoUsuario")
	public String nuevoUsuario (Model model, Usuarios usuario, HttpServletRequest request) {
		//Guardar el nuevo usuario en la db
		repositoryUsuario.save(usuario);
		EviarMail e= new EviarMail();
		e.sendEmail(usuario.getNombre(), usuario.getEmail());
		Inicio(model, request);
		return "Inicio";
	}
}

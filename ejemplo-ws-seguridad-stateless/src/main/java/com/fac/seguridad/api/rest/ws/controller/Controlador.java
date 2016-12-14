package com.fac.seguridad.api.rest.ws.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fac.seguridad.api.rest.tocken.accesoseguridadstateless.exception.BadTockenException;
import com.fac.seguridad.api.rest.tocken.accesoseguridadstateless.handler.DefaultAuthenticationAccessDeniedHandler;
import com.fac.seguridad.api.rest.tocken.accesoseguridadstateless.services.AccesoCreateTokenService;

@RestController
public class Controlador {
	private static final String AUTH_HEADER_NAME = "Authorization";

	@Autowired(required = false)
	AccesoCreateTokenService accesoCreateTokenService;

	@Autowired(required = false)
	DefaultAuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

	@RequestMapping(value = { "/noticiaAdmin" }, method = {RequestMethod.GET })
	public ResponseEntity<List<Noticia>> obtenerNoticiasAdmin() {
		Noticia promocion = new Noticia(this, 1, "Noticia nro 1 paa el Administrador");

		List<Noticia> noticias = new ArrayList<Noticia>();

		noticias.add(promocion);

		return new ResponseEntity<List<Noticia>>(noticias, HttpStatus.OK);
	}

	@RequestMapping(value = { "/noticiaAsistente" }, method = {RequestMethod.GET })
	public ResponseEntity<List<Noticia>> obtenerNoticiasAsistente() {
		Noticia promocion = new Noticia(this, 1, "Noticia nro 1 para asistente y administrador");

		List<Noticia> noticias = new ArrayList<Noticia>();

		noticias.add(promocion);

		return new ResponseEntity<List<Noticia>>(noticias, HttpStatus.OK);
	}

	@RequestMapping(value = { "/noticiaConductor" }, method = {RequestMethod.GET })
	public ResponseEntity<List<Noticia>> obtenerNoticiasConductor() {
		Noticia promocion = new Noticia(this, 1, "Noticia nro 1 el conductor");

		List<Noticia> noticias = new ArrayList<Noticia>();

		noticias.add(promocion);

		return new ResponseEntity<List<Noticia>>(noticias, HttpStatus.OK);
	}

	@RequestMapping(value = { "/tocken" }, method = {RequestMethod.GET })
	public void obtenerTocken(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		try {
			resp.setHeader("Authorization", this.accesoCreateTokenService.createTokenForUser(req));
			resp.setStatus(HttpStatus.OK.value());
		} catch (BadTockenException ex) {
			System.out.println(ex.getMessage());
			this.authenticationAccessDeniedHandler.handle((HttpServletRequest) request, (HttpServletResponse) response,
					new AccessDeniedException("Unauthorized"));
		}
	}
	
	
	
	public class Noticia {
	  private int id;
	  private String nombre;

	  public Noticia(Controlador paramControlador, int paramInt, String paramString)
	  {
	    this.id = paramInt;
	    this.nombre = paramString;
	  }

	  public int getId() {
	    return this.id; }

	  public void setId(int id) {
	    this.id = id; }

	  public String getNombre() {
	    return this.nombre; }

	  public void setNombre(String nombre) {
	    this.nombre = nombre;
	  }
	}
}

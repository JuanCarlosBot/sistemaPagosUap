package com.pago.uap.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pago.uap.model.entity.Usuario;
import com.pago.uap.model.service.IUsuarioService;
import javax.servlet.http.HttpSession;
@Controller
public class loginController {
    
    @Autowired
    private IUsuarioService usuarioService; 

    @GetMapping(value="/login")
    public String login(){
        return "login";
    }

    @PostMapping(value="/login")
    public String login(@RequestParam("user") String user, @RequestParam("password") String password, HttpServletRequest request){

        Usuario usuario = usuarioService.loginUsuario(user, password);
        
        if (usuario != null) {

            HttpSession session = request.getSession(false);
            session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            
            return "redirect:/persona";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping("/cerrar_sesion")
	public String cerrarSesion(HttpServletRequest request, RedirectAttributes flash) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.invalidate();
			flash.addAttribute("validado", "Se cerro sesion con exito!");
		}
		return "redirect:/login";
	}
}

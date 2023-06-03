package com.pago.uap.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.pago.uap.model.entity.Municipio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pago.uap.model.entity.Usuario;
import com.pago.uap.model.service.IGestionService;
import com.pago.uap.model.service.IMunicipioService;

@Controller
public class MunicipioController {
    
    @Autowired
    private IMunicipioService municipioService;
    @Autowired
    private IGestionService gestionService;

    @GetMapping(value ="/Vmunicipio")
    public String MunicipioFormulario(Model model, HttpServletRequest request){
        System.out.println("AAAAAAA");
        if(request.getSession().getAttribute("usuario")!=null){
            System.out.println("BBBBBB");
            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
            model.addAttribute("usuario", usuario);  
            model.addAttribute("gestiones", gestionService.listaGestionesAll());             
            //--------------------------------------------
            model.addAttribute("municipio", new Municipio());
            model.addAttribute("listaMunicipio", municipioService.listaMunicipiosAll());
        
            return "municipiok/municipiop";
        
        }else{
            return "redirect:/login";
        }
    }
    
    @PostMapping("/guardarMunicipio")
    public String guardarMunicipio(@ModelAttribute Municipio municipio, RedirectAttributes redirectAttrs) {
        municipio.setEstado("A");
        municipioService.guardarMunicipio(municipio);
        redirectAttrs
                .addFlashAttribute("mensaje", "Se guardo correctamente el Municipio")
                .addFlashAttribute("clase", "success alert-dismissible fade show");
        return "redirect:/Vmunicipio"; // Redirige a una página de éxito o a otra vista después de guardar los datos
    }

    @PostMapping("/guardarMuncipioEditado")
    public String guardarMunicipioEditado(@ModelAttribute @Valid Municipio municipio, BindingResult bindingResult, RedirectAttributes redirectAttrs){                      
        municipio.setEstado("A");
        municipioService.guardarMunicipio(municipio);
        
        redirectAttrs
            .addFlashAttribute("mensaje", "Se actualizo correctamente el Municipio")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/Vmunicipio";
    }

    //---------------Para hacer el modificar y eliminar___2

    @GetMapping(value="/modificarMunicipio/{id_municipio}")
    public String editarMunicipio(@PathVariable(value="id_municipio")Long id_municipio, Model model, HttpServletRequest request){
        if(request.getSession().getAttribute("usuario")!=null){
            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
            
            Municipio municipio = municipioService.sacarIdMunicipio(id_municipio);
            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("editMode", "true");
            model.addAttribute("municipio", municipio);
            model.addAttribute("listaMunicipios", municipioService.listaMunicipiosAll());
            return "municipiok/municipiop";
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping(value="/eliminarMunicipio/{id_municipio}")
    public String eliminarMunicipio(@PathVariable(value="id_municipio") Long id_municipio, HttpServletRequest request,  RedirectAttributes redirectAttrs){
        if(request.getSession().getAttribute("usuario") != null){
           
            Municipio municipio = municipioService.sacarIdMunicipio(id_municipio);
            municipio.setEstado("X");
            municipioService.guardarMunicipio(municipio);
            redirectAttrs
            .addFlashAttribute("mensaje", "Se Eliminado correctamente el Municipio")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
            return "redirect:/Vmunicipio";
        } else {
            return "redirect:/login";
        }
    }
}

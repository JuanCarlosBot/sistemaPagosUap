package com.pago.uap.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.service.IPersonaService;

@Controller
public class homeController {
    
    @Autowired
    private IPersonaService personaService;

    @GetMapping(value = "/home")
    public String PersonaFormulario(Model model){
        model.addAttribute("persona", new Persona());
        model.addAttribute("listaPersonas", personaService.listaPersonasAll());
        return "persona/persona";
    }
    @GetMapping(value = "/personasAll")
    public String PersonasAll(Model model){
        model.addAttribute("listaPersonas", personaService.listaPersonasAll());
        return "persona/personasAll";
    }

    @PostMapping("/guardarPersona")
    public String guardarPersona(@ModelAttribute @Valid Persona persona, BindingResult bindingResult, RedirectAttributes redirectAttrs){
        try {
            if (bindingResult.hasErrors()) {
                // Aquí se puede hacer cualquier cosa, yo hago una redirección para mostrar los errores en el form
                return "persona/persona";
            }
            personaService.guardarPersona(persona);
            redirectAttrs
                .addFlashAttribute("mensaje", "Se guardo correctamente la persona")
                .addFlashAttribute("clase", "success alert-dismissible fade show");
            
            return "redirect:/home";  
        } catch (Exception e) {
            System.out.println("ya existe ci");
            redirectAttrs
            .addFlashAttribute("mensaje", "El ci "+persona.getCi_persona()+" se encuentra registrado!")
            .addFlashAttribute("clase", "danger alert-dismissible fade show");
            return "redirect:/home";
        }
        
    }

    @GetMapping(value="/modificarPersona/{id_persona}")
    public String editarPersona(@PathVariable(value="id_persona")Long id_persona, Model model){
        Persona persona = personaService.sacarIdPersona(id_persona);
        model.addAttribute("editMode", "true");
        model.addAttribute("persona", persona);
        model.addAttribute("listaPersonas", personaService.listaPersonasAll());
        return "persona/persona";
    }

    @PostMapping("/guardarPersonaEditado")
    public String guardarPersonaEditado(@ModelAttribute @Valid Persona persona, BindingResult bindingResult, RedirectAttributes redirectAttrs){
        
        if (bindingResult.hasErrors()) {
            // Aquí se puede hacer cualquier cosa, yo hago una redirección para mostrar los errores en el form
            return "persona/persona";
        }
        
        personaService.guardarPersona(persona);
        
        redirectAttrs
            .addFlashAttribute("mensaje", "Se actualizo correctamente la persona")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/home";
    }
    
    @GetMapping("/cancelarEditPersona")
    public String cancelarEditPersona(){
        return "redirect:/home";
    }
}

package com.pago.uap.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pago.uap.model.entity.EstadoPago;
import com.pago.uap.model.entity.Usuario;
import com.pago.uap.model.service.IEstadoPagoService;
import com.pago.uap.model.service.IGestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PagoController {
    
    @Autowired
    private IEstadoPagoService estadoPagoService;
    @Autowired
    private IGestionService gestionService;

    @GetMapping(value = "/VEstadoPago")
    public String VentanaLocalidad(Model model, HttpServletRequest request) {
        if(request.getSession().getAttribute("usuario")!=null){
            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("estadoPago", new EstadoPago());
            model.addAttribute("ListestadoPago", estadoPagoService.listaEstadoPagosAll());
            return "pago/registrar";
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(value="/GuardarEstadoPago")
    public String GuardarEstadoPago(@Validated EstadoPago estadoPago, RedirectAttributes redirectAttrs, HttpServletRequest request, Model model) {
        
        estadoPago.setEstado("A");
        estadoPagoService.guardarEstadoPago(estadoPago);
        redirectAttrs
            .addFlashAttribute("mensaje", "Se ha Registrado correctamente el Estado de Pago")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/VEstadoPago";
    }

    @GetMapping(value = "/ModEstadoPago/{id_estado_pago}")
    public String ModEstadoPago(@PathVariable(value="id_estado_pago")Long id_estado_pago, Model model, HttpServletRequest request) {
        if(request.getSession().getAttribute("usuario")!=null){
            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("estadoPago", estadoPagoService.sacarIdEstadoPago(id_estado_pago));
            model.addAttribute("ListestadoPago", estadoPagoService.listaEstadoPagosAll());
            model.addAttribute("editMode", "true");
            return "pago/registrar";
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(value="/GuardarModEstadoPago")
    public String GuardarModLocalidad(@Validated EstadoPago estadoPago, RedirectAttributes redirectAttrs, HttpServletRequest request, Model model) {
        
        estadoPago.setEstado("A");
        estadoPagoService.guardarEstadoPago(estadoPago);
        
        redirectAttrs
            .addFlashAttribute("mensaje", "Se actualizo correctamente el Estado de Pago")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/VEstadoPago";
    }

    @GetMapping(value="/EliminarEstadoPago/{id_estado_pago}")
    public String EliminarLocalidad(@PathVariable(value="id_estado_pago")Long id_estado_pago, RedirectAttributes redirectAttrs, HttpServletRequest request, Model model) {
        EstadoPago estadoPago = estadoPagoService.sacarIdEstadoPago(id_estado_pago);
        estadoPago.setEstado("X");
        estadoPagoService.guardarEstadoPago(estadoPago);
        
        redirectAttrs
            .addFlashAttribute("mensaje", "Se ha Eliminado correctamente el Estado de Pago")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/VEstadoPago";
    }
}


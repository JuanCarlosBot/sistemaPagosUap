package com.pago.uap.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pago.uap.model.entity.Localidad;
import com.pago.uap.model.entity.Usuario;
import com.pago.uap.model.service.IGestionService;
import com.pago.uap.model.service.ILocalidadService;
import com.pago.uap.model.service.IMunicipioService;
import com.pago.uap.model.service.ITipoLocalidadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LocalidadController {
    
    @Autowired
    private IMunicipioService municipioService;
    @Autowired
    private ITipoLocalidadService tipoLocalidadService;
    @Autowired
    private ILocalidadService localidadService;
    @Autowired
    private IGestionService gestionService;
    
    @GetMapping(value = "/VLocalidad")
    public String VentanaLocalidad(Model model, HttpServletRequest request) {
        if(request.getSession().getAttribute("usuario")!=null){
            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
            /*for (int i = 0; i < 128; i++) {
                try {
                    Localidad l = new Localidad();
                    l.setEstado("A");
                    localidadService.guardarLocalidad(l);
                    System.out.println(l.getId_localidad()+"dfgfffffffff");
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }*/
            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("municipios", municipioService.listaMunicipiosAll());
            model.addAttribute("localidades", localidadService.listaLocalidadesAll());
            model.addAttribute("Tipolocalidades", tipoLocalidadService.listaTipoLocalidadesAll());
            model.addAttribute("localidad", new Localidad());
            return "localidad/registrar";
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(value="/GuardarLocalidad")
    public String GuardarLocalidad(@Validated Localidad localidad, RedirectAttributes redirectAttrs, HttpServletRequest request, Model model) {
        //localidad.setId_localidad(localidadService.UltimoId()+1);
        //System.out.println("----------------ultimo id "+(localidadService.UltimoId()+1));
        localidad.setEstado("A");
        localidadService.guardarLocalidad(localidad);
        
        redirectAttrs
            .addFlashAttribute("mensaje", "Se ha Registrado correctamente a la Localidad")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/VLocalidad";
    }

    @GetMapping(value = "/ModLocalidad/{id_localidad}")
    public String VentanaMODLocalidad(@PathVariable(value="id_localidad")Long id_localidad, Model model, HttpServletRequest request) {
        if(request.getSession().getAttribute("usuario")!=null){
            Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("municipios", municipioService.listaMunicipiosAll());
            model.addAttribute("localidades", localidadService.listaLocalidadesAll());
            model.addAttribute("Tipolocalidades", tipoLocalidadService.listaTipoLocalidadesAll());
            model.addAttribute("localidad", localidadService.sacarIdLocalidad(id_localidad));
            model.addAttribute("editMode", "true");
            return "localidad/registrar";
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(value="/GuardarModLocalidad")
    public String GuardarModLocalidad(@Validated Localidad localidad, RedirectAttributes redirectAttrs, HttpServletRequest request, Model model) {
        

        
        localidad.setEstado("A");
        localidadService.guardarLocalidad(localidad);
        
        redirectAttrs
            .addFlashAttribute("mensaje", "Se actualizo correctamente la Localidad")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/VLocalidad";
    }

    @GetMapping(value="/EliminarLocalidad/{id_localidad}")
    public String EliminarLocalidad(@PathVariable(value="id_localidad")Long id_localidad, RedirectAttributes redirectAttrs, HttpServletRequest request, Model model) {
        Localidad localidad = localidadService.sacarIdLocalidad(id_localidad);
        localidad.setEstado("X");
        localidadService.guardarLocalidad(localidad);
        
        redirectAttrs
            .addFlashAttribute("mensaje", "Se ha Eliminado correctamente la Localidad")
            .addFlashAttribute("clase", "success alert-dismissible fade show");
        
        return "redirect:/VLocalidad";
    }
}

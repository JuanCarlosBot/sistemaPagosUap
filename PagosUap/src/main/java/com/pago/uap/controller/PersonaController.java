package com.pago.uap.controller;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pago.uap.model.entity.Cargo;
import com.pago.uap.model.entity.EstadoPago;
import com.pago.uap.model.entity.Gestion;
import com.pago.uap.model.entity.Localidad;
import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.entity.TipoCargo;
import com.pago.uap.model.entity.Usuario;
import com.pago.uap.model.service.ICargoService;
import com.pago.uap.model.service.IContratoService;
import com.pago.uap.model.service.IEstadoPagoService;
import com.pago.uap.model.service.IGestionService;
import com.pago.uap.model.service.ILocalidadService;
import com.pago.uap.model.service.IMunicipioService;
import com.pago.uap.model.service.IPersonaService;
import com.pago.uap.model.service.ITipoCargoService;

@Controller
public class PersonaController {

    @Autowired
    private IPersonaService personaService;
    @Autowired
    private IMunicipioService municipioService;
    @Autowired
    private ILocalidadService localidadService;
    @Autowired
    private ICargoService cargoService;
    @Autowired
    private IEstadoPagoService estadoPagoService;
    @Autowired
    private ITipoCargoService tipoCargoService;
    @Autowired
    private IGestionService gestionService;
    @Autowired
    private IContratoService contratoService;
/*  @Autowired
    private V_todo v_todo;*/

    
    
    @GetMapping(value = { "/", "/persona" })
    public String PersonaFormulario(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("persona", new Persona());
            model.addAttribute("listaPersonas", personaService.listaPersonasAll());
            model.addAttribute("municipios", municipioService.listaMunicipiosAll());
            model.addAttribute("tipoCargos", tipoCargoService.listaTipoCargosAll());
            return "persona/persona";
            /*
             * for (int i = 0; i < 3000; i++) {
             * try {
             * Cargo cargo = new Cargo();
             * cargo.setFecha_inicio(new Date());
             * cargoService.guardarCargo(cargo);
             * System.out.println(cargo.getId_cargo()+" iddddddddddddddd");
             * } catch (Exception e) {
             * // TODO: handle exception
             * }
             * }
             */

        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/personasAll")
    public String PersonasAll(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("listaPersonas", personaService.listaPersonasAll());
            model.addAttribute("localidades", localidadService.listaLocalidadesAll());
            return "persona/personasAll";
        } else {
            return "redirect:/login";
        }

    }

    @GetMapping("/personaGestion/{id_gestion}")
    public String personasPorGestion(
            @PathVariable(value = "id_gestion") Long id_gestion, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

            

            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            List<Persona> personas = personaService.listarPersonasPorGestion(id_gestion);
            model.addAttribute("listaPersonas", personas);
            return "persona/personasAll";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/guardarPersona")
    public String guardarPersona(@ModelAttribute("persona") @Valid Persona persona,
            BindingResult bindingResult, RedirectAttributes redirectAttrs, HttpServletRequest request, Model model) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        try {
            if (bindingResult.hasErrors()) {
                List<ObjectError> errors = bindingResult.getAllErrors();
                for (ObjectError error : errors) {
                    System.out.println(error.getDefaultMessage());

                }
                // Aquí se puede hacer cualquier cosa, yo hago una redirección para mostrar los
                // errores en el form
                model.addAttribute("usuario", usuario);
                model.addAttribute("gestiones", gestionService.listaGestionesAll());
                model.addAttribute("municipios", municipioService.listaMunicipiosAll());
                model.addAttribute("tipoCargos", tipoCargoService.listaTipoCargosAll());
                return "persona/persona";
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            String anioActual = year + "";
            Gestion gestionA;
            boolean encontrada = false;
            for (Gestion palabra : gestionService.listaGestionesAll()) {
                if (palabra.getNombre_gestion().equals(anioActual)) {
                    encontrada = true;
                    break;
                }
            }
            if (encontrada) {
                Gestion gestion1 = gestionService.sacarGestionPorNombre(anioActual);
                gestionA = gestion1;
            } else {
                Gestion gestion = new Gestion();
                gestion.setNombre_gestion(anioActual);
                gestionService.guardarGestion(gestion);
                gestionA = gestion;
            }

            String id_tipo_carg = request.getParameter("id_tipo_cargo");
            long id_tipo_cargo = Long.parseLong(id_tipo_carg);

            TipoCargo tipoCargo = tipoCargoService.sacarIdTipoCargo(id_tipo_cargo);

            EstadoPago estadoPago = estadoPagoService.sacarIdEstadoPago(2l);

            //contrar personas por anio
            int numeroContrato = cargoService.numerarPorGestion(gestionA.getId_gestion())+1;
            String contratoString = String.valueOf(numeroContrato);
            int cantidadDigitos = contratoString.length();
            String textoNumeracion="";
            if (cantidadDigitos==1) {
                textoNumeracion="000"+contratoString;
            }else if (cantidadDigitos==2) {
                textoNumeracion="00"+contratoString;
            }else if (cantidadDigitos==3) {
                textoNumeracion="0"+contratoString;
            }
            else if (cantidadDigitos>=4) {
                textoNumeracion=contratoString;
            }
            System.out.println("numero de personas por 2023="+ textoNumeracion);


            Cargo cargo = new Cargo();
            cargo.setEstadoPago(estadoPago);
            cargo.setTipoCargo(tipoCargo);
            cargo.setGestion(gestionA);
            cargo.setNumeracion(textoNumeracion);
            cargoService.guardarCargo(cargo);

            persona.setNombre_completo_persona(persona.getAp_paterno_persona() + " " + persona.getAp_materno_persona()
                    + " " + persona.getNombre_persona());
            persona.setFecha_registro_persona(new Date());
            persona.setEstado_persona("A");
            persona.setCargo(cargo);
            personaService.guardarPersona(persona);
            redirectAttrs
                    .addFlashAttribute("mensaje", "Se guardo correctamente la persona")
                    .addFlashAttribute("clase", "success alert-dismissible fade show");

            return "redirect:/persona";
        } catch (Exception e) {
            System.out.println("ya existe ci");
            redirectAttrs
                    .addFlashAttribute("mensaje", "El ci " + persona.getCi_persona() + " ya se encuentra registrado!")
                    .addFlashAttribute("clase", "danger alert-dismissible fade show");
            return "redirect:/persona";
        }

    }

    @GetMapping(value = "/modificarPersona/{id_persona}")
    public String editarPersona(@PathVariable(value = "id_persona") Long id_persona, Model model,
            HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

            model.addAttribute("usuario", usuario);
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            Persona persona = personaService.sacarIdPersona(id_persona);
            model.addAttribute("editMode", "true");
            model.addAttribute("persona", persona);
            model.addAttribute("listaPersonas", personaService.listaPersonasAll());
            return "persona/persona";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/guardarPersonaEditado")
    public String guardarPersonaEditado(@ModelAttribute @Valid Persona persona, BindingResult bindingResult,
            RedirectAttributes redirectAttrs) {

        if (bindingResult.hasErrors()) {
            // Aquí se puede hacer cualquier cosa, yo hago una redirección para mostrar los
            // errores en el form
            return "persona/persona";
        }

        personaService.guardarPersona(persona);

        redirectAttrs
                .addFlashAttribute("mensaje", "Se actualizo correctamente la persona")
                .addFlashAttribute("clase", "success alert-dismissible fade show");

        return "redirect:/persona";
    }

    @GetMapping("/cancelarEditPersona")
    public String cancelarEditPersona() {
        return "redirect:/persona";
    }

    @RequestMapping(value = "/getLocalidades", method = RequestMethod.GET)
    public @ResponseBody List<Localidad> findAllLocalidades(
            @RequestParam(value = "municipioId", required = true) Long id_municipio) {
        List<Localidad> localidades = localidadService.localidadesPorIdMunicipio(id_municipio);
        return localidades;
    }

}

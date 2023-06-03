package com.pago.uap.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pago.uap.CertificateServicee;
import com.pago.uap.Config;
import com.pago.uap.model.entity.Cargo;
import com.pago.uap.model.entity.Contrato;
import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.service.ICargoService;
import com.pago.uap.model.service.IContratoService;
import com.pago.uap.model.service.IPersonaService;

@Controller
public class CertificateController {

    Config config = new Config();

    @Autowired
    private CertificateServicee certificateService;
    @Autowired
    private IPersonaService personaService;
    @Autowired
    private IContratoService contratoService;
    @Autowired
    private ICargoService cargoService;

    @PostMapping("/generarContrato")
    public String generarCertificadoPost(RedirectAttributes redirectAttrs,
        @RequestParam(value = "id_person")Long id_persona,
        @RequestParam(value = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechainicio,
        @RequestParam(value = "fecha_fin")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechafin
        ){

        
        
        Persona persona = personaService.sacarIdPersona(id_persona);
        Cargo cargo = cargoService.sacarIdCargo(persona.getCargo().getId_cargo());
        cargo.setFecha_inicio(fechainicio);
        cargo.setFecha_fin(fechafin);
        cargoService.guardarCargo(cargo);

        certificateService.generateCertificate(id_persona, fechainicio, fechafin);
        redirectAttrs
                .addFlashAttribute("mensaje", "Contrato generado correctamente con ci: " + persona.getCi_persona())
                .addFlashAttribute("clase", "success alert-dismissible fade show");
        return "redirect:/personaGestion/"+cargo.getGestion().getId_gestion();
    }
    
    @PostMapping("/GenerarContratosAll")
    public String enviarSeleccionados(@RequestParam(value="seleccionados", required = false) List<Long> id_persona,
    @RequestParam(value = "fecha_inicio")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechainicio,
        @RequestParam(value = "fecha_fin")@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechafin, RedirectAttributes redirectAttrs) {
        Cargo cargo1 = new Cargo();
        if (!id_persona.isEmpty()) {
            
        for (Long long1 : id_persona) {
            Persona persona = personaService.sacarIdPersona(long1);
            Cargo cargo = cargoService.sacarIdCargo(persona.getCargo().getId_cargo());
            cargo.setFecha_inicio(fechainicio);
            cargo.setFecha_fin(fechafin);
            cargoService.guardarCargo(cargo);
            certificateService.generateCertificate(long1, fechainicio, fechafin);
            cargo1=cargo;
            System.out.println(persona.getNombre_completo_persona());
            return "redirect:/personaGestion/"+cargo1.getGestion().getId_gestion();
        }
        }else{
            redirectAttrs
                .addFlashAttribute("mensaje", "Se requiere seleccionar al menos un registro!")
                .addFlashAttribute("clase", "warning alert-dismissible fade show");
                return "redirect:/persona";
        }
        return "redirect:/personaGestion/"+cargo1.getGestion().getId_gestion();
        
    }





    @RequestMapping(value = "/openFile/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody
    FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response, 
        @PathVariable("id") long id_contrato) throws FileNotFoundException {
        Contrato contrato = contratoService.sacarIdContrato(id_contrato);
        File file = new File("D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/"+ contrato.getRuta());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }
}

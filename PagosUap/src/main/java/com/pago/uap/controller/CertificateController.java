package com.pago.uap.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.DocumentException;
import com.pago.uap.CertificateServicee;
import com.pago.uap.Config;
import com.pago.uap.model.entity.Cargo;
import com.pago.uap.model.entity.Contrato;
import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.entity.Usuario;
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
        @RequestParam(value = "fecha_inicio", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechainicio,
        @RequestParam(value = "fecha_fin", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechafin,
        @RequestParam(value = "codigo", required = false)String codigo,HttpServletRequest req
        ) throws GeneralSecurityException, IOException, DocumentException{

            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        
        Persona persona = personaService.sacarIdPersona(id_persona);
        Cargo cargo1 = new Cargo();
        if (usuario.getId_usuario()==1 && persona.getContrato().size()==0) {
        Cargo cargo = cargoService.sacarIdCargo(persona.getCargo().getId_cargo());
        cargo.setFecha_inicio(fechainicio);
        cargo.setFecha_fin(fechafin);
        cargoService.guardarCargo(cargo);
          
        certificateService.generateCertificate(codigo, id_persona, fechainicio, fechafin);
        cargo1=cargo;
        }else if(usuario.getId_usuario()==2 && persona.getContrato().size()==1){
            Cargo cargo = cargoService.sacarIdCargo(persona.getCargo().getId_cargo());
            certificateService.generateCertificate(codigo, id_persona, cargo.getFecha_inicio(), cargo.getFecha_fin());
            cargo1=cargo;
            System.out.println(persona.getNombre_completo_persona());
            }else{
                redirectAttrs
                .addFlashAttribute("mensaje", "Error en seleccionar! vuelve a ingresar.")
                .addFlashAttribute("clase", "danger alert-dismissible fade show");
                return "redirect:/persona";
            }
        redirectAttrs
                .addFlashAttribute("mensaje", "Contrato generado correctamente con ci: " + persona.getCi_persona())
                .addFlashAttribute("clase", "success alert-dismissible fade show");
        return "redirect:/personaGestion/"+cargo1.getGestion().getId_gestion();
    }
    
    @PostMapping("/GenerarContratosAll")
    public String enviarSeleccionados(@RequestParam(value="seleccionados", required = false) List<Long> id_persona,
    @RequestParam(value = "fecha_inicio", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechainicio,
        @RequestParam(value = "fecha_fin", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date fechafin,
        @RequestParam(value = "codigo", required = false)String codigo,HttpServletRequest req,
         RedirectAttributes redirectAttrs) throws GeneralSecurityException, IOException, DocumentException {
        
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
            
            Cargo cargo1 = new Cargo();
        
        if (!id_persona.isEmpty()) {
           
        for (Long long1 : id_persona) {
            Persona persona = personaService.sacarIdPersona(long1);
            if (usuario.getId_usuario()==1 && persona.getContrato().size()==0) {
                Cargo cargo = cargoService.sacarIdCargo(persona.getCargo().getId_cargo());
                cargo.setFecha_inicio(fechainicio);
                cargo.setFecha_fin(fechafin);
                cargoService.guardarCargo(cargo);
                certificateService.generateCertificate(codigo, long1, fechainicio, fechafin);
                cargo1=cargo;
                System.out.println(persona.getNombre_completo_persona());
            } else if(usuario.getId_usuario()==2 && persona.getContrato().size()==1){
            Cargo cargo = cargoService.sacarIdCargo(persona.getCargo().getId_cargo());
            certificateService.generateCertificate(codigo, long1, cargo.getFecha_inicio(), cargo.getFecha_fin());
            cargo1=cargo;
            System.out.println(persona.getNombre_completo_persona());
            }else{
                redirectAttrs
                .addFlashAttribute("mensaje", "Error en seleccionar! vuelve a ingresar.")
                .addFlashAttribute("clase", "danger alert-dismissible fade show");
                return "redirect:/persona";
            }
        }
        }else{
            redirectAttrs
                .addFlashAttribute("mensaje", "Se requiere seleccionar al menos un registro!")
                .addFlashAttribute("clase", "warning alert-dismissible fade show");
                return "redirect:/persona";
        }
        return "redirect:/personaGestion/"+cargo1.getGestion().getId_gestion();
        
    }

    @GetMapping("/contratos/{id_contrato}")
    public String validacionContratos(@PathVariable("id_contrato")Long id_contrato, Model model){
        model.addAttribute("contrato", contratoService.sacarIdContrato(id_contrato));
        return "vitaContrato";
    }



    @RequestMapping(value = "/openFile/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody
    FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response, 
        @PathVariable("id") long id_contrato) throws FileNotFoundException {
        Contrato contrato = contratoService.sacarIdContrato(id_contrato);
        String rutaParaMostrar = Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/"+contrato.getRuta();
        File file = new File(rutaParaMostrar);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }
}

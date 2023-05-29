package com.pago.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.pago.uap.CertificateService;
import com.pago.uap.Config;
import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.service.IPersonaService;

@RestController
public class CertificateController {

    Config config = new Config();

    @Autowired
    private CertificateService certificateService;
    @Autowired
    private IPersonaService personaService; 

    @GetMapping("/certificates/{id}")
    public String generateCertificate(@PathVariable("id") Long id) {
        // Lógica para obtener el nombre y otros datos de la tabla según el ID
        Persona persona = personaService.sacarIdPersona(id);
        String name = "Nombre del participante"; // Ejemplo: obtener el nombre de una base de datos
        String nam= persona.getNombre_completo_persona();
        String filePath = "D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/certificados.pdf";
        //String filePath = config.guardarArchivo(ruta_icon);
        //String filePath = "ruta/del/certificado.pdf";
        certificateService.generateCertificate(id);

        return "Certificado generado y guardado en: " + filePath+" nm "+persona.getNombre_completo_persona();
    }
}

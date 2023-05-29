package com.pago.uap;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pago.uap.model.entity.Contrato;
import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.service.IContratoService;
import com.pago.uap.model.service.IPersonaService;

@Service
public class CertificateService {
    Config config = new Config(); 
    @Autowired 
    private IPersonaService personaService;
    @Autowired
    private IContratoService contratoService;

    public void generateCertificate(Long id_persona) {
        Persona persona = personaService.sacarIdPersona(id_persona);
        String name=persona.getNombre_completo_persona();
        String ci=persona.getCi_persona();
        String filePathh = "D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/certificados.pdf";
        MultipartFile fileFisico=convertToMultipartFile(filePathh);
        String ruta = persona.getId_persona()+"-"+config.guardarArchivo(fileFisico);

        Contrato contrato=new Contrato();
        contrato.setRuta(ruta);
        contrato.setPersona(persona);
        contratoService.guardarContrato(contrato);
        String rutaParaGenerar="D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/"+contrato.getRuta();
        
        generateContract(name, rutaParaGenerar);
        System.out.println("Contrato generado: " + contrato.getRuta());
        /*System.out.println(name+" -----------------");
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph("Certificado para: " + name));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static void generateContract(String name, String filePath) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Agrega el contenido del contrato
            addTitle(document);
            addParties(name,document);
            addTerms(document);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Contrato de Servicios");
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    public static void addParties(String name, Document document) throws DocumentException {
        Paragraph parties = new Paragraph("El senios "+name+" la Unidad de Sistemas de Información y Comunicación - USIC, solicita un recurso de interoperabilidad del Sistema Siringuero mediante una API REST, motivo por el cual remito “Formulario de Solicitud de Requerimiento de Información Interoperable” para que sea considerado por su autoridad:");
        parties.setSpacingAfter(10);
        document.add(parties);

        Paragraph party1 = new Paragraph("En el diagrama de carga de tolva se inicia una ves cuando el silo de acopio tenga la cantidad necesaria de materia prima es en cuanto es cargado a distintas tolvas que están identificadas de 1 al 12 y esa carga es mantenida en tolvas hasta un máximo de 36 horas.");
        document.add(party1);

        Paragraph party2 = new Paragraph("La gestión de producción y operaciones permite una planificación ideal, una organización adecuada y una supervisión final de los pasos que hacen parte de la línea de elaboración y entrega final del producto y/o servicio. De esta forma, se garantiza que la productividad empresarial se vea reflejada de forma efectiva y eficaz en los objetivos de la gestión de producción y operaciones para que los insumos disponibles se conviertan en bienes (Beetrack, 2019).");
        document.add(party2);

        document.add(new Paragraph("\n"));
    }

    public static void addTerms(Document document) throws DocumentException {
        Paragraph terms = new Paragraph("Términos y Condiciones:");
        terms.setSpacingAfter(10);
        document.add(terms);

        Paragraph term1 = new Paragraph("- Como se observa en la ilustración 2, en el proceso interviene actividades que pueden ser gestionadas en una secuencia de, pues aquí intervienen los diversos recursos tales como personas, materiales, información y equipamiento o maquinaria. Esta secuencia es el factor causal que tendrá, como efecto, un producto o salida para el cliente interno o externo. El cliente interno será todo el personal o área de la empresa que recibe el producto o resultado de un proceso, como información, datos, o productos semiprocesados. El cliente externo será quien recibe el producto terminado, ya sea el usuario o consumidor, o los distribuidores o mayoristas, por ejemplo.");
        document.add(term1);

        Paragraph term2 = new Paragraph("- Ya se mencionó las entradas, las secuencias y las salidas como elementos del proceso, sin embargo, Pérez (Pérez 2010) añade dos elementos más, los controles y los límites. Los controles corresponden a los procedimientos que permiten si el proceso está funcionando correctamente, mientras tanto los límites son la definición de la amplitud y profundidad del proceso, es decir, los criterios que establecen las unidades que gestionan el proceso, sus interacciones, elementos y factores (Perez, 2010).");
        document.add(term2);

        document.add(new Paragraph("\n"));
    }





//metodo para convertir ruta a multipartfile

public MultipartFile convertToMultipartFile(String filePath) {
    File file = new File(filePath);
    DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem("file", "text/plain", true, file.getName());
    try {
        fileItem.getOutputStream();
        fileItem.getOutputStream().write(((MultipartFile) file).getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return new CommonsMultipartFile(fileItem);
}
}

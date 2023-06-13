package com.pago.uap;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//para qr
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;////////
import java.util.HashMap;
import java.util.Map;
//para qr
import java.awt.image.BufferedImage;
import java.awt.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
//para firma digital
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.pago.uap.model.entity.Contrato;
import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.service.IContratoService;
import com.pago.uap.model.service.IPersonaService;

//md5
import java.math.BigInteger;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
@Service
public class CertificateServiceImpl implements CertificateServicee {
    Config config = new Config();
    @Autowired
    private IPersonaService personaService;
    @Autowired
    private IContratoService contratoService;

    @Override
    public void generateCertificate(String llave, Long id_persona, Date fechainicio, Date fechafin) throws GeneralSecurityException, IOException, DocumentException {

        
        Persona persona = personaService.sacarIdPersona(id_persona);
        int numeroContrato = contratoService.NumerarContrato(persona.getCargo().getGestion().getId_gestion())+1;
        String filePathh = Paths.get("").toAbsolutePath().toString()+"/PagosUap/src/main/resources/uploads/certificados.pdf";
        // String filePathh =
        // "D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/certificados.pdf";
        MultipartFile fileFisico = convertToMultipartFile(filePathh);
        String ruta = persona.getId_persona() + "-" + config.guardarArchivo(fileFisico);
        String tipoC = persona.getCargo().getTipoCargo().getNombre_tipo_cargo(); // beneficiario y lider
        System.out.println("-------------------------el cargo es: " + tipoC);
        String tipoL = persona.getLocalidad().getTipoLocalidad().getNombre_tipo_localidad(); // rural o urbano
        System.out.println("-----------------------area : " + tipoL);
        
        // String
        // rutaParaGenerar="D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/"+contrato.getRuta();
        
        
        String numeracion = persona.getCargo().getNumeracion();
        String nombre_c = persona.getNombre_completo_persona();
        String ci = persona.getCi_persona();
        String nom_localidad = persona.getLocalidad().getNombre_localidad();
        String nom_municipio = persona.getLocalidad().getMunicipio().getNombre_municipio();
        System.out.println("-----------------------la localidad es: " + nom_localidad);
        String gestion = persona.getCargo().getGestion().getNombre_gestion();
        // String fechai = "13/13/13";
        // String fechaf = "15/15/15";
        
        if (persona.getContrato().size()==0) {
            Contrato contrato = new Contrato();
        contrato.setRuta(ruta);
        contrato.setPersona(persona);
        contrato.setEstado("A");
        contratoService.guardarContrato(contrato);
        String rutaParaGenerar = Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/"
            + contrato.getRuta();
        generateContract(numeracion, nombre_c, ci, nom_localidad, nom_municipio, tipoL, fechainicio, fechafin, 
        rutaParaGenerar, tipoC, gestion);
        }else if(persona.getContrato().size()>=1){
            Contrato contrato = new Contrato();
        contrato.setRuta(ruta);
        contrato.setPersona(persona);
        contrato.setEstado("R");
        contratoService.guardarContrato(contrato);
        BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
        String rutaParaCertificado = Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/cert-key-20230523-170937.p12";
        String rutaParaGenerar = Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/"
            + contrato.getRuta();
        generateContractQr(contrato.getId_contrato(), numeracion, nombre_c, ci, nom_localidad, nom_municipio, tipoL, fechainicio, 
        fechafin, rutaParaGenerar, tipoC, gestion);
        //String llave = "123456";
        String rutaParaGenerarF = Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/I_"
            + contrato.getRuta();
        
        
            
                sign(rutaParaCertificado, llave.toCharArray(), PdfSignatureAppearance.NOT_CERTIFIED, 
                rutaParaGenerar, rutaParaGenerarF);
            contrato.setRuta("I_"+contrato.getRuta());
            contratoService.guardarContrato(contrato);
            
        }
    }

    public static void generateContract(String numeracion, String nombre_c, String ci, String nom_localidad,String nom_municipio, String tipo_localidad,
            Date fechainicio, Date fechafin, String filePath, String tipoCargo, String gestion) {
        // 1 pulgada = 25.4mm
        // 1 pulgada 72 puntos
        System.out.println("-------------------------METODO GENERATE CONTRACT----------------");
        System.out.println("-------------------------el cargo es: " + tipoCargo);
        System.out.println("-----------------------area : " + tipo_localidad);
        double a = 72;
        double b = 8.5;
        float ancho = (float) (a * b);
        Rectangle pageSize = new Rectangle(ancho, 72 * 13);// 216f, 330f o 648f, 981f
        
       
        // izquierda, derecha, arriba, abajo
        Document document = new Document(pageSize, 50f, 45f, 88f, 33f);// 55f, 45f, 88.8f, 33f

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // String rutaImagen =
            // "D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/fondoContratoOficial.png";
            String rutaImagen = Paths.get("").toAbsolutePath().toString()+"/PagosUap/src/main/resources/uploads/fondoContratoOficial.png";
            Image imagenFondo = Image.getInstance(rutaImagen);
            imagenFondo.scaleAbsolute(document.getPageSize());
            imagenFondo.setAbsolutePosition(0, 0);
            document.add(imagenFondo);

           
            PdfContentByte content = writer.getDirectContentUnder();
            PdfGState gstate = new PdfGState();
            gstate.setFillOpacity(0.5f);
            content.setGState(gstate);
            content.addImage(imagenFondo);

            // Agrega el contenido del contrato
            addTitle(numeracion, document, gestion);
            System.out.println("----------------------CUERPO DOCUMENTO---------------");
            addCuerpo(nombre_c, ci, nom_localidad, nom_municipio, tipo_localidad, fechainicio, fechafin, document, tipoCargo);
            addFirma(nombre_c, document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void generateContractQr(Long id_contrato, String numeracion, String nombre_c, String ci, String nom_localidad,String nom_municipio, String tipo_localidad,
            Date fechainicio, Date fechafin, String filePath, String tipoCargo, String gestion) {
        // 1 pulgada = 25.4mm
        // 1 pulgada 72 puntos
        System.out.println("-------------------------METODO GENERATE CONTRACT----------------");
        System.out.println("-------------------------el cargo es: " + tipoCargo);
        System.out.println("-----------------------area : " + tipo_localidad);
        double a = 72;
        double b = 8.5;
        float ancho = (float) (a * b);
        Rectangle pageSize = new Rectangle(ancho, 72 * 13);// 216f, 330f o 648f, 981f
        
        
        //entradas para qr
        String linkQr = "http://servicios.uap.edu.bo:9797/contratos/" + id_contrato;
        String firmaRector = "FIRMA DIGITAL M.Sc. FRANZ NAVIA MIRANDA\n https://validar.firmadigital.bo/";
        int docsize = 90; // Ancho de la imagen del código QR
        int firmasize = 100; // Alto de la imagen del código QR
        String formatoImagen = "PNG";


        // izquierda, derecha, arriba, abajo
        Document document = new Document(pageSize, 50f, 45f, 88f, 33f);// 55f, 45f, 88.8f, 33f

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // String rutaImagen =
            // "D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/fondoContratoOficial.png";
            String rutaImagen = Paths.get("").toAbsolutePath().toString()+"/PagosUap/src/main/resources/uploads/fondoContratoOficial.png";
            Image imagenFondo = Image.getInstance(rutaImagen);
            imagenFondo.scaleAbsolute(document.getPageSize());
            imagenFondo.setAbsolutePosition(0, 0);
            document.add(imagenFondo);

            //qr para validar documento
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(linkQr, BarcodeFormat.QR_CODE, docsize, docsize, hintMap);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            BufferedImage invertedImage = makeWhiteTransparent(qrImage);
            Image qrImagePDF = Image.getInstance(invertedImage, null);
            qrImagePDF.setAbsolutePosition(-2, 845);
            document.add(qrImagePDF);
            //qr para firma
            Map<EncodeHintType, Object> hintMapf = new HashMap<>();
            hintMapf.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BitMatrix bitMatrixf = new MultiFormatWriter().encode(firmaRector, BarcodeFormat.QR_CODE, firmasize, firmasize, hintMapf);
            BufferedImage qrImagef = MatrixToImageWriter.toBufferedImage(bitMatrixf);
            BufferedImage invertedImagef = makeWhiteTransparent(qrImagef);
            Image qrImagePDFf = Image.getInstance(invertedImagef, null);
            qrImagePDFf.setAbsolutePosition(33, 83);
            document.add(qrImagePDFf);

            


            PdfContentByte content = writer.getDirectContentUnder();
            PdfGState gstate = new PdfGState();
            gstate.setFillOpacity(0.5f);
            content.setGState(gstate);
            content.addImage(imagenFondo);

            // Agrega el contenido del contrato
            addTitle(numeracion, document, gestion);
            System.out.println("----------------------CUERPO DOCUMENTO---------------");
            addCuerpo(nombre_c, ci, nom_localidad, nom_municipio, tipo_localidad, fechainicio, fechafin, document, tipoCargo);
            BaseFont baseFontN = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font cursiv = new Font(baseFontN, 10, Font.ITALIC);
            

            Paragraph firma = new Paragraph();
            firma.add(new Phrase(
                "                              INCLUYE FIRMA DIGITAL\n \n",cursiv));
            firma.setAlignment(Element.ALIGN_LEFT);
            firma.setSpacingBefore(20);
            firma.setSpacingAfter(-25);
            document.add(firma);
            addFirma(nombre_c, document);


            //codigo de validacion
            String id_c = id_contrato+"";
            BaseFont baseFont = BaseFont.createFont(
                Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negrita = new Font(baseFont, 10);
            Font cursiva = new Font(baseFont, 8, Font.ITALIC);
            Paragraph title = new Paragraph();
            title.add(new Phrase("INCLUYE FIRMA DIGITAL AVALADO POR ADSIB                   \n", cursiva));
            Chunk boldChunk = new Chunk("COD. DE CERTIFICADO: " + getMD5(id_c),cursiva);
            title.add(boldChunk);

            title.setAlignment(Element.ALIGN_RIGHT);
            title.setLeading(9f); // salto de lineas de texto en un parrafo
            title.setSpacingBefore(10);
            //title.setSpacingBefore(0);// salto de filas antes de parrafo
            //title.setSpacingAfter(7);// salto de filas despues de parrafo
            document.add(title);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void addTitle(String numeracion, Document document, String gestion) throws DocumentException {
        // 1 pulgada = 25.4mm
        // 1 pulgada 72 puntos
        try {
            // BaseFont baseFont =
            // BaseFont.createFont("D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",
            // BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont baseFont = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negrita = new Font(baseFont, 11);
            // BaseFont baseFont2 =
            // BaseFont.createFont("D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/Arialn.ttf",
            // BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont baseFont2 = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/Arialn.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font normal = new Font(baseFont2, 12);

            Paragraph title = new Paragraph();
            title.add(new Phrase(
                    "CONTRATO DE SERVICIOS MANUALES CON PAGO POR JORNAL DIARIO \n", negrita));

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
            String currentYear = currentDate.format(formatter);

            Chunk boldChunk = new Chunk("Nº " + numeracion + "/" + gestion );
            title.add(boldChunk);

            title.setAlignment(Element.ALIGN_CENTER);
            title.setLeading(13.2f); // salto de lineas de texto en un parrafo
            title.setSpacingBefore(0);// salto de filas antes de parrafo
            title.setSpacingAfter(7);// salto de filas despues de parrafo
            document.add(title);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addCuerpo(String nombre_c, String ci, String nom_localidad, String nom_municipio, String tipo_localidad,
            Date fechainicio, Date fechafin, Document document, String tipoCargo)
            throws DocumentException, IOException {
        System.out.println("-------------------------METODO ADD CUERPO----------------");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechai = sdf.format(fechainicio);// fecha de inicio de contrato
        String fechaf = sdf.format(fechafin);// fecha de fin de contrato
        int dias = contarDiasHabiles(fechainicio, fechafin);// cantidad de dias a trabajar
        System.out.println("--------DIAS TRABAJADOS: "+dias);
        String textodias = convertirNumeroACardinal(dias);
        // double preciodia=103;
        double preciodia = 0;
        // double preciototal=dias*preciodia;//total a pagar
        // double descuento12p5=preciototal*0.13;//descuento del 13%
        // double descuento3p=preciototal*0.03;//descuento del 3% it
        double preciototal = 0;
        double descuento12p5 = 0;
        double descuento3p = 0;
        // calculos de precios
        System.out.println("----------------------VARIABLES DE CONDICION DE LOS SUELDOS---------------");
        System.out.println("-------------------------el cargo es: " + tipoCargo);
        System.out.println("-----------------------area : " + tipo_localidad);
        if (tipoCargo.equals("BENEFICIARIO")) {
            if (tipo_localidad.equals("URBANO")) {
                preciodia = 103;
                preciototal = dias*preciodia;
                descuento12p5 = preciototal * 0.13;// descuento del 13%
                System.out.println("----------------------CONDICION BENEFICIARIO URBANO cobra: "+preciototal);
            }
            if (tipo_localidad.equals("RURAL")) {
                preciodia = 103;
                preciototal = dias*preciodia;
                descuento12p5 = preciototal * 0.13;// descuento del 13%
                 descuento3p = preciototal * 0.03;// descuento del 3% it
                System.out.println("----------------------CONDICION BENEFICIARIO RURAL cobra: "+preciototal);
            }
        } if (tipoCargo.equals("LIDER")) {
            if (tipo_localidad.equals("URBANO")) {
                preciodia = 120;
                preciototal = dias*preciodia;
                descuento12p5 = preciototal * 0.13;// descuento del 13%
               
                System.out.println("----------------------CONDICION LIDER URBANO cobra: "+preciototal);
            }
            if (tipo_localidad.equals("RURAL")) {
                preciodia = 103;
                preciototal = dias*preciodia;
                descuento12p5 = preciototal * 0.13;// descuento del 13%
                descuento3p = preciototal * 0.03;// descuento del 3% it
                System.out.println("----------------------CONDICION LIDER URBANO cobra: "+preciototal);
            }
        }
        System.out.println("----------------------CONDICION TERMINADA---------------");
        // double preciototal=dias*preciodia;//total a pagar
        // double descuento12p5=preciototal*0.13;//descuento del 13%

        double liquidoPagable = preciototal - descuento12p5 - descuento3p;

        // Font contentFont1 = FontFactory.getFont("Helvetica Narrow",
        // BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 11);
        try {
            // BaseFont baseFontN =
            // BaseFont.createFont("D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",
            // BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont baseFontN = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negrita = new Font(baseFontN, 11);// arial narrow negrita
            // BaseFont baseFont =
            // BaseFont.createFont("D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/Arialn.ttf",
            // BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont baseFont = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/Arialn.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            double d = 10.5;
            Float tam = (float) d;
            Font normal = new Font(baseFont, tam);// arial narrow normal
            Font cursiva = new Font(baseFont, tam, Font.ITALIC);
            Paragraph parte1 = new Paragraph();
            parte1.add(new Phrase(
                    "Contrato de prestación de servicios manuales que celebran por una parte el Msc. Franz Navia Miranda en representación "
                            +
                            "legal de la Universidad Amazónica de Pando, posesionado mediante Resolución del Honorable Consejo Universitario "
                            +
                            "No. 088/2021 de 08 de marzo del 2021 y el Sr. (a) ",
                    normal));
            Chunk boldChunk = new Chunk(nombre_c, negrita);
            parte1.add(boldChunk);
            parte1.add(new Phrase(
                    ", mayor de edad, hábil por ley, con Cedula de Identidad Nº ", normal));
            Chunk boldChunk1 = new Chunk(ci, negrita);
            parte1.add(boldChunk1);

            //String tlog = "";       
            if (tipo_localidad.equals("RURAL")) {
                    parte1.add(new Phrase(" vecino de la Comunidad ",normal));
                        //tlog = "rural";
            } else {
                    parte1.add(new Phrase(" vecino de la Junta Vecinal ",normal));
                        //tlog = "urbano";
            }
            //parte1.add(new Phrase(", vecino de la Comunidad ", normal));
            Chunk boldChunk2 = new Chunk(nom_localidad, negrita);
            parte1.add(boldChunk2);
            parte1.add(new Phrase(
                    ", a quienes en adelante y para los efectos del presente contrato " +
                            "se los denominará CONTRATANTE y CONTRATADO documento que suscribirá, al tenor y contenido de las cláusulas siguientes:",
                    normal));
            parte1.setAlignment(Element.ALIGN_JUSTIFIED);
            parte1.setLeading(12.3f);
            parte1.setSpacingBefore(4);
            document.add(parte1);

            Paragraph parte2 = new Paragraph();
            parte2.add(new Phrase(
                    "PRIMERA. - (GENERALIDADES) ", negrita));
            parte2.add(new Phrase(
                    "EL CONTRATANTE es una persona Jurídica de derecho público, " +
                            "legalmente constituido por Derecho Supremo Nº 20511 del 21 de septiembre de 1984 sancionada "
                            +
                            "mediante Ley de la Nación Nº 653 de 18 de octubre de 1984, aprobado su Estatuto Orgánico en la "
                            +
                            "VI Conferencia Nacional de Universidades y cuenta Número de Identificación Tributaria NIT Nº 1016009020.",
                    normal));
            parte2.setAlignment(Element.ALIGN_JUSTIFIED);
            parte2.setLeading(12.3f);
            parte2.setSpacingBefore(9);
            document.add(parte2);

            // ver fechas
            Paragraph parte3 = new Paragraph();
            parte3.add(new Phrase(
                    "SEGUNA. - (OBJETO Y NATURALEZA DEL CONTRATO) ", negrita));
                String Cargo="";
                if (tipoCargo.equals("LIDER")) {
                        Cargo = "Jefe de Cuadrilla";
                } else {
                       Cargo = "Beneficiario Comunal"; 
                }
            parte3.add(new Phrase(
                    "EL CONTRATANTE, a fin de atender necesidades específicas para el " +
                            "cumplimiento del Convenio firmado entre la Universidad Amazónica de Pando, el Fondo Nacional de Desarrollo "+
                            "Forestal y el Ministerio de Medio Ambiente y Agua, para el cofinanciamiento del proyecto Estratégico ",
                    normal));
                    
            parte3.add(new Phrase("“Desarrollo de la gestión del conocimiento en prácticas sustentables y adopción de tecnologías en sistemas agroforestales en 14 municipios del departamento de Pando”",cursiva));
            parte3.add(new Phrase(", se contrata los servicios manuales diarios por jornal, para que preste servicios en el Componente de Capacitación y Asistencia Técnica como ",normal));

        String tlog = "";       
        if (tipo_localidad.equals("RURAL")) {
                parte3.add(new Phrase(
                    "Beneficiario Rural-",
                    negrita));
                    tlog = "en el area rural";
        } else {
                parte3.add(new Phrase(
                    "Beneficiario Urbano-",
                    normal));
                    tlog = "en el area urbana";
        }
            parte3.add(new Phrase(nom_localidad, negrita));
            parte3.add(new Phrase(
                    tlog+" de "+nom_municipio+", computables a partir de ",
                    normal));
            parte3.add(new Phrase(fechai + " hasta el " + fechaf, negrita));
            parte3.add(new Phrase(
                    " haciendo un total de " + dias + " dias computables.", normal));
            parte3.setAlignment(Element.ALIGN_JUSTIFIED);
            parte3.setLeading(12.3f);// salto entre lineas de texto
            parte3.setSpacingBefore(9);// salto antes de parrafo
            document.add(parte3);

            Paragraph parte4 = new Paragraph();
            parte4.add(new Phrase(
                    "TERCERA. - (OBLIGACIONES) ", negrita));
            parte4.add(new Phrase(
                    "EL CONTRATADO, se obliga a aplicar su capacidad y su conocimiento para cumplir satisfactoriamente "
                            +
                            "las actividades que se encomiende el CONTRATANTE, así como responder en forma personal de la calidad de los servicios y de "
                            +
                            "cualquiera otra responsabilidad en la que incurra, así como de los daños y perjuicios que por inobservancias o negligencia "
                            +
                            "de su parte se causare.",
                    normal));
            parte4.setAlignment(Element.ALIGN_JUSTIFIED);
            parte4.setLeading(12.3f);
            parte4.setSpacingBefore(9);
            document.add(parte4);

            // ver sueldos por cantidad de dias
            Paragraph parte5 = new Paragraph();
            parte5.add(new Phrase(
                    "CUARTA. - (MONTO Y FORMA DE PAGO) ", negrita));
            parte5.add(new Phrase(
                    "EL CONTRATANTE, cancelará al CONTRATADO, por el concepto de sus honorarios, la suma " +
                            "libremente consensuada y convenida de ",
                    normal));

            parte5.add(new Phrase(
                    "Bs. " + preciototal, negrita));
            String preciototaltexto = convertirDoubleATexto(preciototal);
            parte5.add(new Phrase(
                    " (un " + preciototaltexto + " bolivianos) menos los " +
                            "descuentos de ley que será de la retención del RC-IVA que corresponden al 13% que equivale a ",
                    normal));
            parte5.add(new Phrase(
                    "Bs. " + redondear(descuento12p5), negrita));
            double deparaText = redondear(descuento12p5);
            String descuento12p5texto = convertirDoubleATexto(deparaText);
            if (tipo_localidad.equals("RURAL")) {
                parte5.add(new Phrase(
                        " (" + descuento12p5texto + " bolivianos), retención del IT que corresponden al 3% que equivale a ",
                        normal));
                parte5.add(new Phrase(
                        "Bs. " + redondear(descuento3p), negrita));   
                        double desc3paraText = redondear(descuento3p);
                String descuento3ptexto = convertirDoubleATexto(desc3paraText);
                parte5.add(new Phrase(
                    " (" + descuento3ptexto + " bolivianos) siendo el líquido pagable de ", normal)); 
            } else{
                parte5.add(new Phrase(
                        " (" + descuento12p5texto + " bolivianos), siendo el líquido pagable de ", normal)); 
            }
            
            parte5.add(new Phrase(
                    "Bs. " + redondear(liquidoPagable), negrita));
            double liquidoPagableparaText = redondear(liquidoPagable);
            String liquidoPagabletexto = convertirDoubleATexto(liquidoPagableparaText);
            parte5.add(new Phrase(
                    " (un " + liquidoPagabletexto
                            + " bolivianos), por lo que dicho importe será pagado una vez completado " +
                            "los " + dias
                            + " días de trabajo.",
                    normal));
            parte5.setAlignment(Element.ALIGN_JUSTIFIED);
            parte5.setLeading(12.3f);
            parte5.setSpacingBefore(9);
            document.add(parte5);

            Paragraph parte6 = new Paragraph();
            parte6.add(new Phrase(
                    "QUINTA. - (ENCARGADO) ", negrita));
            String lider = "";       
            if (tipo_localidad.equals("RURAL")) {
                tlog = "Líder Comunal encargado de la Comunidad ";
            } else {
                tlog = "Jefe de cuadrilla encargado de la Junta Vecinal ";
            }
            parte6.add(new Phrase(
                    "mediante la coordinación del Proyecto designará a un "+lider, normal));
            parte6.add(new Phrase(nom_localidad, negrita));
            parte6.add(new Phrase(
                    " donde se desarrollará la asistencia técnica (servicios manuales), conforme las " +
                            "actividades contempladas en el proyecto, el mismo que será encargado de supervisar el cumplimiento y satisfacción "
                            +
                            "de los informes de actividades desarrolladas por el CONTRATADO, así como resultado " +
                            "de los servicios pactados en el contrato.",
                    normal));
            parte6.setAlignment(Element.ALIGN_JUSTIFIED);
            parte6.setLeading(12.3f);
            parte6.setSpacingBefore(9);
            document.add(parte6);

            Paragraph parte7 = new Paragraph();
            parte7.add(new Phrase(
                    "SEXTA. - (RESOLUCION DEL CONTRATO) ", negrita));
            parte7.add(new Phrase(
                    "EL CONTRATANTE, podrá rescindir el presente contrato, sin necesidad de juicio, por " +
                            "cualquiera de las siguientes causas imputables al CONTRATADO: \n",
                    normal));
            parte7.add(new Phrase(
                    " a)    Por prestar los servicios deficientemente, de manera inoportuna o por apegarse a lo estipulado en el presente contrato. \n"
                            +
                            " b)    Por suspender injustificadamente la prestación del servicio o por negarse a corregir lo rechazado por el encargado asignado. \n"
                            +
                            " c)    Por negarse a informar sobre la prestación y/o el resultado de los servicios encomendados. \n"
                            +
                            " d)    Por ausencia injustificada de 3 días consecutivo. \n" +
                            " e)    Ninguna de las partes será responsable por cualquier evento de caso fortuito o de fuerza mayor que le impida parcial o\n"+
                            "        totalmente cumplir con las obligaciones contraídas por virtud del presente contrato.",
                    normal));

            parte7.setAlignment(Element.ALIGN_JUSTIFIED);
            parte7.setLeading(12.3f);
            parte7.setSpacingBefore(9);
            document.add(parte7);

            Paragraph parte8 = new Paragraph();
            parte8.add(new Phrase(
                    "SÉPTIMA. - (DE LAS CARGAS SOCIALES Y LABORALES) ", negrita));
            parte8.add(new Phrase(
                    "Por la naturaleza y modalidad de contrato, EL CONTRATANTE no " +
                            "adquiere ni reconoce obligación alguna de carácter laboral, a favor del CONTRATADO.",
                    normal));
            parte8.setAlignment(Element.ALIGN_JUSTIFIED);
            parte8.setLeading(12.3f);
            parte8.setSpacingBefore(9);
            document.add(parte8);

            Paragraph parte9 = new Paragraph();
            parte9.add(new Phrase(
                    "OCTAVA. - (CONFORMIDAD) ", negrita));
            parte9.add(new Phrase(
                    "Las partes aceptan que todo lo previsto en el presente contrato, se regirá por las " +
                            "disposiciones regidas en Art. 454 del Código civil, y en caso de controversia para su interpretación y "
                            +
                            "cumplimiento, se someterán a jurisdicción ordinaria competente.",
                    normal));
            parte9.setAlignment(Element.ALIGN_JUSTIFIED);
            parte9.setLeading(12.3f);
            parte9.setSpacingBefore(9);
            document.add(parte9);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaelavoracion = dateFormat.format(fechainicio);
            String salidatextofecha = con(fechaelavoracion);
            Paragraph parte10 = new Paragraph(
                    "Para constancia dos ejemplares de un mismo tenor y contenido a los " + salidatextofecha + ".",
                    normal);
            parte10.setAlignment(Element.ALIGN_JUSTIFIED);
            parte10.setLeading(12.3f);
            parte10.setSpacingBefore(9);
            parte10.setSpacingAfter(20);
            document.add(parte10);
            // document.add(new Paragraph("\n"));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public static void addFirma(String nombre_c, Document document) throws DocumentException {

        try {
            // BaseFont baseFontN =
            // BaseFont.createFont("D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",
            // BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont baseFontN = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString()
                            + "/PagosUap/src/main/resources/uploads/ArialNarrowBold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negrita = new Font(baseFontN, 11);// arial narrow negrita
            Font cursiva = new Font(baseFontN, 10, Font.ITALIC);
            // BaseFont baseFont =
            // BaseFont.createFont("D:/UAP/proyecto/sistemaPagosUap/PagosUap/src/main/resources/uploads/Arialn.ttf",
            // BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont baseFont = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString() + "/PagosUap/src/main/resources/uploads/Arialn.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font normal = new Font(baseFont, 11);// arial narrow normal

            Paragraph firma = new Paragraph();
            
            firma.add(new Phrase(
                "                             ING. M.SC. FRANZ NAVIA MIRANDA                        Sr (a). " + nombre_c + "\n",
                    normal));
            firma.add(new Phrase(
                "                                          CONTRATANTE                                                                    CONTRATADO", negrita));

            firma.setAlignment(Element.ALIGN_LEFT);
            firma.setLeading(13.5f);
            firma.setSpacingBefore(27);
            firma.setSpacingAfter(5);
            document.add(firma);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void sign(String keystore, char[] password, int level, String src, String dest)
			throws GeneralSecurityException, IOException, DocumentException {

		System.out.print(keystore + " " + password.toString() + " " + level + " " + src + " " + dest);

		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream(keystore), password);
		String alias = (String) ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
		Certificate[] chain = ks.getCertificateChain(alias);
		// Creating the reader and the stamper
		PdfReader reader = new PdfReader(src);
		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		// appearance.setVisibleSignature(name);
		appearance.setReason("USIC Unidad de Sistemas de Informacion y Comunicacion");
		appearance.setLocation("Cobija - Pando");
		appearance.setCertificationLevel(level);
		// Creating the signature
		ExternalSignature pks = new PrivateKeySignature(pk, "SHA-256", "BC");
		ExternalDigest digest = new BouncyCastleDigest();
		MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, CryptoStandard.CMS);

		os.close();
		stamper.close();
		reader.close();
	}


    // metodo para redonderar doubles
    public static double redondear(Double entrada) {
        BigDecimal bd = new BigDecimal(entrada);
        BigDecimal numeroRedondeado = bd.setScale(2, RoundingMode.HALF_UP);
        double resultado = numeroRedondeado.doubleValue();
        return resultado;
    }

    // metodo para convertir ruta a multipartfile

    public MultipartFile convertToMultipartFile(String filePath) {
        File file = new File(filePath);
        DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem("file", "text/plain", true,
                file.getName());
        try {
            fileItem.getOutputStream();
            fileItem.getOutputStream().write(((MultipartFile) file).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonsMultipartFile(fileItem);
    }

    public static int contarDiasHabiles(Date fechaInicio, Date fechaFin) {
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fechaInicio);
        Calendar fin = Calendar.getInstance();
        fin.setTime(fechaFin);

        int count = 0;
        while (inicio.before(fin) || inicio.equals(fin)) {
            if (inicio.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && inicio.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                count++;
            }
            inicio.add(Calendar.DAY_OF_MONTH, 1);
        }

        return count;
    }

    public static String convertirNumeroACardinal(int numero) {
        RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(new Locale("es"), RuleBasedNumberFormat.SPELLOUT);
        return formatter.format(numero);
    }

    public static String convertirDoubleATexto(double numero) {
        RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(new Locale("es"), RuleBasedNumberFormat.SPELLOUT);
        int parteEntera = (int) numero;
        int parteDecimal = (int) Math.round((numero - parteEntera) * 100); // Obtener los dos dígitos decimales
        String parteEnteraTexto = formatter.format(parteEntera);
        // String parteDecimalTexto = formatter.format(parteDecimal);
        return parteEnteraTexto + " con " + parteDecimal + "/100";
    }

    public static String con(String fechaTexto) {
        // String fechaTexto = "25/10/2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha;
        try {
            fecha = dateFormat.parse(fechaTexto);
            // Extraer los componentes de la fecha
            int dia = fecha.getDate();
            int mes = fecha.getMonth();
            int anio = fecha.getYear() + 1900; // El método getYear() devuelve el año actual - 1900

            // Construir la representación deseada
            String representacion = dia + " días del mes de " + obtenerNombreMes(mes) + " de " + anio;
            // System.out.println(representacion);
            return representacion;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public static String obtenerNombreMes(int mes) {
        String[] nombresMeses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
                "Septiembre", "Octubre", "Noviembre", "Diciembre" };
        return nombresMeses[mes];
    }

    private static BufferedImage makeWhiteTransparent(BufferedImage image) {
        BufferedImage transparentImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                if (red == 255 && green == 255 && blue == 255) {
                    // Si el píxel es blanco, establece el componente alfa en 0 (transparente)
                    alpha = 0;
                }
                int transparentRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                transparentImage.setRGB(x, y, transparentRGB);
            }
        }
        return transparentImage;
    }

    public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}

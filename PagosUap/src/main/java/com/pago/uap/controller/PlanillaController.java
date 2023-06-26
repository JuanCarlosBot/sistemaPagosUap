package com.pago.uap.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pago.uap.model.entity.Cargo;
import com.pago.uap.model.entity.EstadoPago;
import com.pago.uap.model.entity.Gestion;
import com.pago.uap.model.entity.Localidad;
import com.pago.uap.model.entity.Persona;
import com.pago.uap.model.entity.Planilla;
import com.pago.uap.model.entity.TipoCargo;
import com.pago.uap.model.entity.Usuario;
import com.pago.uap.model.service.ICargoService;
import com.pago.uap.model.service.IEstadoPagoService;
import com.pago.uap.model.service.IGestionService;
import com.pago.uap.model.service.ILocalidadService;
import com.pago.uap.model.service.IMunicipioService;
import com.pago.uap.model.service.IPersonaService;
import com.pago.uap.model.service.IPlanillaService;
import com.pago.uap.model.service.ITipoCargoService;
//excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
@Controller
public class PlanillaController {

    @Autowired
    private IMunicipioService municipioService;
    @Autowired
    private ITipoCargoService tipoCargoService;
    @Autowired
    private IGestionService gestionService;
    @Autowired
    private ILocalidadService localidadService;
    @Autowired
    private ICargoService cargoService;
    @Autowired
    private IEstadoPagoService estadoPagoService;
    @Autowired
    private IPlanillaService planillaService;
    @Autowired
    private IPersonaService personaService;
    
    @GetMapping("/generarPlanilla")
    public String vistaGenerarPlanilla(Model model, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("municipios", municipioService.listaMunicipiosAll());
            model.addAttribute("tipoCargos", tipoCargoService.listaTipoCargosAll());
            model.addAttribute("gestiones", gestionService.listaGestionesAll());
            model.addAttribute("listaLocalidades", localidadService.listaLocalidadesAll());
            return "pago/generarPlanilla";
        
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/generarPlanillaPost")
    public String generarPlanillaPost(
    @RequestParam(value="id_localidad") Long id_localidad,
    @RequestParam(value="id_gestion") Long id_gestion, RedirectAttributes redirectAttrs){

        Localidad localidad = localidadService.sacarIdLocalidad(id_localidad);
        Gestion gestion = gestionService.sacarIdGestion(id_gestion);
        //TipoCargo tipoCargo = tipoCargoService.sacarIdTipoCargo(id_tipo_cargo);
        int c= 0;
        for (Persona persona : localidad.getPersonas()) {
            if (gestion.getId_gestion()==persona.getCargo().getGestion().getId_gestion()) {//SOLO A PERSONAS CON GESTION SELECCIONADO
                Planilla planilla = new Planilla();
                int dias_trabajado = contarDiasHabiles(persona.getCargo().getFecha_inicio(), persona.getCargo().getFecha_fin());
                if (persona.getCargo().getEstadoPago().getId_estado_pago()==2) {//estado pago 2 SIN PAGAR
                    if (persona.getCargo().getTipoCargo().getId_tipo_cargo()==1) {//tipo cargo 1 BENEFICIARIO
                        
                        Double monto_por_dia = 103.00;
                        Double total_ganado = dias_trabajado * monto_por_dia;
                        Double descuento_rciva = total_ganado * 0.13;//13%descuento
                        Double descuento_it = 0.0;
                        if (persona.getLocalidad().getTipoLocalidad().getId_tipo_localidad()!=1) {//1 URBANO
                            descuento_it = total_ganado * 0.03;//3%descuento it
                        }
                        Double liquido_pagable = total_ganado - descuento_rciva - descuento_it;
                        
                        planilla.setDias_trabajado(dias_trabajado+"");
                        planilla.setMonto_por_dia(monto_por_dia+"");
                        planilla.setTotal_ganado(total_ganado+"");
                        planilla.setDescuento_rciva(descuento_rciva+"");
                        planilla.setDescuento_it(descuento_it+""); 
                        planilla.setLiquido_pagable(liquido_pagable+"");
                        planilla.setLocalidad(localidad);
                        planilla.setPersona(persona);
                        planillaService.guardarPlanilla(planilla);

                        /*EstadoPago estadoPago = estadoPagoService.sacarIdEstadoPago(1l);
                        Cargo cargo = persona.getCargo();
                        cargo.setEstadoPago(estadoPago);
                        cargoService.guardarCargo(cargo);*/

                        System.out.println(c+" "+persona.getCargo().getNumeracion()+" "+persona.getLocalidad().getTipoLocalidad().getNombre_tipo_localidad()+" "+
                        persona.getCargo().getTipoCargo().getNombre_tipo_cargo()+" "+persona.getNombre_completo_persona()+" "+
                        dias_trabajado+" "+monto_por_dia+" "+total_ganado+" "+descuento_rciva+" "+liquido_pagable);
                        
                    }else if(persona.getCargo().getTipoCargo().getId_tipo_cargo()==2){//tipo cargo 2 LIDER
                        Double monto_por_dia = 120.00;
                        Double total_ganado = dias_trabajado * monto_por_dia;
                        Double descuento_rciva = total_ganado * 0.13;//13%descuento
                        Double descuento_it = 0.0;
                        if (persona.getLocalidad().getTipoLocalidad().getId_tipo_localidad()!=1) {//1 URBANO
                            descuento_it = total_ganado * 0.03;//3%descuento it
                        }
                        Double liquido_pagable = total_ganado - descuento_rciva - descuento_it;

                        planilla.setDias_trabajado(dias_trabajado+"");
                        planilla.setMonto_por_dia(monto_por_dia+"");
                        planilla.setTotal_ganado(total_ganado+"");
                        planilla.setDescuento_rciva(descuento_rciva+"");
                        planilla.setDescuento_it(descuento_it+""); 
                        planilla.setLiquido_pagable(liquido_pagable+"");
                        planilla.setLocalidad(localidad);
                        planilla.setPersona(persona);
                        planillaService.guardarPlanilla(planilla);

                        /*EstadoPago estadoPago = estadoPagoService.sacarIdEstadoPago(1l);
                        Cargo cargo = persona.getCargo();
                        cargo.setEstadoPago(estadoPago);
                        cargoService.guardarCargo(cargo);*/

                        System.out.println(c+" "+persona.getCargo().getNumeracion()+" "+persona.getLocalidad().getTipoLocalidad().getNombre_tipo_localidad()+" "+
                        persona.getCargo().getTipoCargo().getNombre_tipo_cargo()+" "+persona.getNombre_completo_persona()+" "+
                        dias_trabajado+" "+monto_por_dia+" "+total_ganado+" "+descuento_rciva+" "+liquido_pagable);
                        
                        
                    }c++;
                    redirectAttrs
                    .addFlashAttribute("mensaje", "Se generaron "+c+" registros en planillas!")
                    .addFlashAttribute("clase", "warning alert-dismissible fade show");
                }else{
                    redirectAttrs
                    .addFlashAttribute("mensaje", "Todas las personas estan con estado pagado!")
                    .addFlashAttribute("clase", "warning alert-dismissible fade show");
                    return "redirect:/generarPlanilla";
                }
            }
        }
            
        return "redirect:/generarPlanilla";
    }
    @GetMapping("/download/{id_localidad}")
    public void downloadExcel(@PathVariable(value = "id_localidad")Long id_localidad, HttpServletResponse response, Model model) throws IOException {
        // Obtener la lista de datos
        Localidad localidades = localidadService.sacarIdLocalidad(id_localidad);
        List<Planilla> listaPlanillas = new ArrayList<>();
        for (Planilla planilla : localidades.getPlanillas()) {
            listaPlanillas.add(planilla);
        }
        // Crear el libro de trabajo (workbook) de Excel
        Workbook workbook = new XSSFWorkbook();

        // Crear una nueva hoja de cálculo
        Sheet sheet = workbook.createSheet("Datos");

        // Crear una fila para la cabecera
        Row headerRow = sheet.createRow(0);

        // Crear celdas para la cabecera
        List<String> headers = Arrays.asList("Nro", "FECHA DE CONTRATO","APELLIDO Y NOMBRE","C.I.","CARGO","DIAS TRABAJADO",
        "MONTO POR DIA","TOTAL GANADO","DESC. RC-IVA 13%", "DESC. IT 3%","LIQUIDO PAGABLE");
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }
            
        // Crear filas y celdas para los datos
        for (int i = 0; i < listaPlanillas.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            Planilla planilla = (Planilla) listaPlanillas.get(i);
            
            // Crear celdas para cada propiedad de los datos
            Cell cell1 = dataRow.createCell(0);
            cell1.setCellValue(planilla.getPersona().getCargo().getNumeracion());
            Cell cell2 = dataRow.createCell(1);
            cell2.setCellValue(planilla.getPersona().getCargo().getFecha_inicio()+" "+planilla.getPersona().getCargo().getFecha_fin());
            Cell cell3 = dataRow.createCell(2);
            cell3.setCellValue(planilla.getPersona().getNombre_completo_persona());
            Cell cell4 = dataRow.createCell(3);
            cell4.setCellValue(planilla.getPersona().getCi_persona());
            Cell cell5 = dataRow.createCell(4);
            cell5.setCellValue(planilla.getPersona().getCargo().getTipoCargo().getNombre_tipo_cargo()+" "+planilla.getLocalidad().getTipoLocalidad().getNombre_tipo_localidad());

            Cell cell6 = dataRow.createCell(5);
            cell6.setCellValue(planilla.getDias_trabajado());
            Cell cell7 = dataRow.createCell(6);
            cell7.setCellValue(planilla.getMonto_por_dia());
            Cell cell8 = dataRow.createCell(7);
            cell8.setCellValue(planilla.getTotal_ganado());
            Cell cell9 = dataRow.createCell(8);
            cell9.setCellValue(planilla.getDescuento_rciva());
            Cell cell10 = dataRow.createCell(9);
            cell10.setCellValue(planilla.getDescuento_it());
            Cell cell11 = dataRow.createCell(10);
            cell11.setCellValue(planilla.getLiquido_pagable());
            
        }

        // Establecer el encabezado de la respuesta HTTP
        response.setHeader("Content-Disposition", "attachment; filename="+localidades.getNombre_localidad()+".xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // Obtener el flujo de salida de la respuesta HTTP
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/downloadddd/{id_localidad}")
    public void downloadCsv(@PathVariable(value = "id_localidad")Long id_localidad, HttpServletResponse response, Model model) throws IOException {
        // Obtener la lista de datos
        //List<Planilla> datos = obtenerDatos(id_localidad);

        Localidad localidades = localidadService.sacarIdLocalidad(id_localidad);
        List<Planilla> listaPlanillas = new ArrayList<>();
        for (Planilla planilla : localidades.getPlanillas()) {
            listaPlanillas.add(planilla);
        }

        // Establecer el encabezado de la respuesta HTTP
        response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
        response.setContentType("text/xlsx");

        // Obtener el flujo de salida de la respuesta HTTP
        PrintWriter writer = response.getWriter();

        try {
            // Escribir el contenido del archivo CSV en el flujo de salida
            writer.println("Columna 1,Columna 2"); // Cabecera
            for (Planilla dato : listaPlanillas) {
                writer.println(dato.getMonto_por_dia() + "," + dato.getDescuento_rciva()); // Filas de datos
            }
        } finally {
            writer.flush();
            writer.close();
        }
    }

    
    public int contarDiasHabiles(Date fechaInicio, Date fechaFin) {
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
}

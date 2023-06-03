package com.pago.uap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

import com.pago.uap.model.entity.Usuario;
import com.pago.uap.model.service.IUsuarioService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UsuarioController {
    @Autowired
    private IUsuarioService iUsuarioService;

    @PostMapping(value="/EditarPerfil")
    public String ModificarPerfil(Model model, @Validated Usuario usuario, HttpServletRequest request,
           BindingResult result,
            @RequestParam("fotoPerfil") MultipartFile fotoPerfil 

    ) throws IOException {
        System.out.println("-------------------entro al metodo editar perfil  ----------------------");
        Usuario usuarioL = (Usuario)request.getSession().getAttribute("usuario");
        try {
            String rutaMostrar = "";

            String filePath = Paths.get("").toAbsolutePath().toString()
                    + "/PagosUap/src/main/resources/static/img/FotoPerfil/";
            System.out.println("ruta prueba: " + filePath);

            System.out.println("nombre de la imagen: " + fotoPerfil .getOriginalFilename());
            String extension = obtenerExtensionArchivo(fotoPerfil .getOriginalFilename());
            //File destinationFile = new File(filePath + fotoPerfil .getOriginalFilename());
            File destinationFile = new File(filePath + "FotoPerfil"+usuarioL.getId_usuario()+extension);
            System.out.println("nueva ruta de img: " + destinationFile.getAbsoluteFile());
            
            //rutaMostrar = "../../img/FotoPerfil/" + fotoPerfil .getOriginalFilename();
            rutaMostrar = "../../img/FotoPerfil/" + "FotoPerfil"+usuarioL.getId_usuario()+extension;
            System.out.println("ruta a mostrar: " + rutaMostrar);
            fotoPerfil.transferTo(destinationFile);
            if (!fotoPerfil .isEmpty()) {
                usuario.setFoto(rutaMostrar);
            }

        } catch (Exception e) {
            System.out.println("Error en mover foto: " + e);
        }
        usuario.setUsuario(usuarioL.getUsuario());
        request.getSession().setAttribute("usuario", usuario);
        iUsuarioService.guardarUsuario(usuario);
        return "redirect:/persona";
    }
    private String obtenerExtensionArchivo(String nombreArchivo) {
        int indicePunto = nombreArchivo.lastIndexOf(".");
        if (indicePunto >= 0) {
            return nombreArchivo.substring(indicePunto);
        }
        return "";
    }
    
}

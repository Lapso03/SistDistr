package com.example.demo.config;

import com.example.demo.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ── NO CRÍTICAS ───────────────────────────────────────────────────

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ModelAndView handleUsernameExists(UsernameAlreadyExistsException e,
                                             HttpServletRequest request) {
        return buildError("Usuario ya registrado", e.getMessage(), false, request, "/registro", "Volver al registro");
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    public ModelAndView handlePokemonNotFound(PokemonNotFoundException e,
                                              HttpServletRequest request) {
        return buildError("Pokémon no encontrado", e.getMessage(), false,  request,"/pokemon", "Buscar otro Pokémon");
    }

    @ExceptionHandler(ApiPythonException.class)
    public ModelAndView handleApiPython(ApiPythonException e,
                                        HttpServletRequest request) {
        return buildError("API Python no disponible", e.getMessage(), false,  request,"/", "Volver al inicio");
    }

    @ExceptionHandler(FlaskServerException.class)
    public ModelAndView handleFlaskServer(FlaskServerException e,
                                          HttpServletRequest request) {
        return buildError("Error en el servidor Python", e.getMessage(), false, request, "/", "Volver al inicio");
    }

    @ExceptionHandler(ApiTimeoutException.class)
    public ModelAndView handleTimeout(ApiTimeoutException e,
                                      HttpServletRequest request) {
        return buildError("Timeout de API",
                e.getMessage(),
                false, request, "/", "Volver al inicio");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleBadRequest(IllegalArgumentException e,
                                         HttpServletRequest request) {
        return buildError("Parámetro inválido",
                e.getMessage(),
                false, request, "/", "Volver");
    }

    // ── CRÍTICAS ──────────────────────────────────────────────────────

    @ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDataAccess(DataAccessException e,
                                         HttpServletRequest request) {
        return buildError("Error de base de datos",
                "No se pudo acceder a la base de datos.",
                true, request, "/login", "Ir al login");
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception e,
                                      HttpServletRequest request) {
        return buildError("Error inesperado",
                "Ha ocurrido un error inesperado en el sistema.",
                true, request, "/login", "Volver al inicio");
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ModelAndView handleAccessDenied(Exception e,
                                           HttpServletRequest request) {
        return buildError("Acceso denegado",
                "No tienes permisos para acceder a esta página.",
                true, request, "/", "Volver al inicio");
    }

    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNull(NullPointerException e,
                                   HttpServletRequest request) {
        return buildError("Error interno",
                "Se ha producido un error inesperado (null).",
                true, request,"/", "Volver al inicio");
    }

    // ── Helper ────────────────────────────────────────────────────────

    private ModelAndView buildError(String titulo, String mensaje,
                                    boolean critico,
                                    HttpServletRequest request,
                                    String fallbackUrl,
                                    String volverTexto) {

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("titulo", titulo);
        mav.addObject("mensaje", mensaje);
        mav.addObject("critico", critico);

        String referer = request.getHeader("Referer");
        String volver = (referer != null) ? referer : fallbackUrl;

        mav.addObject("volver", volver);
        mav.addObject("volverTexto", volverTexto);

        return mav;
    }
}
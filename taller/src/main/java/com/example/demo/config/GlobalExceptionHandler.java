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

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleBadRequest(IllegalArgumentException e,
                                         HttpServletRequest request) {
        return buildError("Parámetro inválido",
                e.getMessage(),
                false, request, "/", "Volver");
    }

    @ExceptionHandler(EventoNotFoundException.class)
    public ModelAndView handleEventoNotFound(EventoNotFoundException e, HttpServletRequest request) {
        return buildError("Evento no encontrado", e.getMessage(), false, request, "/", "Volver al inicio");
    }

    @ExceptionHandler(AforoAgotadoException.class)
    public ModelAndView handleAforoAgotado(AforoAgotadoException e, HttpServletRequest request) {
        return buildError("Sin entradas disponibles", e.getMessage(), false, request, "/", "Ver otros eventos");
    }

    @ExceptionHandler(ReservaNotFoundException.class)
    public ModelAndView handleReservaNotFound(ReservaNotFoundException e, HttpServletRequest request) {
        return buildError("Reserva no encontrada", e.getMessage(), false, request, "/usuario/reservas", "Mis reservas");
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
    public ModelAndView handleGeneric(Exception e, HttpServletRequest request) {
        return buildError("Error inesperado",
                "Ha ocurrido un error inesperado en el sistema.",
                true, request, "/login", "Volver al inicio");
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ModelAndView handleAccessDenied(Exception e, HttpServletRequest request) {
        return buildError("Acceso denegado",
                "No tienes permisos para acceder a esta página.",
                false, request, "/", "Volver al inicio");
    }

    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNull(NullPointerException e, HttpServletRequest request) {
        return buildError("Error interno",
                "Se ha producido un error inesperado (null).",
                true, request,"/", "Volver al inicio");
    }

    // ── Helper ────────────────────────────────────────────────────────

    private ModelAndView buildError(String titulo, String mensaje, boolean critico,
                                    HttpServletRequest request, String fallbackUrl, String volverTexto) {

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
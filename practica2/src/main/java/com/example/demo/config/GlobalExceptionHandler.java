package com.example.demo.config;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Error de acceso a base de datos
    @ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDataAccessException(DataAccessException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("titulo", "Error de base de datos");
        mav.addObject("mensaje", "No se pudo acceder a la base de datos. Por favor, inténtalo más tarde.");
        mav.addObject("critico", false);
        return mav;
    }

    // Flask no disponible
    @ExceptionHandler(ResourceAccessException.class)
    public ModelAndView handleResourceAccessException(ResourceAccessException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("titulo", "API Python no disponible");
        mav.addObject("mensaje", "No se pudo conectar con la API Python. ¿Está arrancada?");
        mav.addObject("critico", false);
        return mav;
    }

    // Flask disponible pero Pokemon no existente
    @ExceptionHandler(ResourceAccessException.class)
    public ModelAndView handleInexistentPokemon(ResourceAccessException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("titulo", "Pokemon Inexistente");
        mav.addObject("mensaje", "El Pokemon buscado no existe. ¿Lo has escrito bien?");
        mav.addObject("critico", false);
        return mav;
    }

    // Cualquier otro error no previsto
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("titulo", "Error inesperado");
        mav.addObject("mensaje", "Ha ocurrido un error inesperado en el sistema.");
        mav.addObject("critico", true);
        return mav;
    }
}
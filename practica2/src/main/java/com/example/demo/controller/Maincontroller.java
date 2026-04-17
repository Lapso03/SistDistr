package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Maincontroller {
    @GetMapping("/")    //Servicio REST de tipo get
    public  String vistaHome(ModelMap interfazConPantalla){
        return "index";
    }
}
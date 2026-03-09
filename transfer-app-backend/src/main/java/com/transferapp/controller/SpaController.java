package com.transferapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles Angular client-side routing in a single-JAR deployment
 */
@Controller
public class SpaController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forwardToAngular() {
        return "forward:/index.html";
    }

    @RequestMapping(value = "/{path:[^\\.]*}/{subPath:[^\\.]*}")
    public String forwardNestedToAngular() {
        return "forward:/index.html";
    }
}
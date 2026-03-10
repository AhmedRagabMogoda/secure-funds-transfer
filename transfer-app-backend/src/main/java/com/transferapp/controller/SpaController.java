package com.transferapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles Angular client-side routing in a single-JAR deployment
 */
@Controller
public class SpaController {

    /**
     * Forward Angular routes to index.html
     */
    @RequestMapping(value = {
            "/login",
            "/dashboard",
            "/transfer",
            "/history",
            "/profile"
    })
    public String forwardAngularRoutes() {
        return "forward:/index.html";
    }
}
package com.transferapp.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SpaController {

    /**
     * Forwards Angular routes to index.html.
     *
     */
    @RequestMapping(
            method  = RequestMethod.GET,
            value   = { "/login", "/dashboard", "/transfer", "/history", "/profile" }
    )
    public String forwardAngularRoutes() {
        return "forward:/index.html";
    }
}
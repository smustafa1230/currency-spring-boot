package com.crewmeister.cmcodingchallenge.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/health")
    public @ResponseBody
    String home() {
        return "CM API is up!";
    }
}
package cz.pwc.borders.controller;

import cz.pwc.borders.exception.NoAvailablePathException;
import cz.pwc.borders.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("routing")
public class RoutingController {

    private final CountryService service;

    public RoutingController(CountryService service) {
        this.service = service;
    }

    @GetMapping(path="/{origin}/{destination}", produces="application/json")
    public @ResponseBody ResponseEntity<HashMap<String, List<String>>> getRoute(
            @PathVariable String origin,
            @PathVariable String destination) {

        HashMap<String, List<String>> res = new HashMap<>();

        try {
            res.put("route", service.getRoute(origin, destination));
        } catch (NoAvailablePathException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

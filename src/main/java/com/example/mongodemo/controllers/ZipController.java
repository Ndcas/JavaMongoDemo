package com.example.mongodemo.controllers;

import com.example.mongodemo.repositories.ZipRepository;
import java.util.ArrayList;
import java.util.Optional;
import models.Zip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ZipController {

    @Autowired
    private ZipRepository zipRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/")
    public String index(ModelMap model) {
        model.addAttribute("zips", zipRepository.findAll());
        return "index";
    }

    @RequestMapping("/filter")
    public String filter(
            ModelMap model,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String city,
            @RequestParam int longOp,
            @RequestParam(required = false) Double longtitude,
            @RequestParam int laOp,
            @RequestParam(required = false) Double latitude,
            @RequestParam int popOp,
            @RequestParam(required = false) Long pop,
            @RequestParam(required = false) String state) {
        Query query = new Query();
        if (id != null && id.trim().length() > 0) {
            query.addCriteria(Criteria.where("_id").regex("^" + id.trim()));
            model.addAttribute("id", id.trim());
        }
        if (city != null && city.trim().length() > 0) {
            query.addCriteria(Criteria.where("city").regex("^" + city.trim(), "i"));
            model.addAttribute("city", city.trim());
        }
        if (longtitude != null) {
            switch (longOp) {
                case 0 ->
                    query.addCriteria(Criteria.where("loc.0").is(longtitude));
                case 1 ->
                    query.addCriteria(Criteria.where("loc.0").gte(longtitude));
                case -1 ->
                    query.addCriteria(Criteria.where("loc.0").lte(longtitude));
            }
            model.addAttribute("longOp", longOp);
            model.addAttribute("longtitude", longtitude);
        }
        if (latitude != null) {
            switch (laOp) {
                case 0 ->
                    query.addCriteria(Criteria.where("loc.1").is(latitude));
                case 1 ->
                    query.addCriteria(Criteria.where("loc.1").gte(latitude));
                case -1 ->
                    query.addCriteria(Criteria.where("loc.1").lte(latitude));
            }
            model.addAttribute("laOp", laOp);
            model.addAttribute("latitude", latitude);
        }
        if (pop != null) {
            switch (popOp) {
                case 0 ->
                    query.addCriteria(Criteria.where("pop").is(pop));
                case 1 ->
                    query.addCriteria(Criteria.where("pop").gte(pop));
                case -1 ->
                    query.addCriteria(Criteria.where("pop").lte(pop));
            }
            model.addAttribute("popOp", popOp);
            model.addAttribute("pop", pop);
        }
        if (state != null && state.trim().length() > 0) {
            query.addCriteria(Criteria.where("state").regex(state.trim(), "i"));
            model.addAttribute("state", state.trim());
        }
        model.addAttribute("zips", mongoTemplate.find(query, Zip.class));
        return "index";
    }

    @RequestMapping("/create")
    public String create() {
        return "create";
    }

    @RequestMapping(value = "/createZip", method = RequestMethod.POST)
    public String createZip(
            ModelMap model,
            @RequestParam String id,
            @RequestParam String city,
            @RequestParam double longtitude,
            @RequestParam double latitude,
            @RequestParam long pop,
            @RequestParam String state) {
        Zip zip = new Zip();
        zip.setId(id);
        zip.setCity(city.trim());
        ArrayList<Double> loc = new ArrayList();
        loc.add(longtitude);
        loc.add(latitude);
        zip.setLoc(loc);
        zip.setPop(pop >= 0 ? pop : 0);
        zip.setState(state);
        if (zipRepository.findById(id).isPresent()) {
            model.addAttribute("status", -1);
            return "create";
        }
        zipRepository.save(zip);
        model.addAttribute("status", 0);
        return "create";
    }

    @RequestMapping("/edit")
    public String edit(ModelMap model, @RequestParam String id) {
        Optional<Zip> searchResult = zipRepository.findById(id);
        if (searchResult.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("zip", searchResult.get());
        return "edit";
    }

    @RequestMapping(value = "/editZip", method = RequestMethod.POST)
    public String editZip(
            ModelMap model,
            @RequestParam String id,
            @RequestParam String city,
            @RequestParam double longtitude,
            @RequestParam double latitude,
            @RequestParam long pop,
            @RequestParam String state) {
        ArrayList<Double> loc = new ArrayList();
        loc.add(longtitude);
        loc.add(latitude);
        Zip zip = new Zip(id, city, loc, pop, state);
        model.addAttribute("zip", zip);
        if (zipRepository.findById(id).isEmpty()) {
            model.addAttribute("status", -1);
            return "edit";
        }
        zipRepository.save(zip);
        model.addAttribute("status", 0);
        return "edit";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestParam String id) {
        Optional<Zip> searchResult = zipRepository.findById(id);
        if (searchResult.isEmpty()) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        zipRepository.delete(searchResult.get());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}

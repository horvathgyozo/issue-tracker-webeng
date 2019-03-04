package hu.elte.IssueTracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TemplateController {
    
    @RequestMapping("/hello")
    public String index() {
        return "hello";
    }
    
    @RequestMapping("/list")
    public String list() {
        return "proba/list";
    }
    
}

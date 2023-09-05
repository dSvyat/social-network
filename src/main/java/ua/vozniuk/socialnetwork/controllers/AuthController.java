package ua.vozniuk.socialnetwork.controllers;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.vozniuk.socialnetwork.util.CountryList;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.services.RegistrationService;
import ua.vozniuk.socialnetwork.util.PersonValidator;

@Controller
@RequestMapping
public class AuthController {
    private final RegistrationService registrationService;
    private final PersonValidator personValidator;

    public AuthController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    @RequestMapping(path = "/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person")Person person, Model model)
    {
        model.addAttribute("countries", CountryList.COUNTRIES);
        return "registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult, Model model){
        model.addAttribute("countries", CountryList.COUNTRIES);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()){
            return "registration";
        }
        registrationService.register(person);
        return "redirect:/login";
    }
}

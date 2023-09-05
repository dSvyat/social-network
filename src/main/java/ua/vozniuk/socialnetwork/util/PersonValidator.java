package ua.vozniuk.socialnetwork.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.services.PersonDetailsService;

@Component
public class PersonValidator implements Validator {
    PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors){
        Person person = (Person) o;
        if (personDetailsService.getByUsername(person.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "Username already taken");
        }
        if(!person.getPassword().equals(person.getConfirmPassword())){
            errors.rejectValue("password","", "Passwords should match");
            errors.rejectValue("confirmPassword","", "Passwords should match");
        }
    }
}

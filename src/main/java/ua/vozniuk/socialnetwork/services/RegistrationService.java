package ua.vozniuk.socialnetwork.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vozniuk.socialnetwork.models.Person;

@Service
public class RegistrationService {
    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder) {
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person){
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            personDetailsService.save(person);
    }
}

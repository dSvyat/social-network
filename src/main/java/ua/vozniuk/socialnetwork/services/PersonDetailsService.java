package ua.vozniuk.socialnetwork.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.repositories.PeopleRepository;
import ua.vozniuk.socialnetwork.security.PersonDetails;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PersonDetailsService implements UserDetailsService{
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public UserDetails loadUserByUsername(String username){
        Optional<Person> person = peopleRepository.findByUsername(username);
        if(person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }

    public List<Person> getAllPeople(){
        return peopleRepository.findAll();
    }

    public Person findById(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> getByUsername(String username){
        return peopleRepository.findByUsername(username);
    }

    public Optional<Person> getByEmail(String email){
        return peopleRepository.findByEmail(email);
    }

    public List<Person> findTop10ByFollowers(int id){
        return peopleRepository.getTop10ByOrderByFollowersList();
    }

    public void setImg(Person person, String fileName){
        Person person1 = peopleRepository.findById(person.getId()).get();
        person1.setImage(fileName);
        peopleRepository.save(person1);
    }
//    public void setImg(Person person, MultipartFile multipartFile){
//        try {
//            person.setImg(multipartFile.getBytes());
//            peopleRepository.save(person);
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }


    @Transactional
    public void save (Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void editAboutPerson(Person updatedPerson) {
        Optional<Person> person = peopleRepository.findById(updatedPerson.getId());
            person.get().setAboutMe(updatedPerson.getAboutMe());
            peopleRepository.save(person.get());
    }

    @Transactional
    public void deleteById(int id){
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void edit(int id, Person updatedPerson){
        BeanUtils.copyProperties(updatedPerson, peopleRepository.findById(id).get(),
                "id", "joiningDate");
    }
}

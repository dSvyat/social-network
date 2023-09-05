package ua.vozniuk.socialnetwork.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.models.Post;
import ua.vozniuk.socialnetwork.models.UserLikes;
import ua.vozniuk.socialnetwork.repositories.UserLikesRepository;

@Service
public class UserLikesService {
    private final UserLikesRepository userLikesRepository;

    public UserLikesService(UserLikesRepository userLikesRepository) {
        this.userLikesRepository = userLikesRepository;
    }

    @Transactional
    public void save(Person person, Post post){
        UserLikes userLikes = new UserLikes();
        userLikes.setPerson(person);
        userLikes.setPost(post);
        userLikesRepository.save(userLikes);
    }

    @Transactional
    public void deleteByPersonAndPost(Person person, Post post){
        userLikesRepository.deleteByPersonAndPost(person, post);
    }

    public UserLikes getByPersonAndPost(Person person, Post post){
        return userLikesRepository.getByPersonAndPost(person, post).orElse(null);
    }
}

package ua.vozniuk.socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.models.Post;
import ua.vozniuk.socialnetwork.models.UserLikes;

import java.util.Optional;

@Repository
public interface UserLikesRepository extends JpaRepository<UserLikes, Integer> {
    Optional<UserLikes> getByPersonAndPost(Person person, Post post);
    void deleteByPersonAndPost(Person person, Post post);
    boolean existsByPersonAndPost(Person person, Post post);
}

package ua.vozniuk.socialnetwork.repositories;

import com.amazonaws.services.greengrassv2.model.LambdaIsolationMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.models.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByAuthorOrderByCreatedAtDesc(Person author);
    void deleteById(int id);
}

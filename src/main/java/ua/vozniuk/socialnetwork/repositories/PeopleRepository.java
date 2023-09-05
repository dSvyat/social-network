package ua.vozniuk.socialnetwork.repositories;

import org.aspectj.weaver.patterns.PerObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.vozniuk.socialnetwork.models.Follower;
import ua.vozniuk.socialnetwork.models.Friendship;
import ua.vozniuk.socialnetwork.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByEmail(String email);
    List<Person> getTop10ByOrderByFollowersList();
}

package ua.vozniuk.socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.vozniuk.socialnetwork.models.Follower;
import ua.vozniuk.socialnetwork.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowersRepository extends JpaRepository<Follower, Integer> {
    List<Follower> getAllByFollowed(Person followed);
    Optional<Follower> findByFollowerAndFollowed(Person follower, Person followed);
    boolean existsByFollowerAndFollowed(Person follower, Person followed);
    void deleteByFollowerAndFollowed(Person follower, Person followed);
}

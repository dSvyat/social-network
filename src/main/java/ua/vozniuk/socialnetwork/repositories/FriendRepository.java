package ua.vozniuk.socialnetwork.repositories;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.vozniuk.socialnetwork.models.Friendship;
import ua.vozniuk.socialnetwork.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friendship, Integer> {
    List<Friendship> getAllByAcceptedAndPersonAccept(boolean accepted, Person person);
    Optional<Friendship> findByPersonAcceptAndPersonRequest(Person personAccept, Person personRequest);
    List<Friendship> getAllByPersonAcceptOrPersonRequestAndAccepted(Person personAccept, Person personRequest, boolean accepted);
    List<Friendship> getAllByAcceptedAndPersonAcceptOrPersonRequest(boolean accepted, Person person, Person person1);
    List<Friendship> getAllByPersonAcceptAndAccepted(Person personAccept, boolean accepted);

}

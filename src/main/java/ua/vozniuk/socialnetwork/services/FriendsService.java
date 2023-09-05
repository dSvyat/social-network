package ua.vozniuk.socialnetwork.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vozniuk.socialnetwork.models.Friendship;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.repositories.FriendRepository;

import java.util.List;
import java.util.stream.Stream;

@Service
public class FriendsService {
    private final FriendRepository friendRepository;
    private final PersonDetailsService personDetailsService;

    public FriendsService(FriendRepository friendRepository, PersonDetailsService personDetailsService) {
        this.friendRepository = friendRepository;
        this.personDetailsService = personDetailsService;
    }

    public Friendship getFriendshipByPeople(Person personRequest, Person personAccept) {
        if (friendRepository.findByPersonAcceptAndPersonRequest(personAccept, personRequest).isPresent()) {
            return friendRepository.findByPersonAcceptAndPersonRequest(personAccept, personRequest).get();
        } else {
            return friendRepository.findByPersonAcceptAndPersonRequest(personRequest, personAccept).orElse(null);
        }
    }

    //Method returns all Users that sent unaccepted request to person
    public List<Person> getUnacceptedRequests(Person currentUser) {
        return friendRepository.getAllByAcceptedAndPersonAccept(false, currentUser)
                .stream().map(Friendship::getPersonRequest).toList();
//        return getAll().stream().filter(p ->
//                !p.isAccepted() && p.getPersonAccept() == currentUser).toList()
//                .stream().map(Friendship::getPersonRequest).toList();
    }


    //Method returns all friends of person
    public List<Person> getFriends(Person currentUser) {
        return friendRepository.getAllByAcceptedAndPersonAcceptOrPersonRequest(true, currentUser, currentUser)
                .stream().flatMap(f ->
                        Stream.of(f.getPersonAccept(), f.getPersonRequest()))
                .distinct()
                .filter(a ->
                        !a.equals(currentUser)).toList();
//        return getAll().stream().filter(p ->
//                        p.isAccepted() && (p.getPersonAccept().getId() == currentUser.getId()
//                                || p.getPersonRequest().getId() == currentUser.getId())).toList()
//                .stream().flatMap(f->
//                        Stream.of(f.getPersonAccept(), f.getPersonRequest()))
//                .distinct()
//                .filter(a->
//                        !a.equals(currentUser)).toList();
    }

    public List<Person> getNotFriends(Person currentUser) {
        List<Person> list = getFriends(currentUser);
        return personDetailsService.getAllPeople().stream().limit(10)
                .filter(p -> !list.contains(p)).toList();
    }

    public List<Friendship> getAll() {
        return friendRepository.findAll();
    }

    public int getUnacceptedRequestsCount(Person person){
        return friendRepository.getAllByPersonAcceptAndAccepted(person, false).size();
    }

    public boolean isFriendshipExists(Person personRequest, Person personAccept) {
        return getFriendshipByPeople(personRequest, personAccept) != null;
//        return personRequest.getFriendshipList().stream().anyMatch(p ->
//                p.getPersonAccept().getId() == personAccept.getId());
    }

    public boolean isWaitingForResponse(Person personRequest, Person personAccept) {
        return getFriendshipByPeople(personRequest, personAccept) != null &&
                !getFriendshipByPeople(personRequest, personAccept).isAccepted();
    }

    public boolean isFriendshipRequestedByCurrent(Person personRequest, Person personAccept) {
        return personRequest.getRequestList().stream().anyMatch(p ->
                personAccept.getId() == p.getPersonAccept().getId());
    }

    @Transactional
    public void acceptFriendship(Person personRequest, Person personAccept) {
        getFriendshipByPeople(personRequest, personAccept).setAccepted(true);
    }

    @Transactional
    public void save(Friendship friendship) {
        friendRepository.save(friendship);
    }

    @Transactional
    public void assign(Person personRequest, Person personAccept) {
        Friendship friendship = new Friendship();
        friendship.setPersonRequest(personRequest);
        friendship.setPersonAccept(personAccept);
        save(friendship);
    }
}

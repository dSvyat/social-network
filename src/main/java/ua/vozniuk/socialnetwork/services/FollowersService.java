package ua.vozniuk.socialnetwork.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.vozniuk.socialnetwork.models.Follower;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.repositories.FollowersRepository;

import java.util.List;

@Service
public class FollowersService {
    private final FollowersRepository followersRepository;
    private final PersonDetailsService personDetailsService;

    public FollowersService(FollowersRepository followersRepository, PersonDetailsService personDetailsService) {
        this.followersRepository = followersRepository;
        this.personDetailsService = personDetailsService;
    }

    public List<Follower> findAll(){
        return followersRepository.findAll();
    }


    //returns List of people followed by Person
    public List<Person> getListFollowedByFollower(Person follower) {
        return findAll().stream().filter(p->
                p.getFollower().getId() == follower.getId()).toList()
                .stream().map(Follower :: getFollowed).toList();
    }

    //returns List of followers by Person
    public List<Person> getListFollowerByFollowed(Person followed){
        return followersRepository.getAllByFollowed(followed)
                .stream().map(Follower :: getFollower).toList();
//        return findAll().stream().filter(p->
//                p.getFollowed().getId() == followed.getId()).toList()
//                .stream().map(Follower :: getFollower).toList();
    }

    public Follower getFollowershipByPeople(Person follower, Person followed){
        return followersRepository.findByFollowerAndFollowed(follower, followed).orElse(null);
//        return findAll().stream().filter(p->
//                p.getFollowed().getId() == followed.getId() &&
//                        p.getFollower().getId() == follower.getId()).findFirst().orElse(null);
    }
    @Transactional
    public void deleteByFollowerAndFollowed(Person follower, Person followed){
        followersRepository.deleteByFollowerAndFollowed(follower, followed);
    }

    //returns true if person1 is following person2
    public boolean followershipExists(Person person1, Person person2){
        return getFollowershipByPeople(person1, person2) != null;
    }

    //Person needed to check if user is already following top-followed users
    public List<Person> getMostFollowed(Person person){
//        return personDetailsService.findTop10ByFollowers(person.getId());
        List<Person> list = personDetailsService.getAllPeople();
        List<Person> followedList = getListFollowedByFollower(personDetailsService.findById(person.getId()));
        list.sort((o1, o2) -> o2.getFollowersList().size() - o1.getFollowersList().size());
        return list.stream().filter(p->
                !followedList.contains(p)).limit(10).toList();
    }

    @Transactional
    public void save(Follower follower){
        followersRepository.save(follower);
    }

    @Transactional
    public void assign(Person follower, Person followed){
        Follower fl = new Follower();
        fl.setFollower(follower);
        fl.setFollowed(followed);
        save(fl);
    }
}
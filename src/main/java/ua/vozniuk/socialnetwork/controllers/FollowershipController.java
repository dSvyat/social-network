package ua.vozniuk.socialnetwork.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.security.PersonDetails;
import ua.vozniuk.socialnetwork.services.FollowersService;
import ua.vozniuk.socialnetwork.services.PersonDetailsService;

@Controller
@RequestMapping
public class FollowershipController {
    private final FollowersService followersService;
    private final PersonDetailsService personDetailsService;

    public FollowershipController(FollowersService followersService, PersonDetailsService personDetailsService) {
        this.followersService = followersService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/followings/{id}")
    public String followingPage(@PathVariable("id") int id, Model model){
        Person person = personDetailsService.findById(id);
        model.addAttribute("followings", followersService.getListFollowedByFollower(person));
        if (authenticated()){
            model.addAttribute("mostFollowed", followersService.getMostFollowed(person));
            model.addAttribute("person", person);
        }
        return "followership/followPage";
    }

    @GetMapping("/followers/{id}")
    public String  followerPage(@PathVariable("id") int id, Model model){
        model.addAttribute("followers", followersService.getListFollowerByFollowed(personDetailsService.findById(id)));
        if (authenticated()){
            model.addAttribute("person", getCurrentPerson());
        }
        return "followership/followersPage";
    }

    @PostMapping("/followerAccept/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void assignFollowershipThroughList(@PathVariable("id") int id){
        followersService.assign(getCurrentPerson(), personDetailsService.findById(id));
    }

    @PostMapping("/deleteFollowing/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteFollowing(@PathVariable("id") int id){
        followersService.deleteByFollowerAndFollowed(getCurrentPerson(), personDetailsService.findById(id));
    }

    private Person getCurrentPerson(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return personDetailsService.findById(
                ((PersonDetails)authentication.getPrincipal()).getId()
        );
    }
    private boolean authenticated(){
        return !(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken);
    }
}


package ua.vozniuk.socialnetwork.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.vozniuk.socialnetwork.models.Friendship;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.security.PersonDetails;
import ua.vozniuk.socialnetwork.services.FriendsService;
import ua.vozniuk.socialnetwork.services.PersonDetailsService;

@Controller
@RequestMapping
public class FriendsController {

    private final PersonDetailsService personDetailsService;
    private final FriendsService friendsService;

    public FriendsController(PersonDetailsService personDetailsService, FriendsService friendsService) {
        this.personDetailsService = personDetailsService;
        this.friendsService = friendsService;
    }

    @GetMapping("/friends/{id}")
    public String friends(@PathVariable("id") int id, Model model) {
        Person person = personDetailsService.findById(id);
        model.addAttribute("friends", friendsService.getFriends(person));
        model.addAttribute("people", friendsService.getNotFriends(person));
        model.addAttribute("person", person);
        if (authenticated()) {
            model.addAttribute("friendshipExists", friendsService.isFriendshipExists(getCurrentPerson(), person));
            model.addAttribute("friendshipRequestedByCurrent", friendsService.isFriendshipRequestedByCurrent(getCurrentPerson(), person));
            model.addAttribute("requestNotAccepted", friendsService.isWaitingForResponse(personDetailsService.findById(id), person));
            model.addAttribute("unacceptedCount", friendsService.getUnacceptedRequestsCount(getCurrentPerson()));
        }
        return "friends";
    }

    @GetMapping("/user/friend-requests")
    public String myRequests(Model model) {
        model.addAttribute("person", getCurrentPerson());
        model.addAttribute("unacceptedRequests", friendsService.getUnacceptedRequests(getCurrentPerson()));
        return "friendRequests";
    }

    @PostMapping("/user/friend-request/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void sendFriendRequest(@PathVariable("id") int id) {
        friendsService.assign(getCurrentPerson(), personDetailsService.findById(id));
    }

    @PostMapping("/user/accept-friend-profile/{id}")
    public String acceptFriendshipFromProfile(@PathVariable("id") int id) {
        friendsService.acceptFriendship(personDetailsService.findById(id), getCurrentPerson());
        return "redirect:/user/" + id;
    }

    @PostMapping("/user/accept-friend/{id}")
    public String acceptFriendshipFromRequests(@PathVariable("id") int id) {
        friendsService.acceptFriendship(personDetailsService.findById(id), getCurrentPerson());
        return "redirect:/user/friend-requests";
    }

    private Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return personDetailsService.findById(
                ((PersonDetails) authentication.getPrincipal()).getId()
        );
    }

    private boolean authenticated() {
        return !(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken);
    }
}

package ua.vozniuk.socialnetwork.controllers;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.vozniuk.socialnetwork.models.Post;
import ua.vozniuk.socialnetwork.services.*;
import ua.vozniuk.socialnetwork.util.CountryList;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.security.PersonDetails;

import java.io.*;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping
@Route("login")
@JavaScript("/static/javascript/homePage.js")
public class HomepageController extends Div {

    private final PersonDetailsService personDetailsService;
    private final FriendsService friendsService;
    private final Validator validator;
    private final FollowersService followersService;
    private final PostService postService;
    private final PostImageService postImageService;
    private final UserLikesService userLikesService;

    public HomepageController(PersonDetailsService personDetailsService, FriendsService friendsService, Validator validator, FollowersService followersService, PostService postService, PostImageService postImageService, UserLikesService userLikesService) {
        this.personDetailsService = personDetailsService;
        this.friendsService = friendsService;
        this.validator = validator;
        this.followersService = followersService;
        this.postService = postService;
        this.postImageService = postImageService;
        this.userLikesService = userLikesService;
        getElement().executeJs("greet($0)", "Dima");
    }

    @PostMapping("/uploadProfilePicture")
    public String handleProfilePictureUpload(@RequestParam("profilePic") MultipartFile file) {
        String fileName = "profileImg"+getCurrentPerson().getId();
        ImageService.saveFile(file, fileName);
        personDetailsService.setImg(getCurrentPerson(), fileName); //was "file" instead of fileName
        return"redirect:/user/" + getCurrentPerson().getId();
    }

    @GetMapping("/user/1")
    public String redirect(){
        return "redirect:/user/13";
    }

    @PostMapping("/uploadPost")
    public String uploadPost(@ModelAttribute("person") Post post, @RequestParam("postImgs")MultipartFile[] multipartFiles){
        Post temp = postService.create(post.getText(), getCurrentPerson());
        if(multipartFiles[0].getContentType().equals("image/jpeg")) {
            postImageService.create(temp, multipartFiles);
            postService.assignImageList(post);
        }
        return "redirect:/home";
    }

    @PostMapping(value = "/addLike/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addLike(@PathVariable("id") int id){
        postService.doLikeyStuff(postService.findById(id), getCurrentPerson());
    }

    @PostMapping("/deletePost/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePost(@PathVariable("id")int id){
        postImageService.deleteAllByPostId(id);
        postService.deleteByPostId(id);
    }

    @GetMapping("/home")
    public String homePage(Model model, @ModelAttribute("post") Post post) {
        model.addAttribute("person", getCurrentPerson());
        model.addAttribute("posts", postService.getPostsForUser(getCurrentPerson()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        model.addAttribute("formatter", formatter);
//        model.addAttribute("liked", userLikesService.existsByPersonAndPost(getCurrentPerson(), post));
        return "homePage";
    }

    @GetMapping("/user/image/{id}")
    public void showImage(@PathVariable("id") int id, HttpServletResponse response) throws  IOException{
        response.setContentType("image/jpg");
        InputStream inputStream = ImageService.getImage("profileImg"+id);
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/post/image/{key}")
    public void showPostImage(@PathVariable("key") String key, HttpServletResponse response) throws IOException{
        response.setContentType("image/jpg");
        InputStream inputStream = ImageService.getImage(key);
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/user/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person pageHolder = personDetailsService.findById(id);
        model.addAttribute("person", personDetailsService.findById(id));
        model.addAttribute("friendsAmount", friendsService.getFriends(pageHolder).size());
        model.addAttribute("followersAmount", followersService.getListFollowerByFollowed(pageHolder).size());
        model.addAttribute("followedAmount", followersService.getListFollowedByFollower(pageHolder).size());
        if (authenticated()) {
            model.addAttribute("friendshipExists", friendsService.isFriendshipExists(getCurrentPerson(), pageHolder));
            model.addAttribute("friendshipRequestedByCurrent", friendsService.isFriendshipRequestedByCurrent(getCurrentPerson(), pageHolder));
            model.addAttribute("requestNotAccepted", friendsService.isWaitingForResponse(pageHolder, getCurrentPerson()));
            model.addAttribute("followershipExists", followersService.followershipExists(getCurrentPerson(), pageHolder));
        }
        return "profile";
    }

    @PostMapping("/user/{id}")
    public String setAboutMe(@ModelAttribute("person") Person person) {
        personDetailsService.editAboutPerson(person);
        return "redirect:/user/{id}";
    }

    @PostMapping("/user/{id}/delete")
    public String deleteAcc(@PathVariable("id") int id) {
        personDetailsService.deleteById(id);
        return "redirect:/login";
    }

    @GetMapping("/user/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDetailsService.findById(id));
        model.addAttribute("countries", CountryList.COUNTRIES);
        return "edit";
    }

    @PostMapping("/user/{id}/edit")
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id,
                         BindingResult bindingResult, Model model) {
        model.addAttribute("countries", CountryList.COUNTRIES);
        validator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        personDetailsService.edit(id, person);
        return "redirect:/user/" + id;
    }

    private Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return personDetailsService.findById(
                ((PersonDetails) authentication.getPrincipal()).getId()
        );
    }
    private boolean authenticated(){
        return !(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken);
    }


}

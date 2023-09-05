package ua.vozniuk.socialnetwork.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "email_address")
    @NotEmpty(message = "Email can not be empty")
    @Email(message = "You should enter valid email")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password can not be empty")
    @Size(message = "Password is too short")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "country")
    @NotEmpty(message = "Country can not be empty")
    private String country;

    @Column(name = "date_of_birth")
    @NotEmpty(message = "Date of birth can not be empty")
    private String dateOfBirth;

    @Column(name = "given_name")
    @NotEmpty(message = "Given name can not be empty")
    @Size(max = 40, message = "given name is too short or too long")
    private String givenName;

    @Column(name = "surname")
    @NotEmpty(message = "Surname can not be empty")
    @Size(max = 40, message = "surname is too short or long")
    private String surname;

    @Column(name = "username")
    @NotEmpty(message = "Username can not be empty")
    private String username;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @OneToMany(mappedBy = "personRequest", cascade = CascadeType.REMOVE)
    private List<Friendship> requestList;

    @OneToMany(mappedBy = "personAccept", cascade = CascadeType.REMOVE)
    private List<Friendship> acceptList;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE)
    private List<Follower> followedList;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.REMOVE)
    private List<Follower> followersList;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Post> postList;

    @OneToMany(mappedBy = "person", cascade = CascadeType.REMOVE)
    private List<UserLikes> userLikes;

    @Column(name = "image")
    private String image;
//    @Column(name="image")
//    private byte[] img = getDefaultImage();
//
//    private byte[] getDefaultImage(){
//        try {
//            Path defaultImagePath =
//                    Paths.get(getClass().getClassLoader().getResource("static/emptyProfilePicture.png").toURI());
//            return Files.readAllBytes(defaultImagePath);
//        } catch (URISyntaxException | IOException e){
//            e.printStackTrace();
//            return null;
//        }
}

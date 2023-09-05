package ua.vozniuk.socialnetwork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "text")
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column (name="likes")
    private int likes;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id")
    private Person author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostImage> postImageList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<UserLikes> postLikes;

    @Transient
    public boolean isLikedBy(Person person){
        return this.postLikes.stream().anyMatch(u->
                u.getPerson().equals(person));
    }
}

package ua.vozniuk.socialnetwork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_likes")
@Getter
@Setter
public class UserLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "post", referencedColumnName = "id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "person", referencedColumnName = "id")
    private Person person;
}

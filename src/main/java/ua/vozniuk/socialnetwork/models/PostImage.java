package ua.vozniuk.socialnetwork.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_img")
@Getter
@Setter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "file_key")
    private String fileKey;

    @ManyToOne
    @JoinColumn(name = "post", referencedColumnName = "id")
    private Post post;
}

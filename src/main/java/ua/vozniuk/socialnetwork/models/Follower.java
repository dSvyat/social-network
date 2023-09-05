package ua.vozniuk.socialnetwork.models;


import jakarta.persistence.*;

@Entity
@Table(name="follower")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "follower", referencedColumnName = "id")
    private Person follower;

    @ManyToOne
    @JoinColumn(name = "followed", referencedColumnName = "id")
    private Person followed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getFollower() {
        return follower;
    }

    public void setFollower(Person follower) {
        this.follower = follower;
    }

    public Person getFollowed() {
        return followed;
    }

    public void setFollowed(Person followed) {
        this.followed = followed;
    }
}

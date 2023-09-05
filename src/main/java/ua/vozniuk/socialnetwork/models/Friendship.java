package ua.vozniuk.socialnetwork.models;

import jakarta.persistence.*;

@Entity
@Table(name="friend_relations")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_request", referencedColumnName = "id")
    private Person personRequest;

    @ManyToOne
    @JoinColumn(name = "person_accept", referencedColumnName = "id")
    private Person personAccept;

    @Column(name="accepted")
    private boolean accepted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPersonRequest() {
        return personRequest;
    }

    public void setPersonRequest(Person personRequest) {
        this.personRequest = personRequest;
    }

    public Person getPersonAccept() {
        return personAccept;
    }

    public void setPersonAccept(Person personAccept) {
        this.personAccept = personAccept;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}


package ua.vozniuk.socialnetwork.util;

import com.amazonaws.services.synthetics.model.transform.CanaryCodeOutputMarshaller;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.models.Post;

import java.util.Comparator;

public class Sorting implements Comparator<Person> {
    public int compare(Person a, Person b){
        return b.getFollowersList().size() - a.getFollowersList().size();
    }
    class SortPostsByDate implements Comparator<Post> {
        public int compare(Post a, Post b){
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }
}

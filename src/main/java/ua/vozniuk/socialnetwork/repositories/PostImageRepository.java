package ua.vozniuk.socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.vozniuk.socialnetwork.models.Post;
import ua.vozniuk.socialnetwork.models.PostImage;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    public List<PostImage> findAllByPostId(int id);
    List<PostImage> getAllByPost(Post post);
    void deleteAllByPostId(int postId);
}

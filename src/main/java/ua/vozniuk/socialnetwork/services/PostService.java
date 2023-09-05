package ua.vozniuk.socialnetwork.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vozniuk.socialnetwork.models.Person;
import ua.vozniuk.socialnetwork.models.Post;
import ua.vozniuk.socialnetwork.models.PostImage;
import ua.vozniuk.socialnetwork.models.UserLikes;
import ua.vozniuk.socialnetwork.repositories.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final FriendsService friendsService;
    private final FollowersService followersService;
    private final UserLikesService userLikesService;

    public PostService(PostRepository postRepository, PostImageService postImageService, FriendsService friendsService, FollowersService followersService, UserLikesService userLikesService) {
        this.postRepository = postRepository;
        this.postImageService = postImageService;
        this.friendsService = friendsService;
        this.followersService = followersService;
        this.userLikesService = userLikesService;
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    public Post findById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public Post doLikeyStuff(Post post, Person person) {
        Optional<Post> post1 = postRepository.findById(post.getId());
        UserLikes userLikes = userLikesService.getByPersonAndPost(person, post);
        if(userLikes == null) {
            userLikesService.save(person, post);
            post1.get().setLikes(post.getLikes() + 1);
        } else {
            userLikesService.deleteByPersonAndPost(person, post);
            post1.get().setLikes(post.getLikes()-1);
        }
        save(post1.get());
        return post;
    }

    public Post create(String text, Person author) {
        Post post = new Post();
        post.setText(text);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        save(post);
        return post;
    }

    @Transactional
    public void assignImageList(Post post) {
        List<PostImage> postImageList = postImageService.findAllByPostId(post.getId());
        post.setPostImageList(postImageList);
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public List<Post> findAllByAuthorOrderByCreatedAtDesc(Person author) {
        return postRepository.findAllByAuthorOrderByCreatedAtDesc(author);
    }


    public List<Post> getPostsForUser(Person person) {
        List<Person> friendList = friendsService.getFriends(person);
        List<Person> followedList = followersService.getListFollowedByFollower(person);
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return posts.stream().filter(p ->
                friendList.contains(p.getAuthor()) || followedList.contains(p.getAuthor()) || p.getAuthor() == person).toList();
    }

    @Transactional
    public void deleteByPostId(int postId){
        postRepository.deleteById(postId);
    }
}
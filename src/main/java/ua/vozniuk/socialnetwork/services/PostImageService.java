package ua.vozniuk.socialnetwork.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.vozniuk.socialnetwork.models.Post;
import ua.vozniuk.socialnetwork.models.PostImage;
import ua.vozniuk.socialnetwork.repositories.PostImageRepository;

import java.util.List;

@Service
public class PostImageService {
    private final PostImageRepository postImageRepository;

    public PostImageService(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    @Transactional
    public void save(PostImage postImage){
        postImageRepository.save(postImage);
    }

    public List<PostImage> findAllByPostId(int id){
        return postImageRepository.findAllByPostId(id);
    }

    public void create(Post post, MultipartFile[] multipartFiles){
        for (int i = 0; i < multipartFiles.length; i++) {
            ImageService.saveFile(multipartFiles[i], post.getId() + "-" + i);
            PostImage postImage = new PostImage();
            postImage.setPost(post);
            postImage.setFileKey(post.getId() + "-" + i);
            save(postImage);
        }
    }

    @Transactional
    public void deleteAllByPostId(int postId){
        postImageRepository.deleteAllByPostId(postId);
    }
}

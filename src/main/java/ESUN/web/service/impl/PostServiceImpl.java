package ESUN.web.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ESUN.web.entity.Post;
import ESUN.web.entity.User;
import ESUN.web.repository.PostRepository;
import ESUN.web.repository.UserRepository;
import ESUN.web.service.PostService;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional //確保發文過程若出錯不會留下髒資料
    public Post createPost(Integer userId, String content) {
        //確認這個發文的使用者是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到該使用者，發文失敗！"));

        //建立新的 Post 物件並塞入資料
        Post post = new Post();
        post.setUser(user);      //綁定發文者
        post.setContent(content); //寫入發文內容
        post.setCreatedAt(LocalDateTime.now()); //記錄發文時間

        //存入資料庫
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        //取得資料庫中所有的文章
        return postRepository.findAll();
    }
}
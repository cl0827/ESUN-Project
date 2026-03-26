package ESUN.web.service;

import java.util.List;

import ESUN.web.entity.Post;

public interface PostService {
    //建立新文章
    Post createPost(Integer userId, String content);
    
    //取得所有文章
    List<Post> getAllPosts();
}
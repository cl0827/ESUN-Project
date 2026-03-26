package ESUN.web.service;

public interface CommentService {
    void createComment(Integer userId, Integer postId, String content);
}
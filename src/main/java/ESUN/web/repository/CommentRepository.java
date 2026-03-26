package ESUN.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ESUN.web.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    // 呼叫 Stored Procedure 新增留言 (防範 SQL Injection)
    @Procedure(procedureName = "add_comment")
    void addCommentSp(@Param("p_user_id") Integer userId, 
                      @Param("p_post_id") Integer postId, 
                      @Param("p_content") String content);
}
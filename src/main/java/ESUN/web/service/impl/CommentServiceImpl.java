package ESUN.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import ESUN.web.repository.CommentRepository;
import ESUN.web.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional // 確保 Transaction
    public void createComment(Integer userId, Integer postId, String content) {
        // XSS 防護：將特殊字元跳脫，例如 <script> 變成 &lt;script&gt;
        String safeContent = HtmlUtils.htmlEscape(content);
        
        // 呼叫 Stored Procedure 存入資料庫
        commentRepository.addCommentSp(userId, postId, safeContent);
    }
}
package ESUN.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ESUN.web.service.CommentService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Map<String, Object> request) {
        try {
            Object userIdObj = request.get("userId");
            Object postIdObj = request.get("postId");
            Object contentObj = request.get("content");

            if (userIdObj == null || postIdObj == null || contentObj == null) {
                return ResponseEntity.badRequest().body("留言失敗：缺少必要參數！");
            }

            Integer userId = Integer.parseInt(userIdObj.toString());
            Integer postId = Integer.parseInt(postIdObj.toString());
            String content = contentObj.toString();

            commentService.createComment(userId, postId, content);
            return ResponseEntity.ok("留言成功！");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("留言失敗：" + e.getMessage());
        }
    }
}
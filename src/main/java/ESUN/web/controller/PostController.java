package ESUN.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ESUN.web.entity.Post;
import ESUN.web.service.PostService;

@CrossOrigin(origins = "http://localhost:5173") // 允許 Vue 前端跨域請求
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    //新增發文 (POST http://localhost:8080/api/posts)
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request) {
        try {
            //從前端傳來的 JSON 中取得 userId 與 content
            //通常會用 DTO 物件來接，這裡為了快速測試先用 Map
            Object userIdObj = request.get("userId");
            if (userIdObj == null) {
                userIdObj = request.get("user_id");
            }

            if (userIdObj == null) {
                return ResponseEntity.badRequest().body("發文失敗：無法取得發文者的 ID，請重新登入！");
            }

            //轉型
            Integer userId = Integer.parseInt(userIdObj.toString());
            String content = request.get("content").toString();
            
            postService.createPost(userId, content);
            return ResponseEntity.ok("發文成功！");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("發文失敗：" + e.getMessage());
        }
    }

    //取得所有文章 (GET http://localhost:8080/api/posts)
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
}
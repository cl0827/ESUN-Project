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

    // 1. 新增發文 (POST http://localhost:8080/api/posts)
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Map<String, String> request) {
        try {
            // 從前端傳來的 JSON 中取得 userId 與 content
            // 實務上通常會用 DTO 物件來接，這裡為了快速測試先用 Map
            Integer userId = Integer.parseInt(request.get("userId"));
            String content = request.get("content");
            
            Post post = postService.createPost(userId, content);
            return ResponseEntity.ok("發文成功！");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("發文失敗：" + e.getMessage());
        }
    }

    // 2. 取得所有文章 (GET http://localhost:8080/api/posts)
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
}
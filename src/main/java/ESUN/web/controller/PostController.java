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

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // 允許 Vue 前端跨域請求
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    //新增發文 (POST http://localhost:8080/api/posts)
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request) {
        try {
        System.out.println("收到發文請求，內容：" + request);

        // 1. 安全取得 userId
        Object userIdObj = request.getOrDefault("userId", request.get("user_id"));
        if (userIdObj == null) {
            return ResponseEntity.badRequest().body("發文失敗：找不到使用者 ID");
        }

        // 2. 安全取得 content 並進行 XSS 防護 (這是你要求的防護)
        Object contentObj = request.get("content");
        if (contentObj == null || contentObj.toString().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("發文失敗：內容不可為空");
        }
        
        // 使用 Spring 內建工具防止 XSS 攻擊
        String safeContent = org.springframework.web.util.HtmlUtils.htmlEscape(contentObj.toString());

        Integer userId = Integer.parseInt(userIdObj.toString());
        
        // 3. 執行 Service
        postService.createPost(userId, safeContent);
        
        return ResponseEntity.ok("發文成功！");
    } catch (Exception e) {
        e.printStackTrace(); // 發生錯誤時在 Console 印出詳細原因
        return ResponseEntity.status(500).body("系統錯誤：" + e.getMessage());
    }
    }

    //取得所有文章 (GET http://localhost:8080/api/posts)
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
}
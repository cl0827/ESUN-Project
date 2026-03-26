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
            // 💡 加入這行：印出前端實際送來的 JSON 解析結果
            System.out.println("收到發文請求，內容：" + request);

            Object userIdObj = request.get("userId");
            if (userIdObj == null) {
                userIdObj = request.get("user_id");
            }

            if (userIdObj == null) {
                // 💡 把 request 內容也印在錯誤訊息中，方便在前端 F12 直接看
                return ResponseEntity.badRequest().body("發文失敗：無法取得發文者的 ID，請重新登入！\n(後端實際收到的資料：" + request + ")");
            }

            Integer userId = Integer.parseInt(userIdObj.toString());
            String content = request.get("content").toString();
            
            postService.createPost(userId, content);
            return ResponseEntity.ok("發文成功！");
        } catch (Exception e) {
            e.printStackTrace();
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
package ESUN.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ESUN.web.entity.User;
import ESUN.web.service.UserService;

@CrossOrigin(origins = "http://localhost:5173") // 允許Vue前端存取
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // POST http://localhost:8080/api/users/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok("註冊成功！歡迎 " + savedUser.getUserName());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST http://localhost:8080/api/users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            // 呼叫 Service 的登入邏輯
            User user = userService.login(loginRequest.getPhone(), loginRequest.getPassword());
            return ResponseEntity.ok(user); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
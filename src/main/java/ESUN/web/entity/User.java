package ESUN.web.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data // Lombok 自動生成 Getter/Setter/ToString 等方法
@Entity // 宣告這是一個 JPA 實體
@Table(name = "User") // 對應資料庫中的 User 表格

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    
     // unique = true：確保手機號碼不會重複註冊
     // length = 20：預留給國際格式或不同長度的號碼
     
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}

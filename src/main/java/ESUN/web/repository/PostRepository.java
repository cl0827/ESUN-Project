package ESUN.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ESUN.web.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    
    // 因為 JpaRepository 已經內建了 findAll() 抓所有文章、save() 存文章等功能
    // 先留空，有需要根據某個特定條件找文章再加

}
package ESUN.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import ESUN.web.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhone(String phone); // 用於登入與檢查重複註冊

    // 如果你有寫好的 Stored Procedure 叫做 "UpdateUserBio"
    @Procedure(name = "UpdateUserBio")
    void updateUserBio(Integer userId, String newBio);
}
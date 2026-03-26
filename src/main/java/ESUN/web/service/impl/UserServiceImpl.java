package ESUN.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ESUN.web.entity.User;
import ESUN.web.repository.UserRepository;
import ESUN.web.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 呼叫在 Config 設定的加密工具

    @Override
    @Transactional // 確保資料一致性，符合Transaction要求
    public User register(User user) {
        //檢查手機號碼是否已存在
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("該手機號碼已註冊過囉！");
        }

        //salt and hash
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //存入資料庫
        return userRepository.save(user);
    }

    @Override
    public User login(String phone, String password) {
        //透過手機號碼尋找使用者
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("找不到此手機號碼，請先註冊。"));

        //驗證密碼
        //用passwordEncoder matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密碼輸入錯誤！");
        }

        //驗證成功，回傳使用者資訊
        return user;
    }
}
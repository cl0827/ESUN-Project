package ESUN.web.service;

import ESUN.web.entity.User;

public interface  UserService {
    //定義註冊
    User register(User user);
    //定義登入
    User login(String phone, String password);
}

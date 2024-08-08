package services;

import dao.UserDao;
import models.Role;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public Role getRoleOfUser(long userId) {
        return userDao.getRoleOfUser(userId);
    }
}

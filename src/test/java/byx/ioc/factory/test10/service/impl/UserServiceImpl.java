package byx.ioc.factory.test10.service.impl;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.factory.test10.dao.UserDao;
import byx.ioc.factory.test10.service.UserService;

@Component
public class UserServiceImpl implements UserService {
    @Autowire
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }
}

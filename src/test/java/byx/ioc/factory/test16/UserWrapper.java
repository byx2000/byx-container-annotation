package byx.ioc.factory.test16;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
public class UserWrapper {
    private User user;

    @Autowired
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

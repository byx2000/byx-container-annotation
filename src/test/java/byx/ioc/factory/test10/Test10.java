package byx.ioc.factory.test10;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test10.dao.UserDao;
import byx.ioc.factory.test10.dao.impl.UserDaoImpl;
import byx.ioc.factory.test10.service.UserService;
import byx.ioc.factory.test10.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Test10 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory(Test10.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        UserService userService = container.getObject(UserService.class);
        UserDao userDao = container.getObject(UserDao.class);

        assertTrue(userService instanceof UserServiceImpl);
        assertTrue(userDao instanceof UserDaoImpl);
        assertSame(((UserServiceImpl) userService).getUserDao(), userDao);
    }
}

package byx.ioc.factory.test16;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component
public class User {
    private Integer id;
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    @Autowired
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Autowired
    public void setUsername(@Id("username") String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Autowired
    public void setPassword(@Id("password") String password) {
        this.password = password;
    }
}

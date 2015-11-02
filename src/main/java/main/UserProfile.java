package main;


public class UserProfile {
    private String login;
    private String password;
    private String email;
    private Integer id;

    public UserProfile(String login, String password, String email, Integer id) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = id;
    }
    public Integer getId(){return id;}

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}

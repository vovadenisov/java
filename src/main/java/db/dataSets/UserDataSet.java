package db.dataSets;

import javax.persistence.*;

/**
 * Created by alla on 21.11.15.
 */

/*
    DataSet ― объект содержащий данные одной строки таблицы На каждую таблицу свой DataSet
 */
@Entity
@Table(name = "users")
public class UserDataSet {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    public UserDataSet() {
    }

    public UserDataSet(long id, String login, String email, String password) {
        this.setId(id);
        this.setLogin(login);
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId(){ return id;}

    public void setId(long id){
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

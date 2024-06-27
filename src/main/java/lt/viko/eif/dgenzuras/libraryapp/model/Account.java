package lt.viko.eif.dgenzuras.libraryapp.model;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Account class holds information about the account.
 *
 * @author dainius.genzuras@stud.viko.lt
 * @since 1.0
 * @see Reader
 */

public class Account {
    private int id;

    private String userName;
    private String password;

    public Account(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public Account() {}

    @Override
    public String toString() {
        return String.format(
                "\n\t\tUser name: %s \n" + "\t\tPassword: %s \n",
                this.userName,
                this.password);
    }

    @XmlAttribute(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute(name = "Username")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @XmlAttribute(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

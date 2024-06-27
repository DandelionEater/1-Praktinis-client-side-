package lt.viko.eif.dgenzuras.libraryapp.model;


import sun.text.resources.cldr.ti.FormatData_ti_ER;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader class holds information about the readers profile.
 *
 * @author dainius.genzuras@stud.viko.lt
 * @since 1.0
 * @see Account
 * @see Book
 */

@XmlRootElement
public class Reader {
    private int id;
    private String firstName;
    private String lastName;
    private int code;

    private Account account;

    private List<Book> bookList = new ArrayList<>();

    public Reader(int id, String firstName, String lastName, int code) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
    }

    public Reader() {}

    @Override
    public String toString() {
        return String.format("Reader:\n" +
                "\tFirst name: %s\n" +
                "\tLast name: %s\n" +
                "\tCode: %s\n" +
                "\tAccount: %s\n" +
                "%s",
                this.firstName,
                this.lastName,
                this.code,
                account,
                constructBookList());
    }

    private String constructBookList() {
        String result = "";
        for (Book book : bookList){
            result += book;
        }
        return result;
    }

    @XmlAttribute(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }


    @XmlAttribute(name = "FirstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    @XmlAttribute(name = "LastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    @XmlAttribute(name = "Code")
    public int getCode() {
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    @XmlElement(name = "BookList")
    public List<Book> getBookList() { return bookList; }

    @XmlElement(name = "Account")
    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }
}

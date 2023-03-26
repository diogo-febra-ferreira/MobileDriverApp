package pt.pleiria.estg.myapplication;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String phoneNumber;
    public String licensePlate;
    public String balance;
    public String deletedAt;


    public User(String firstName, String lastName, String email, String password, String phoneNumber, String licensePlate, String balance, String deletedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.licensePlate = licensePlate;
        this.balance = balance;
        this.deletedAt = deletedAt;
    }

    public User() {
    }


}

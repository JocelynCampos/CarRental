package se.edugrade.carrental.entities;

import jakarta.persistence.*;

/*Erik Edman*/

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_security_number", unique = true, nullable = false, length = 15)
    private String socialSecurityNumber;

    @Column(name = "first_name", nullable = false, length = 40)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 40)
    private String lastName;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "email", nullable = true, length = 30)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private Role role;


    public enum Role {
        CUSTOMER,
        ADMIN
    }

    public User() {
    }

    public User(String socialSecurityNumber, String firstName, String lastName, String address, String phoneNumber, String email, Role role) {
        this.socialSecurityNumber = socialSecurityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}



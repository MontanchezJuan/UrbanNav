package com.urbanNav.security.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document()
public class User {
    @Id
    private String _id;
    private String email;
    private String password;
    private int status;
    private LocalDateTime created_at;
    private String twofactor_code;
    @DBRef
    private Role role;
    @DBRef
    private List<CreditCard> creditCards;
    @DBRef
    private UserProfile userProfile;

    public User() {
    }

    public User(String email, String password, int status, String twofactor_code) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.twofactor_code = twofactor_code;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getTwofactor_code() {
        return twofactor_code;
    }

    public void setTwofactor_code(String twofactor_code) {
        this.twofactor_code = twofactor_code;
    }

    public void addCreditCard(CreditCard creditCard) {
        this.creditCards.add(creditCard);
    }

    public void removeCreditCard(CreditCard creditCard) {
        this.creditCards.remove(creditCard);
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile(UserProfile userProfile) {
        return this.userProfile;
    }
}

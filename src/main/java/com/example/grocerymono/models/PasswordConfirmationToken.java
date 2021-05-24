package com.example.grocerymono.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document("password-tokens")
public class PasswordConfirmationToken {

    @Id
    private String id;
    private String confirmationToken;
    @CreatedDate
    private Date createdDate;

    @DBRef
    private User user;

    public PasswordConfirmationToken()
    {

    }

    public PasswordConfirmationToken(User user) {
        this.user=user;
        this.confirmationToken = UUID.randomUUID().toString();
        this.createdDate=new Date();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

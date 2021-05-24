package com.example.grocerymono.models;

import com.example.grocerymono.models.enums.Gender;
import com.example.grocerymono.models.enums.Occupancy;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "properties")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    private String id;
    private String userId;
    private double rent;
    private String location;
    private List<String> photosUrl;
    private Occupancy occupancy;
    private Gender lookingFor;
    private String availabilityDate;
    private List<String> amenities;
    private String contactNumber;
    private boolean showContactNumber;
    private String description;
    private boolean pgInterested;
    private boolean checkPropertyOwner;

}

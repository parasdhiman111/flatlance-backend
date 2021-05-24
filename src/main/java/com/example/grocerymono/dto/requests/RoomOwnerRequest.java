package com.example.grocerymono.dto.requests;


import com.example.grocerymono.models.enums.Gender;
import com.example.grocerymono.models.enums.Occupancy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomOwnerRequest {

    private double rent;
    private String location;
    private List<String> photosUrl;
    private String occupancy;
    private String lookingFor;
    private String availabilityDate;
    private List<String> amenities;
    private boolean showContactNumber;
    private String description;
}

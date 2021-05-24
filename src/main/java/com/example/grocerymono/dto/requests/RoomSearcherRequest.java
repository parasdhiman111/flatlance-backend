package com.example.grocerymono.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearcherRequest {
    private double rent;
    private String location;
    private String occupancy;
    private String lookingFor;
    private String availabilityDate;
    private String description;
    private boolean checkForPgInterest;
    private boolean showContactNumber;

}

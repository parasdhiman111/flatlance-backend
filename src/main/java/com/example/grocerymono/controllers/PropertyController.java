package com.example.grocerymono.controllers;

import com.example.grocerymono.dto.requests.RoomOwnerRequest;
import com.example.grocerymono.dto.requests.RoomSearcherRequest;
import com.example.grocerymono.models.Property;
import com.example.grocerymono.models.User;
import com.example.grocerymono.models.enums.Gender;
import com.example.grocerymono.models.enums.Occupancy;
import com.example.grocerymono.repositories.PropertyRepository;
import com.example.grocerymono.repositories.UserRepository;
import com.example.grocerymono.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class PropertyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${paras.app.jwtSecret}")
    public String jwtSecret;

    @Autowired
    private PropertyRepository propertyRepository;


    @GetMapping("/properties/owner")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getAllOwnersListedProperties()
    {
        return ResponseEntity.ok(propertyRepository.findByCheckPropertyOwner(true));
    }

    @GetMapping("/properties/owner/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getAllOwnersListedPropertiesByCurrentUser(@PathVariable String id)
    {
        return ResponseEntity.ok(propertyRepository.findByCheckPropertyOwnerAndUserId(true,id));
    }

    @PostMapping("/properties/owner")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addNewOwnerProperty(@RequestHeader("Authorization") String authorization,@RequestBody RoomOwnerRequest request)
    {

        String userEmail=jwtUtils.getUserNameFromJwtToken(authorization.substring(7));
        Optional<User> user=userRepository.findByEmail(userEmail);



        Property property=new Property();
        property.setUserId(user.get().getUserId());
        property.setLocation(request.getLocation());
        property.setRent(request.getRent());
        property.setPhotosUrl(request.getPhotosUrl());
        property.setAvailabilityDate(request.getAvailabilityDate());
        property.setAmenities(request.getAmenities());
        property.setContactNumber(user.get().getContactNumber());
        property.setDescription(request.getDescription());
        property.setShowContactNumber(request.isShowContactNumber());
        property.setCheckPropertyOwner(true);
        String occupancy=request.getOccupancy();
       if(occupancy.equalsIgnoreCase("single"))
           property.setOccupancy(Occupancy.SINGLE);
       else if(occupancy.equalsIgnoreCase("shared"))
           property.setOccupancy(Occupancy.SHARED);
        else
            property.setOccupancy(Occupancy.ANY);

        String preference=request.getLookingFor();
        if(preference.equalsIgnoreCase("male"))
            property.setLookingFor(Gender.MALE);
        else if(preference.equalsIgnoreCase("female"))
            property.setLookingFor(Gender.FEMALE);
        else
            property.setLookingFor(user.get().getGender());

        propertyRepository.save(property);

        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }


    @PutMapping("/properties/owner/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> updateOwnerProperty(@RequestHeader String authorization,@PathVariable String id, @RequestBody RoomOwnerRequest request)
    {
        String userId=getUserIdFromToken(authorization.substring(7));
        Optional<User> user=userRepository.findById(userId);
        Optional<Property> property = propertyRepository.findById(id);
        if(property.isPresent() && userId.equals(property.get().getUserId()))
        {
            property.get().setLocation(request.getLocation());
            property.get().setRent(request.getRent());
            property.get().setPhotosUrl(request.getPhotosUrl());
            property.get().setAvailabilityDate(request.getAvailabilityDate());
            property.get().setAmenities(request.getAmenities());
            property.get().setContactNumber(user.get().getContactNumber());
            property.get().setDescription(request.getDescription());
            property.get().setShowContactNumber(request.isShowContactNumber());
            property.get().setCheckPropertyOwner(true);
            String occupancy=request.getOccupancy();
            if(occupancy.equalsIgnoreCase("single"))
                property.get().setOccupancy(Occupancy.SINGLE);
            else if(occupancy.equalsIgnoreCase("shared"))
                property.get().setOccupancy(Occupancy.SHARED);
            else
                property.get().setOccupancy(Occupancy.ANY);

            String preference=request.getLookingFor();
            if(preference.equalsIgnoreCase("male"))
                property.get().setLookingFor(Gender.MALE);
            else if(preference.equalsIgnoreCase("female"))
                property.get().setLookingFor(Gender.FEMALE);
            else
                property.get().setLookingFor(user.get().getGender());
            return ResponseEntity.ok(propertyRepository.save(property.get()));
        }
        return ResponseEntity.badRequest().body("Not Allowed");
    }

    @DeleteMapping("/properties/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> deleteOwnerProperty(@RequestHeader String authorization,@PathVariable String id)
    {
        String userId=getUserIdFromToken(authorization.substring(7));
        Optional<Property> property = propertyRepository.findById(id);
        if(property.isPresent() && userId.equals(property.get().getUserId()))
        {
            propertyRepository.delete(property.get());
            return ResponseEntity.ok("Deleted");

        }
        return ResponseEntity.badRequest().body("Not Allowed");
    }




    @GetMapping("/properties/searcher")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getAllSearchersListedProperties()
    {
        return ResponseEntity.ok(propertyRepository.findByCheckPropertyOwner(false));
    }

    @GetMapping("/properties/searcher/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getAllSearchersListedPropertiesByCurrentUser(@PathVariable String id)
    {
        return ResponseEntity.ok(propertyRepository.findByCheckPropertyOwnerAndUserId(false,id));
    }

    @PostMapping("/properties/searcher")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addNewSearcherProperty(@RequestHeader("Authorization") String authorization,@RequestBody RoomSearcherRequest request)
    {

        String userEmail=jwtUtils.getUserNameFromJwtToken(authorization.substring(7));
        Optional<User> user=userRepository.findByEmail(userEmail);
        Property property=new Property();
        property.setUserId(user.get().getUserId());
        property.setLocation(request.getLocation());
        property.setRent(request.getRent());
        property.setAvailabilityDate(request.getAvailabilityDate());
        property.setContactNumber(user.get().getContactNumber());
        property.setDescription(request.getDescription());
        property.setShowContactNumber(request.isShowContactNumber());
        property.setCheckPropertyOwner(false);
        property.setPgInterested(request.isCheckForPgInterest());
        String occupancy=request.getOccupancy();
        if(occupancy.equalsIgnoreCase("single"))
            property.setOccupancy(Occupancy.SINGLE);
        else if(occupancy.equalsIgnoreCase("shared"))
            property.setOccupancy(Occupancy.SHARED);
        else
            property.setOccupancy(Occupancy.ANY);

        String preference=request.getLookingFor();
        if(preference.equalsIgnoreCase("male"))
            property.setLookingFor(Gender.MALE);
        else if(preference.equalsIgnoreCase("female"))
            property.setLookingFor(Gender.FEMALE);
        else
            property.setLookingFor(user.get().getGender());

        propertyRepository.save(property);

        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }


    @PutMapping("/properties/searcher/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> updateSearcherProperty(@RequestHeader String authorization,@PathVariable String id, @RequestBody RoomSearcherRequest request)
    {
        String userId=getUserIdFromToken(authorization.substring(7));
        Optional<User> user=userRepository.findById(userId);
        Optional<Property> property = propertyRepository.findById(id);
        if(property.isPresent() && userId.equals(property.get().getUserId()))
        {
            property.get().setLocation(request.getLocation());
            property.get().setRent(request.getRent());
            property.get().setAvailabilityDate(request.getAvailabilityDate());
            property.get().setContactNumber(user.get().getContactNumber());
            property.get().setDescription(request.getDescription());
            property.get().setShowContactNumber(request.isShowContactNumber());
            property.get().setCheckPropertyOwner(false);
            property.get().setPgInterested(request.isCheckForPgInterest());
            String occupancy=request.getOccupancy();
            if(occupancy.equalsIgnoreCase("single"))
                property.get().setOccupancy(Occupancy.SINGLE);
            else if(occupancy.equalsIgnoreCase("shared"))
                property.get().setOccupancy(Occupancy.SHARED);
            else
                property.get().setOccupancy(Occupancy.ANY);

            String preference=request.getLookingFor();
            if(preference.equalsIgnoreCase("male"))
                property.get().setLookingFor(Gender.MALE);
            else if(preference.equalsIgnoreCase("female"))
                property.get().setLookingFor(Gender.FEMALE);
            else
                property.get().setLookingFor(user.get().getGender());
            return ResponseEntity.ok(propertyRepository.save(property.get()));
        }
        return ResponseEntity.badRequest().body("Not Allowed");
    }


    private String getUserIdFromToken(String token)
    {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getId();
    }




}

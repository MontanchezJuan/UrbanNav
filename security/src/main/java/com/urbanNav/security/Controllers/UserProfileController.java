package com.urbanNav.security.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.urbanNav.security.Models.Permission;
import com.urbanNav.security.Models.UserProfile;
import com.urbanNav.security.Repositories.UserProfileRepository;

@CrossOrigin
@RestController
@RequestMapping("api/profile")
public class UserProfileController {
    @Autowired
    private UserProfileRepository profileRepository;

    @GetMapping("")
    public List<UserProfile> index(){
        return this.profileRepository.findAll();    
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserProfile store(@RequestBody UserProfile userProfile){
        return this.profileRepository.save(userProfile);
    }

    @GetMapping("{id}")
    public UserProfile show(@PathVariable String id){
        UserProfile profile = this.profileRepository.findById(id).orElse(null);
        return profile;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{id}")
    public UserProfile update(@PathVariable String id, @RequestBody UserProfile profile){
        UserProfile current = this.profileRepository.findById(id).orElse(null);
        if (current != null) {
            current.setName(profile.getName());
            current.setLastName(profile.getName());
            current.setBirthday(profile.getBirthday());            
            current.setBackgroundImage(profile.getBackgroundImage());
            current.setProfilePhoto(profile.getProfilePhoto());
            current.setNumberPhone(profile.getNumberPhone());
            return this.profileRepository.save(current);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        UserProfile profile = this.profileRepository.findById(id).orElse(null);
        if (profile != null ) {
            this.profileRepository.delete(profile);
        }
    }
}
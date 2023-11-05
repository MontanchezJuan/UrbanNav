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

import com.urbanNav.security.Models.User;
import com.urbanNav.security.Models.UserProfile;
import com.urbanNav.security.Repositories.UserProfileRepository;
import com.urbanNav.security.Repositories.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/profiles")
public class UserProfileController {
    @Autowired
    private UserProfileRepository profileRepository;
    @Autowired UserRepository userRepository;

    @GetMapping("")
    public List<UserProfile> index() {
        return this.profileRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserProfile store(@RequestBody UserProfile userProfile) {        
        return this.profileRepository.save(userProfile);
    }

    @GetMapping("{id}")
    public UserProfile show(@PathVariable String id) {
        UserProfile profile = this.profileRepository.findById(id).orElse(null);
        return profile;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{id}")
    public UserProfile update(@PathVariable String id, @RequestBody UserProfile profile) {
        UserProfile current = this.profileRepository.findById(id).orElse(null);
        if (current != null) {
            current.setName(profile.getName());
            current.setLastName(profile.getLastName());
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
    public void destroy(@PathVariable String id) {
        UserProfile profile = this.profileRepository.findById(id).orElse(null);
        if (profile != null) {
            this.profileRepository.delete(profile);
        }
    }

    @PutMapping("{profile_id}/user/{user_id}")
    public UserProfile match(@PathVariable String profile_id, @PathVariable String user_id){
        UserProfile profile = this.profileRepository.findById(profile_id).orElse(null);
        User user = this.userRepository.findById(user_id).orElse(null);

        if (user != null && profile != null) {
            profile.setUser(user);
            return this.profileRepository.save(profile);
        }else{
            return null;
        }
    }

    @PutMapping("{profile_id}/user")
    public UserProfile unmatch(@PathVariable String profile_id, @PathVariable String user_id){
        UserProfile profile = this.profileRepository.findById(profile_id).orElse(null);
        profile.setUser(null);

        return this.profileRepository.save(profile);
    }
}
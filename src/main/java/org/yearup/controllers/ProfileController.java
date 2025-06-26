package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_USER')")
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(UserDao userDao, ProfileDao profileDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @GetMapping("")
    public Profile getProfile(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the profileDao to get and return the profile
            return profileDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("")
    public Profile updateProfile(Principal principal, @RequestBody Profile profile){
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            Profile current=profileDao.getByUserId(userId);
            if(profile.getFirstName()==null||profile.getFirstName().isEmpty()) profile.setFirstName(current.getFirstName());
            if(profile.getLastName()==null||profile.getLastName().isEmpty()) profile.setLastName(current.getLastName());
            if(profile.getPhone()==null||profile.getPhone().isEmpty()) profile.setPhone(current.getPhone());
            if(profile.getEmail()==null||profile.getEmail().isEmpty()) profile.setEmail(current.getEmail());
            if(profile.getAddress()==null||profile.getAddress().isEmpty()) profile.setAddress(current.getAddress());
            if(profile.getCity()==null||profile.getCity().isEmpty()) profile.setCity(current.getCity());
            if(profile.getState()==null||profile.getState().isEmpty()) profile.setState(current.getState());
            if(profile.getZip()==null||profile.getZip().isEmpty()) profile.setZip(current.getZip());

            profileDao.update(userId,profile);

            // use the profileDao to get and return the profile
            return profileDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}

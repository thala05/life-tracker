package com.mind.trail.MindTrail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private userRepo userRepository;

    @Autowired
    private moodService moodService;

    @Autowired
    private userService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userEntity loggedInUser = userRepository.findByUsername(username);
        if(loggedInUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("User logged in: " + loggedInUser.getUsername());
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody userDTO user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if(!username.equals(user.getUsername())) {
            return new ResponseEntity<>("Access Denied: Cannot update other user's information", HttpStatus.FORBIDDEN);
        }
        return userService.updateUser(user);
    }

    @PostMapping("/mood")
    public ResponseEntity<?> recordMood(@Valid @RequestBody moodDTO mood) {
        return moodService.recordMood(mood);
    }

    @GetMapping("/show")
    public ResponseEntity<?> showMood() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserMoodResponseDTO response = userService.getUserMoodDetails(username);
        if(response == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }



}

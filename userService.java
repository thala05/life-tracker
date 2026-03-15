package com.mind.trail.MindTrail;

import org.springframework.http.ResponseEntity;

public interface userService {
    
    ResponseEntity<?> registerUser(userDTO user);
    ResponseEntity<?> registerAdmin(userDTO user);
    ResponseEntity<?> updateUser(userDTO user);
    UserMoodResponseDTO getUserMoodDetails(String username);
}

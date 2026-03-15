package com.mind.trail.MindTrail;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {
    
    @Autowired
    private userRepo userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private moodRepo moodRepository;

    @Override
    public ResponseEntity<?> registerUser(userDTO user) {
      
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        userEntity newUser = new userEntity();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setRoles(Arrays.asList("USER"));
        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }

    @Override
    public ResponseEntity<?> registerAdmin(userDTO user) {
        try{
            if(userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.badRequest().body("Username is already taken");
            }
            userEntity newUser = new userEntity();
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(encoder.encode(user.getPassword()));
            newUser.setRoles(Arrays.asList("ADMIN"));
            userRepository.save(newUser);
            return ResponseEntity.ok("Admin registered successfully");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Override
    public ResponseEntity<?> updateUser(userDTO user) {
        userEntity existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser == null) {
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
        existingUser.setEmail(user.getEmail());
        if(user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
        return ResponseEntity.ok("User updated successfully");
    }

    @Override
    @Cacheable(value = "userDetails",key = "#username",unless = "#result == null")
    public UserMoodResponseDTO getUserMoodDetails(String username) {
        userEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        Optional<moodEntity> moodOpt = moodRepository.findByUserId(user.getId());
        if (moodOpt.isEmpty()) {
            return null;
        }
        moodEntity mood = moodOpt.get();
        return new UserMoodResponseDTO(mood.getDate(),mood.getMood());
    }
}
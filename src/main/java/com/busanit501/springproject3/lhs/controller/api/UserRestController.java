package com.busanit501.springproject3.lhs.controller.api;



import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.entity.mongoEntity.ProfileImage;
import com.busanit501.springproject3.lhs.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Log4j2
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/page")
    public Page<User> getAllUsersWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsersWithPage(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<User> createUser( @RequestPart("user") User user,
                                            @RequestParam(value = "profileImage", required = false) MultipartFile file) {
        try {


            User createdUser = userService.createUser(user);

            if (file !=null && !file.isEmpty()) {
                userService.saveProfileImage(createdUser.getId(), file);
            }

            return ResponseEntity.ok(createdUser);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save user or profile image", e);
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestPart("user") User user,
            @RequestParam(value = "profileImage", required = false) MultipartFile file) {

        try {
            User updatedUser = userService.updateUser(id, user);

            if (file !=null && !file.isEmpty()) {
                Optional<User> loadUser = userService.getUserById(id);
                User loadedUser = loadUser.get();
                userService.deleteProfileImage(loadedUser);

                userService.saveProfileImage(id, file);
            }

            return ResponseEntity.ok(updatedUser);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save user or profile image", e);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: " + id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/uploadProfileImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.saveProfileImage(id, file);
            return ResponseEntity.ok("Profile image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload profile image");
        }
    }


    @GetMapping("/{id}/profileImage")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id) {
        log.info("lsy users image 확인 ");
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent() && user.get().getProfileImageId() != null) {
            ProfileImage profileImage = userService.getProfileImage(user.get().getProfileImageId());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(profileImage.getContentType()))
                    .body(profileImage.getData());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/deleteProfileImage")
    public String deleteProfileImage(@RequestParam Long id) {
        Optional<User> user = userService.getUserById(id);
        User user1 = user.get();
        if (user1 != null) {
            userService.deleteProfileImage(user1);
            return "Profile image deleted successfully";
        } else {
            return "User not found";
        }
    }

}

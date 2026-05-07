package bt.edu.gcit.repositoryservice.controller;

import bt.edu.gcit.repositoryservice.entity.User;
import bt.edu.gcit.repositoryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // Get specific user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    // Global counts (Colleges, Courses, Users)
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Long>> getGlobalCounts() {
        return ResponseEntity.ok(userService.getGlobalSystemCounts());
    }

    // Aggregate Percentage
    @GetMapping("/aggregate/{id}")
    public ResponseEntity<Map<String, Object>> getStudentAggregate(@PathVariable String id) {
        return ResponseEntity.ok(userService.calculateAggregate(id));
    }

    // update users
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(userService.updateUserDetails(id, updates));
    }

    // Delete any user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User removed successfully.");
    }
}
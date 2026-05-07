package bt.edu.gcit.repositoryservice.service;

import bt.edu.gcit.repositoryservice.dao.UserRepository;
import bt.edu.gcit.repositoryservice.entity.User;
import bt.edu.gcit.repositoryservice.dao.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() != User.Role.superadmin)
                .collect(Collectors.toList());
    }

    public User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User updateUserDetails(String id, Map<String, Object> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 1. General Profile Updates (Available to all roles)
        if (updates.containsKey("fullName"))
            user.setFullName((String) updates.get("fullName"));
        if (updates.containsKey("email"))
            user.setEmail((String) updates.get("email"));
        if (updates.containsKey("cid"))
            user.setCid((String) updates.get("cid"));

        // 2. Role-Specific: College Admin Fields
        if (user.getRole() == User.Role.admin) {
            if (updates.containsKey("collegeName"))
                user.setCollegeName((String) updates.get("collegeName"));
            if (updates.containsKey("websiteUrl"))
                user.setWebsiteUrl((String) updates.get("websiteUrl"));
            if (updates.containsKey("contactInfo"))
                user.setContactInfo((String) updates.get("contactInfo"));
        }

        // 3. Role-Specific: Student Academic Marks
        if (user.getRole() == User.Role.student && updates.containsKey("academicMarks")) {
            user.setAcademicMarks(updates.get("academicMarks"));
        }

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    /**
     * Logic adapted from superAdminController.js getCounts
     */
    public Map<String, Long> getGlobalSystemCounts() {
        Map<String, Long> counts = new HashMap<>();
        List<User> allUsers = userRepository.findAll();

        counts.put("users", (long) allUsers.size());
        counts.put("students", allUsers.stream().filter(u -> u.getRole() == User.Role.student).count());
        counts.put("colleges", allUsers.stream().filter(u -> u.getRole() == User.Role.admin).count());
        counts.put("courses", courseRepository.count());

        return counts;
    }

    /**
     * Logic adapted from superAdminController.js getAggregatePercentage
     */
    public Map<String, Object> calculateAggregate(String userId) {
        User student = findUserById(userId);

        if (student.getRole() != User.Role.student) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a student");
        }

        // academicMarks is stored as an Object/Map in User entity
        if (!(student.getAcademicMarks() instanceof Map)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid marks format");
        }

        Map<String, Object> marksMap = (Map<String, Object>) student.getAcademicMarks();
        double total = 0;
        int count = 0;

        for (Object val : marksMap.values()) {
            try {
                total += Double.parseDouble(val.toString());
                count++;
            } catch (Exception ignored) {
            }
        }

        double aggregate = count > 0 ? total / count : 0.0;

        Map<String, Object> response = new HashMap<>();
        response.put("fullName", student.getFullName());
        response.put("indexNumber", student.getIndexNumber());
        response.put("aggregatePercentage", Math.round(aggregate * 100.0) / 100.0);

        return response;
    }
}

package bt.edu.gcit.repositoryservice.service;

import bt.edu.gcit.repositoryservice.dao.UserRepository;
import bt.edu.gcit.repositoryservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollegeService {

    @Autowired
    private UserRepository userRepository;

    // In your Node code, College was a separate model.
    // Here, we filter the User collection for 'admin' roles.
    public List<User> getAllColleges() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.admin)
                .collect(Collectors.toList());
    }

    public User getCollegeById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "College not found"));

        if (user.getRole() != User.Role.admin) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a college admin");
        }
        return user;
    }

    public User updateCollege(String id, User updatedData) {
        User existingAdmin = getCollegeById(id);

        // Update fields specific to the college/admin profile
        if (updatedData.getCollegeName() != null)
            existingAdmin.setCollegeName(updatedData.getCollegeName());
        if (updatedData.getWebsiteUrl() != null)
            existingAdmin.setWebsiteUrl(updatedData.getWebsiteUrl());
        if (updatedData.getContactInfo() != null)
            existingAdmin.setContactInfo(updatedData.getContactInfo());
        if (updatedData.getFullName() != null)
            existingAdmin.setFullName(updatedData.getFullName());

        return userRepository.save(existingAdmin);
    }

    public void deleteCollege(String id) {
        User user = getCollegeById(id);
        userRepository.delete(user);
    }

    public Map<String, Long> getCollegeStats() {
        List<User> allUsers = userRepository.findAll();

        long totalColleges = allUsers.stream().filter(u -> u.getRole() == User.Role.admin).count();
        long activeColleges = allUsers.stream().filter(u -> u.getRole() == User.Role.admin && u.isVerified()).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalColleges", totalColleges);
        stats.put("activeColleges", activeColleges);
        stats.put("pendingColleges", totalColleges - activeColleges);

        return stats;
    }
}

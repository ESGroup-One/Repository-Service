package bt.edu.gcit.repositoryservice.controller;

import bt.edu.gcit.repositoryservice.entity.User;
import bt.edu.gcit.repositoryservice.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colleges")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    // get all colleges
    @GetMapping
    public ResponseEntity<List<User>> getAllColleges() {
        return ResponseEntity.ok(collegeService.getAllColleges());
    }

    // get college by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getCollegeById(@PathVariable String id) {
        return ResponseEntity.ok(collegeService.getCollegeById(id));
    }

    // College Stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getCollegeStats() {
        return ResponseEntity.ok(collegeService.getCollegeStats());
    }

    // Update College details
    @PutMapping("/{id}")
    public ResponseEntity<User> updateCollege(@PathVariable String id, @RequestBody User updatedData) {
        return ResponseEntity.ok(collegeService.updateCollege(id, updatedData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCollege(@PathVariable String id) {
        collegeService.deleteCollege(id);
        return ResponseEntity.ok("College admin deleted successfully.");
    }
}

package bt.edu.gcit.repositoryservice.controller;

import bt.edu.gcit.repositoryservice.entity.Course;
import bt.edu.gcit.repositoryservice.service.CourseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<Course> createCourse(
            @RequestHeader("Authorization") String token,
            @RequestBody Course course) {
        return ResponseEntity.ok(courseService.addCourse(token, course));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<Course>> getMyCourses(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(courseService.getCoursesByAdmin(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/my-count")
    public ResponseEntity<Long> getMyCourseCount(@RequestHeader("Authorization") String token) {
        long count = courseService.getCourseCountByAdmin(token);
        return ResponseEntity.ok(count);
    }
}
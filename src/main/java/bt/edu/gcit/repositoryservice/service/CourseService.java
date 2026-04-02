package bt.edu.gcit.repositoryservice.service;

import bt.edu.gcit.repositoryservice.dao.CourseRepository;
import bt.edu.gcit.repositoryservice.entity.Course;
import bt.edu.gcit.repositoryservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Course addCourse(String token, Course course) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String identifier = jwtUtil.extractIdentifier(token);

        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("indexNumber").is(identifier),
                Criteria.where("email").is(identifier)));

        Map<String, Object> adminUser = mongoTemplate.findOne(query, Map.class, "users");

        if (adminUser != null) {
            Course.CollegeInfo collegeInfo = new Course.CollegeInfo();
            // Use .get("_id").toString() if it's an ObjectId, or just .get("_id")
            collegeInfo.setId(adminUser.get("_id").toString());
            collegeInfo.setCollegeName((String) adminUser.get("collegeName"));
            collegeInfo.setWebsiteUrl((String) adminUser.get("websiteUrl"));
            collegeInfo.setContactInfo((String) adminUser.get("contactInfo"));
            collegeInfo.setFullName((String) adminUser.get("fullName"));
            collegeInfo.setEmail((String) adminUser.get("email"));

            course.setCollege(collegeInfo);
        }

        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByAdmin(String token) {
        // 1. Clean the token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String identifier = jwtUtil.extractIdentifier(token);

        Query query = new Query(new Criteria().orOperator(
                Criteria.where("indexNumber").is(identifier),
                Criteria.where("email").is(identifier)));

        Map<String, Object> adminUser = mongoTemplate.findOne(query, Map.class, "users");

        if (adminUser != null) {
            String adminId = adminUser.get("_id").toString();
            // 4. Query the courses collection where college.id matches this admin
            return courseRepository.findByCollegeId(adminId);
        }

        return Collections.emptyList();
    }
}
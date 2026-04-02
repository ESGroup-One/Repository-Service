package bt.edu.gcit.repositoryservice.dao;

import bt.edu.gcit.repositoryservice.entity.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findByCollegeId(String collegeId);
}
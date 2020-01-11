package com.udacity.course3.reviews.ReviewRepository;

import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entity.ReviewDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewDocRepository extends MongoRepository<ReviewDoc, String> {

    Optional<ReviewDoc> findByReviewid(int reviewid);

    List<ReviewDoc> findByProdid(int prodid);
}
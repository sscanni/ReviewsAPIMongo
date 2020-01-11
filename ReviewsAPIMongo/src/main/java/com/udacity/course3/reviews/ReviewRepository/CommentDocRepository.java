package com.udacity.course3.reviews.ReviewRepository;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.CommentDoc;
import com.udacity.course3.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentDocRepository extends MongoRepository<CommentDoc, String> {

//    Optional<CommentDoc> findByReviewid(int reviewid);

//    List<CommentDoc> findByProdid(int prodid);
}
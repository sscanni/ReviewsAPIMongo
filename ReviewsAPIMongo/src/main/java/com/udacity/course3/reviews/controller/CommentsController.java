package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.ReviewRepository.CommentsRepository;
import com.udacity.course3.reviews.ReviewRepository.ReviewDocRepository;
import com.udacity.course3.reviews.ReviewRepository.ReviewsRepository;
import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.CommentDoc;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entity.ReviewDoc;
import com.udacity.course3.reviews.service.ReviewsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private ReviewDocRepository reviewDocRepository;

    /********************************************************************************
    * Creates a comment for a review.
    /********************************************************************************
    * 1. Add argument for comment entity. Use {@link RequestBody} annotation.
    * 2. Check for existence of review.
    * 3. If review not found, return NOT_FOUND.
    * 4. If found, save comment.
    /********************************************************************************
    * @param reviewId The id of the review.
    * @param comments The comment request body
    /********************************************************************************/
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<?> createCommentForReview(@RequestBody Comment comments, @PathVariable("reviewId") Integer reviewId) {

        Review reviews = reviewsRepository.findById(reviewId)
                .orElseThrow(ReviewsNotFoundException::new);

        ReviewDoc reviewDocs = reviewDocRepository.findByReviewid(reviewId)
                .orElseThrow(ReviewsNotFoundException::new);

        Comment newComment = new Comment();
        newComment.setReviewid(reviews.getReviewid());
        newComment.setComment(comments.getComment());
        commentsRepository.save(newComment);

        /******************************************************************************
        * Save data to MongoDB document
        /*****************************************************************************/
        CommentDoc newCommentDoc = new CommentDoc();
        newCommentDoc.setId(newComment.getId());
        newCommentDoc.setComment(newComment.getComment());

        List <CommentDoc> comList = new  ArrayList(reviewDocs.getComments());
        comList.add(newCommentDoc);
        reviewDocs.setComments(comList);
        reviewDocRepository.save(reviewDocs);

        return new ResponseEntity<CommentDoc>(newCommentDoc, HttpStatus.OK);
    }

    /********************************************************************************
    * List comments for a review.
    /********************************************************************************
    * 2. Check for existence of review.
    * 3. If review not found, return NOT_FOUND.
    * 4. If found, return list of comments.
    /********************************************************************************
    * @param reviewId The id of the review.
    /********************************************************************************/
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<?> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {

//        Review reviews = reviewsRepository.findById(reviewId)
//                .orElseThrow(ReviewsNotFoundException::new);
//
//        List<Comment> list = reviews.getComments();
//
//        return new ResponseEntity<List<Comment>>(list, HttpStatus.OK);

        Optional <ReviewDoc> revDoc = reviewDocRepository.findByReviewid(reviewId);

        List<CommentDoc> list = revDoc.get().getComments();

       return new ResponseEntity<List<CommentDoc>>(list, HttpStatus.OK);
    }
}

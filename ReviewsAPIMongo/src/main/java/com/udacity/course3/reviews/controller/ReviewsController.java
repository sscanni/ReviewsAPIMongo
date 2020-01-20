package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.ReviewRepository.CommentsRepository;
import com.udacity.course3.reviews.ReviewRepository.ProductRepository;
import com.udacity.course3.reviews.ReviewRepository.ReviewDocRepository;
import com.udacity.course3.reviews.ReviewRepository.ReviewsRepository;
import com.udacity.course3.reviews.entity.*;
import com.udacity.course3.reviews.service.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    // TODO: Wire JPA repositories here

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private ReviewDocRepository reviewDocRepository;

    /******************************************************************************
    * Creates a review for a product.
    /******************************************************************************
    * 1. Add argument for review entity. Use {@link RequestBody} annotation.
    * 2. Check for existence of product.
    * 3. If product not found, return NOT_FOUND.
    * 4. If found, save review.
    /******************************************************************************
    * @param productId The id of the product.
    * @return The created review or 404 if product id is not found.
    /******************************************************************************/
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<?> createReviewForProduct(@RequestBody Comment comments,
                                            @PathVariable("productId") Integer productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        List<Review> curReviews = product.getReviews();

        Review reviews = new Review();
        reviews.setProdid(productId);
        reviews.setComments(new ArrayList());

        curReviews.add(reviews);
        product.setReviews(curReviews);

        reviews = reviewsRepository.save(reviews);

        Comment newComment = new Comment();
        newComment.setReviewid(reviews.getReviewid());
        newComment.setComment(comments.getComment());

        commentsRepository.save(newComment);

        List<Comment> comList = new ArrayList<>();
        comList.add(newComment);
        reviews.setComments(comList);

        /******************************************************************************
        * Save data to MongoDB document
        /*****************************************************************************/
        ReviewDoc newReviewDoc = new ReviewDoc();
        newReviewDoc.setReviewid(reviews.getReviewid());
        newReviewDoc.setProdid(productId);

        CommentDoc newCommentDoc = new CommentDoc();
        newCommentDoc.setId(newComment.getId());
        newCommentDoc.setComment(newComment.getComment());

        List <CommentDoc> comDocList = new  ArrayList();
        comDocList.add(newCommentDoc);
        newReviewDoc.setComments(comDocList);

        reviewDocRepository.save(newReviewDoc);

        return new ResponseEntity<>(newReviewDoc, HttpStatus.OK);
    }

    /*******************************************************************************
    * Lists reviews by product.
    /*******************************************************************************
    * @param productId The id of the product.
    * @return The list of reviews.
    /******************************************************************************/
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<?>> listReviewsForProduct(@PathVariable("productId") Integer productId) {

        productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        /******************************************************************************
        * Retrieve the reviews and comments from MongoDB
        /*****************************************************************************/
        List<ReviewDoc> revDocs = reviewDocRepository.findByProdid(productId);

        return new ResponseEntity<>(revDocs, HttpStatus.OK);
    }
}
package com.udacity.course3.reviews.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Reviews not found exception.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Review/Comment not found")
public class ReviewsNotFoundException extends RuntimeException {

}

package com.udacity.course3.reviews.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("reviews")
public class ReviewDoc {

    @Id
    private String id;

    private int reviewid;

    private int prodid;

    private List<CommentDoc> comments;

    public ReviewDoc() {
    }

    public ReviewDoc(int reviewid, int prodid, List<CommentDoc> comments) {
        this.reviewid = reviewid;
        this.prodid = prodid;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReviewid() {
        return reviewid;
    }

    public void setReviewid(int reviewid) {
        this.reviewid = reviewid;
    }

    public int getProdid() {
        return prodid;
    }

    public void setProdid(int prodid) {
        this.prodid = prodid;
    }

    public List<CommentDoc> getComments() {
        return comments;
    }

    public void setComments(List<CommentDoc> comments) {
        this.comments = comments;
    }
}


package com.udacity.course3.reviews.entity;

import java.util.List;

public class CommentDoc {

    private int id;

    private String comment;

    public CommentDoc () {

    }

    public CommentDoc(int id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}

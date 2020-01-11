package com.udacity.course3.reviews.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * The type Review.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewid")
    private Integer reviewid;

    @Column(name = "prodid")
    private Integer prodid;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reviewid", referencedColumnName = "reviewid")
    private List<Comment> comments;
}
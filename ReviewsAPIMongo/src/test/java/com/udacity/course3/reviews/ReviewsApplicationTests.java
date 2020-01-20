package com.udacity.course3.reviews;

import com.udacity.course3.reviews.ReviewRepository.CommentsRepository;
import com.udacity.course3.reviews.ReviewRepository.ProductRepository;
import com.udacity.course3.reviews.ReviewRepository.ReviewsRepository;
import com.udacity.course3.reviews.ReviewRepository.ReviewDocRepository;
import com.udacity.course3.reviews.entity.*;
import com.udacity.course3.reviews.service.ProductNotFoundException;
import com.udacity.course3.reviews.service.ReviewsNotFoundException;
import lombok.var;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@Transactional                  //Added so that the reviewsRepository is read eagerly.
public class ReviewsApplicationTests {

    /**
     * The Review comment 1.
     */
    String reviewComment1 = "This is a new review inserted from sql.";
    /**
     * The Review comment 2.
     */
    String reviewComment2 = "This is the second comment for review 1.";
    /**
     * The Review comment 3.
     */
    String reviewComment3 = "This is the first comment for product 1 review 1.";

    @Autowired private ReviewDocRepository reviewDocrepository;

    @Autowired private ProductRepository productRepository;

    @Autowired private ReviewsRepository reviewsRepository;

    @Autowired private CommentsRepository commentsRepository;

    private static final int NUMBER_OF_REVIEWS = 1;

    @Before
    public void init() {

        reviewDocrepository.deleteAll();

        reviewsRepository.findAll().forEach((review) -> {
            System.err.println(review.getReviewid() + " | " + review.getProdid());
            List<Comment> comList = review.getComments();
            List<CommentDoc> comDocList = new ArrayList<>();
            for (Comment comment : comList) {
                System.err.println(comment.getId() + " | " + comment.getReviewid() + " | " + comment.getComment());
                comDocList.add(new CommentDoc(comment.getId(), comment.getComment()));
            }
            reviewDocrepository.save(new ReviewDoc(review.getReviewid(), review.getProdid(), comDocList));
        });
    }
    /**
     * Test to ensure that the injected components are not null.
     */
    @Test
    public void injectedComponentsAreNotNull(){
        assertThat(productRepository).isNotNull();
        assertThat(reviewsRepository).isNotNull();
        assertThat(commentsRepository).isNotNull();
        assertThat(reviewDocrepository).isNotNull();
    }
    @Test
    public void testProduct(){
        //Expect 5 products
        List<Product> prodList = productRepository.findAll();
        assertThat(prodList.size()).isEqualTo(5);

        //add a new product
        Product addProd = new Product();
        addProd.setName("New Product");
        productRepository.save(addProd);

        //we should now have 6 products
        assertThat(productRepository.findAll().size()).isEqualTo(6);

        //verify that we have expected products. Product 1 is the only one with a review.
        Product testProd = new Product();
        testProd = productRepository.findById(1).orElseThrow(ProductNotFoundException::new);
        assertThat(testProd.getName()).isEqualTo("Dell XPS Desktop Computer");
        assertThat(testProd.getReviews()).isNotEmpty();
        testProd = productRepository.findById(2).orElseThrow(ProductNotFoundException::new);
        assertThat(testProd.getName()).isEqualTo("Dell XPS 13 Laptop Computer");
        assertThat(testProd.getReviews()).isEmpty();
        testProd = productRepository.findById(3).orElseThrow(ProductNotFoundException::new);
        assertThat(testProd.getName()).isEqualTo("Lenovo Laptop Computer");
        assertThat(testProd.getReviews()).isEmpty();
        testProd = productRepository.findById(4).orElseThrow(ProductNotFoundException::new);
        assertThat(testProd.getName()).isEqualTo("Dell 27 inch Monitor");
        assertThat(testProd.getReviews()).isEmpty();
        testProd = productRepository.findById(5).orElseThrow(ProductNotFoundException::new);
        assertThat(testProd.getName()).isEqualTo("HP Laptop");
        assertThat(testProd.getReviews()).isEmpty();
    }
    @Test
    public void testReviews() {

        Product product = productRepository.findById(2)
                .orElseThrow(ProductNotFoundException::new);

        List<Review> curReviews = product.getReviews();
        Review reviews = new Review();
        reviews.setProdid(2);
        curReviews.add(reviews);
        product.setReviews(curReviews);

        reviews = reviewsRepository.save(reviews);

        Comment comments = new Comment();
        comments.setReviewid(reviews.getReviewid());
        comments.setComment(reviewComment3);
        List<Comment> comList = new ArrayList<>();
        comList.add(comments);
        reviews.setComments(comList);

        commentsRepository.save(comments);

        Product updatedProduct = productRepository.findById(1)
                .orElseThrow(ProductNotFoundException::new);

        //Expect 2 reviews for Product #1
        assertThat(updatedProduct.getReviews().size()).isEqualTo(1);
        assertThat(updatedProduct.getReviews().get(0).getComments().get(0).getComment()).isEqualTo(reviewComment1);
        assertThat(updatedProduct.getReviews().get(0).getComments().get(1).getComment()).isEqualTo(reviewComment2);

        Product updatedProduct2 = productRepository.findById(2)
                .orElseThrow(ProductNotFoundException::new);

        //Expect 1 review for Product #2
        assertThat(updatedProduct2.getReviews().size()).isEqualTo(1);
        assertThat(updatedProduct2.getReviews().get(0).getComments().get(0).getComment()).isEqualTo(reviewComment3);
    }
    /**
     * testComments method
     * 1) Test adding a new comment to an existing review.
     */
    @Test
    public void testComments() {
        Review reviews = reviewsRepository.findById(1)
                .orElseThrow(ReviewsNotFoundException::new);

        List<Comment> list = reviews.getComments();

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getComment()).isEqualTo(reviewComment1);
    }
    @Test
    public void countAllReviews() {

        var reviews = reviewDocrepository.findAll();
        assertEquals(NUMBER_OF_REVIEWS, reviews.size());
    }

    @Test
    public void countOneReview() {

        List<CommentDoc> comDocList = new ArrayList<>();
        comDocList.add(new CommentDoc(1, reviewComment1));
        comDocList.add(new CommentDoc(2, reviewComment2));

        Example<ReviewDoc> example = Example.of(new ReviewDoc(1, 1, comDocList));

        assertThat(reviewDocrepository.count(example)).isEqualTo(1L);
    }

    @Test
    public void setsIdOnSave() {

        List<CommentDoc> comDocList = new ArrayList<>();
        comDocList.add(new CommentDoc(1, reviewComment1));

        ReviewDoc reviewDoc = reviewDocrepository.save(new ReviewDoc(2, 1, comDocList));

        assertThat(reviewDoc.getId()).isNotNull();
    }

    @Test
    public void findOneReview() {

        List<CommentDoc> comDocList = new ArrayList<>();
        comDocList.add(new CommentDoc(1, reviewComment1));
        comDocList.add(new CommentDoc(2, reviewComment2));

        Example<ReviewDoc> example = Example.of(new ReviewDoc(1, 1, comDocList));

        Optional<ReviewDoc> review = reviewDocrepository.findOne(example);
        assertThat(review.get().getComments().get(0).getComment()).isEqualTo(reviewComment1);
    }
}
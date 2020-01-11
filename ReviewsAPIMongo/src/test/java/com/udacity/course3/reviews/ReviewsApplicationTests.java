package com.udacity.course3.reviews;

import com.udacity.course3.reviews.ReviewRepository.CommentsRepository;
import com.udacity.course3.reviews.ReviewRepository.ProductRepository;
import com.udacity.course3.reviews.ReviewRepository.ReviewsRepository;
import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.service.ProductNotFoundException;
import com.udacity.course3.reviews.service.ReviewsNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ReviewsApplicationTests
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
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

	@Autowired private ProductRepository productRepository;
	@Autowired private ReviewsRepository reviewsRepository;
	@Autowired private CommentsRepository commentsRepository;

	/**
	 * Test to ensure that the injected components are not null.
	 */
	@Test
	public void injectedComponentsAreNotNull(){
		assertThat(productRepository).isNotNull();
		assertThat(reviewsRepository).isNotNull();
		assertThat(commentsRepository).isNotNull();
	}

	/**
	 * testProduct method
	 * 1) Test reading all products.
	 * 2) Test adding a new product to the product database table. Ensure new product added correctly.
	 * 3) Test that all of the expected products are returned.
	 */
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

	/**
	 * testReviews method
	 * 1) Test adding a new review and comment.
	 */
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

}
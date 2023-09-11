package com.kh.youtube.service;

import com.kh.youtube.domain.Review;
import com.kh.youtube.repo.ReviewDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    public List<Review> showAll() {
        return reviewDAO.findAll();
    }

    public Review show(int id) {
        return reviewDAO.findById(id).orElse(null);
    }

    public Review create(Review review) {
        return reviewDAO.save(review);
    }

    public Review update(Review review) {
        Review target = reviewDAO.findById(review.getReviewCode()).orElse(null);
        if(target != null) {
            return reviewDAO.save(review);
        }
        return null;
    }

    public Review delete(int id) {
        Review target = reviewDAO.findById(id).orElse(null);
        reviewDAO.delete(target);
        return target;
    }
}
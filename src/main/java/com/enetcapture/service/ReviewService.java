package com.enetcapture.service;

import com.enetcapture.model.Review;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewService {
    private static ReviewService instance;
    private List<Review> reviews;
    private final String dataFilePath;
    private int nextId;

    private ReviewService() {
        reviews = new ArrayList<>();
        dataFilePath = new File(getClass().getClassLoader().getResource("WEB-INF/reviews.txt") != null ?
                getClass().getClassLoader().getResource("WEB-INF/reviews.txt").getFile() :
                "webapps/enetcapture/WEB-INF/reviews.txt").getAbsolutePath();
        initializeFile();
        loadReviews();
        nextId = reviews.stream().mapToInt(Review::getId).max().orElse(0) + 1;
    }

    public static synchronized ReviewService getInstance() {
        if (instance == null) {
            instance = new ReviewService();
        }
        return instance;
    }

    private void initializeFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("ReviewService: Created reviews.txt at " + dataFilePath);
            } catch (IOException e) {
                System.err.println("ReviewService: Failed to create reviews.txt: " + e.getMessage());
            }
        }
    }

    private void loadReviews() {
        reviews.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    reviews.add(new Review(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            Double.parseDouble(parts[3]),
                            parts[4],
                            Integer.parseInt(parts[5])
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("ReviewService: Error loading reviews.txt: " + e.getMessage());
        }
    }

    private void saveReviews() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            for (Review review : reviews) {
                String reviewData = String.format("%d|%s|%s|%.1f|%s|%d|%s%n",
                        review.getId(), review.getUsername(), review.getPhotographer(), review.getRating(),
                        review.getComment(), review.getEventId(), review.getDate());
                writer.write(reviewData);
            }
            writer.flush();
            System.out.println("ReviewService: Successfully saved reviews to " + dataFilePath);
        } catch (IOException e) {
            System.err.println("ReviewService: Error saving reviews.txt: " + e.getMessage());
        }
    }

    public synchronized boolean addReview(Review review) {
        loadReviews();
        if (review.getUsername() == null || review.getPhotographer() == null || review.getRating() < 0 || review.getRating() > 5) {
            return false;
        }
        review.setId(nextId++);
        reviews.add(review);
        saveReviews();
        updatePhotographerRating(review.getPhotographer());
        return true;
    }

    public synchronized boolean updateReview(Review review) {
        loadReviews();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getId() == review.getId()) {
                reviews.set(i, review);
                saveReviews();
                updatePhotographerRating(review.getPhotographer());
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteReview(int id) {
        loadReviews();
        Review review = getReviewById(id);
        if (review != null) {
            boolean removed = reviews.removeIf(r -> r.getId() == id);
            if (removed) {
                saveReviews();
                updatePhotographerRating(review.getPhotographer());
            }
            return removed;
        }
        return false;
    }

    public Review getReviewById(int id) {
        loadReviews();
        return reviews.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    public List<Review> getReviewsByPhotographer(String photographer) {
        loadReviews();
        return reviews.stream().filter(r -> r.getPhotographer().equalsIgnoreCase(photographer)).toList();
    }

    public List<Review> getAllReviews() {
        loadReviews();
        return new ArrayList<>(reviews);
    }

    private void updatePhotographerRating(String photographer) {
        List<Review> photographerReviews = getReviewsByPhotographer(photographer);
        if (!photographerReviews.isEmpty()) {
            double averageRating = photographerReviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            PhotographerService.getInstance().updatePhotographer(photographer, averageRating);
        }
    }
}
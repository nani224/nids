package com.nids.repository;

import com.nids.model.AlertReview;
import com.nids.model.AlertReviewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<AlertReview, AlertReviewId> {}

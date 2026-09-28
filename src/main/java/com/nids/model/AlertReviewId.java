package com.nids.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class AlertReviewId implements Serializable {
    private Integer reviewId;
    private Integer alert;
}

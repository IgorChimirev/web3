package com.igor.jsfgraph.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a result entity for storing point data.
 * This is a plain POJO without ORM mappings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultEntity {
    private long id;

    private double x;
    private double y;
    private double r;
    private boolean result;
}

package com.malle.microone.product.dto;

import java.math.BigDecimal;

public record ProductRequest(String id, String name, String description, BigDecimal price) {}

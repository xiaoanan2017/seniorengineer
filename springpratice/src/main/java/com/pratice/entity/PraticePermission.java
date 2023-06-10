package com.pratice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PraticePermission {
    private Long id;

    private String url;

    private String name;

    private String description;

    private Long pid;
}
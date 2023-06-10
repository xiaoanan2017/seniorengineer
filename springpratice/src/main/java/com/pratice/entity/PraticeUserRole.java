package com.pratice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PraticeUserRole {
    private Long userId;

    private Long roleId;
}
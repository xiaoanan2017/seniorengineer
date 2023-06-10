package com.pratice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PraticeRolePermission {
    private Long roleId;

    private Long permissionId;
}
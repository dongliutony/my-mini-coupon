package com.dongliu.coupon.vo;

import lombok.Data;

/**
 * <h1>Interface permission info. assembler</h1>
 * When permission detector scan interfaces and find targets, it generates PermissionInfo instance accordingly.
 * Then passes it into createPath() to create path info.
 */
@Data
public class PermissionInfo {
    private String url;
    private String method;
    private Boolean isReadOnly;
    private String description;
    private String extra;

    @Override
    public String toString() {

        return "url = " + url
                + ", method = " + method
                + ", isRead = " + isReadOnly
                + ", description = " + description;
    }
}

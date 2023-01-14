package com.dongliu.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    /**
     * A PartArgsConstructor to provide flexibility when we don't need to include {@link T data} in the response.
     * @param code
     * @param message
     */
    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}

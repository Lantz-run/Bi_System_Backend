package com.yupi.springbootinit.model.vo;

/**
 * <p>Project: yubi-backend
 * <p>Powered by Lantz On 2025/3/29
 *
 * @author Lantz
 * @version 1.0
 * @Description BiResponse
 * @since 1.8
 */

import lombok.Data;

/**
 * Bi 的返回结果
 */
@Data
public class BiResponse {

    private String genChart;

    private String genResult;

    private Long chartId;
}

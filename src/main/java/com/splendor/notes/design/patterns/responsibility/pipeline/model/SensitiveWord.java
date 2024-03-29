package com.splendor.notes.design.patterns.responsibility.pipeline.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author splendor.s
 * @create 2022/11/28 下午2:22
 * @description 命中敏感词信息
 */
@Builder
@Data
public class SensitiveWord {
    /**
     * 敏感词
     */
    private String sensitive;
    /***********************命中敏感词信息处理************************/
    /**
     * 敏感词编号
     */
    private Long sensitiveId;
    /**
     * 敏感词归类：词库编号、正则编号等
     */
    private Integer kind;
}


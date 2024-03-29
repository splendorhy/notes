package com.splendor.notes.design.patterns.responsibility.pipeline.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author splendor.s
 * @create 2023/4/18 下午6:41
 * @description
 */
@Builder
@Data
public class SensitveHitContext extends PipelineContext {
    /**
     * 是否存在敏感词命中
     */
    private Boolean hasHit;
    /**
     * 用户输入文本原稿
     */
    private String content;
    /**
     * 用户清洗后的文本内容（清洗内容后的结果）
     */
    private String cleanContent;
    /**
     * 用户文本属性
     */
    private ContentAttr contentAttr;
    /**
     * 命中的敏感词
     */
    private List<SensitiveWord> hitWords;

    @Override
    public String getName() {
        return "模型实例(敏感词命中)构建上下文";
    }
}

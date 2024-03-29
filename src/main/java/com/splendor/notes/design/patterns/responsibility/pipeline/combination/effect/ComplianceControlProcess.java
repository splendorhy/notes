package com.splendor.notes.design.patterns.responsibility.pipeline.combination.effect;

import com.google.common.collect.Lists;
import com.splendor.notes.design.patterns.responsibility.pipeline.ContextHandler;
import com.splendor.notes.design.patterns.responsibility.pipeline.enums.SensitiveEffect;
import com.splendor.notes.design.patterns.responsibility.pipeline.model.SensitiveWord;
import com.splendor.notes.design.patterns.responsibility.pipeline.model.SensitveEffectiveContext;
import com.splendor.notes.design.patterns.responsibility.pipeline.constant.*;
import com.splendor.notes.design.patterns.responsibility.pipeline.model.SensitveHitContext;
import com.splendor.notes.spring.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author splendor.s
 * @create 2023/4/18 下午5:30
 * @description
 */

@Component
@SensitiveEffect(effectCode = SensitiveCons.Effect.COMPLIANCE_CONTROL)
public class ComplianceControlProcess implements ContextHandler<SensitveHitContext, SensitveEffectiveContext> {

    /**
     * 实际合规管控处理是否可以放行
     *
     * @param context 处理时的上下文数据
     * @return 最终处理结果
     */
    @Override
    public SensitveEffectiveContext handle(SensitveHitContext context) {
        List<SensitiveWord> hitWords = context.getHitWords();
        /*如果未命中任何敏感词则直接返回*/
        if (CollectionUtils.isEmpty(hitWords)) {
            return SensitveEffectiveContext.builder()
                    /*没有任何词生效*/
                    .isHit(false)
                    .content(context.getContent())
                    .cleanContent(context.getCleanContent())
                    .hitWords(Lists.newArrayList()).build();
        }

        /*此处只为模拟,根据当前命中的敏感词信息查询是否存在对应的合规管控处理策略,如果存在对放行的敏感词进行标志*/
        List<SensitiveWord> complianceControlSensitiveWord = getComplianceControlSensitiveWord(context.getHitWords());
        if (CollectionUtils.isEmpty(complianceControlSensitiveWord)) {
            /*没有需要放行的词，则当前词就是命中的，可直接返回*/
            return SensitveEffectiveContext.builder()
                    /*已经有生效的词直接返回*/
                    .isHit(true)
                    .content(context.getContent())
                    .cleanContent(context.getCleanContent())
                    .hitWords(hitWords)
                    .complianceIgnoreWords(Lists.newArrayList()).build();
        }

        /*整合合规管控中放行的词*/
        List<Long> complianceIgnoreWordIds = complianceControlSensitiveWord.stream().map(SensitiveWord::getSensitiveId).collect(Collectors.toList());
        /*去除放行词后命中的词*/
        List<SensitiveWord> finalHitWords = hitWords.stream().filter(sensitiveWord -> !complianceIgnoreWordIds.contains(sensitiveWord.getSensitiveId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(finalHitWords)) {
            return SensitveEffectiveContext.builder()
                    /*已经有生效的词直接返回*/
                    .isHit(false)
                    .content(context.getContent())
                    .cleanContent(context.getCleanContent())
                    .hitWords(Lists.newArrayList())
                    .complianceIgnoreWords(complianceControlSensitiveWord).build();
        }

        /*此时链路可能还会记继续，保持对应内容的传递*/
        context.setHitWords(finalHitWords);
        /*返回当前命中的内容*/
        return SensitveEffectiveContext.builder()
                /*已经有生效的词直接返回*/
                .isHit(true)
                .content(context.getContent())
                .cleanContent(context.getCleanContent())
                .hitWords(finalHitWords)
                .complianceIgnoreWords(complianceControlSensitiveWord).build();
    }

    private List<SensitiveWord> getComplianceControlSensitiveWord(List<SensitiveWord> hitWords) {
        if (CollectionUtils.isEmpty(hitWords)) {
            return Lists.newArrayList();
        }
        /*合规处某些词管控中有新的策略，如果是改词的话直接放行*/
        List<SensitiveWord> complianceControlSensitiveWord = Arrays.asList(SensitiveWord.builder().sensitive("外卖").build(),
                SensitiveWord.builder().sensitive("闪购").build());
        List<String> complianceControlSensitiveWordInfo = complianceControlSensitiveWord.stream().map(SensitiveWord::getSensitive).collect(Collectors.toList());
        return hitWords.stream().filter(sensitiveWord -> complianceControlSensitiveWordInfo.contains(sensitiveWord.getSensitive())).collect(Collectors.toList());
    }
}

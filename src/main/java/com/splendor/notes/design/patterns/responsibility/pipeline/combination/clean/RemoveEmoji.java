package com.splendor.notes.design.patterns.responsibility.pipeline.combination.clean;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.splendor.notes.design.patterns.responsibility.pipeline.ContextHandler;
import com.splendor.notes.design.patterns.responsibility.pipeline.base.BCConvert;
import com.splendor.notes.design.patterns.responsibility.pipeline.enums.SensitiveClean;
import com.splendor.notes.design.patterns.responsibility.pipeline.constant.*;
import com.splendor.notes.design.patterns.responsibility.pipeline.model.ContentCleanResContext;
import com.splendor.notes.design.patterns.responsibility.pipeline.model.ContentInfoContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author splendor.s
 * @create 2022/11/28 下午2:35
 * @description 数据清洗：去除相关emoji信息
 */
@Component
@SensitiveClean(cleanCode = SensitiveCons.Clean.REMOVE_EMOJI)
public class RemoveEmoji implements ContextHandler<ContentInfoContext, ContentCleanResContext> {

    private LoadingCache<String, Set<Integer>> emojiCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(10, TimeUnit.MINUTES)
            /*构建缓存*/
            .build(new CacheLoader<String, Set<Integer>>() {
                /*初始化加载数据的缓存信息*/
                @Override
                public Set<Integer> load(String specialSymbols) throws Exception {
                    return getEmojis();
                }
            });

    /**
     * 对用户内容进行处理：去除相关emoji信息
     *
     * @param context 处理时的上下文数据
     * @return 处理结果（代进入敏感词词库校验）
     */
    @Override
    public ContentCleanResContext handle(ContentInfoContext context) {
        try {
            String EMOJI = "emoji";
            Set<Integer> emojis = emojiCache.get(EMOJI);
            StringBuilder cleanContent = new StringBuilder();
            /*其他链路中清洗后的词*/
            char[] valueChars = context.getCleanContent().toCharArray();
            for (char valueChar : valueChars) {
                int temp = BCConvert.charConvert(valueChar);
                if (emojis.contains(temp)) {
                    /*过滤特殊字符*/
                    continue;
                }
                cleanContent.append(valueChar);
            }

            /*将本次清洗数据载入待继续清洗实体中*/
            context.setCleanContent(cleanContent.toString());
            /*设置处理结果*/
            return ContentCleanResContext.builder()
                    .isCleanDone(true)
                    .content(context.getContent())
                    .cleanContent(cleanContent.toString())
                    .contentAttr(context.getContentAttr())
                    .build();
        } catch (Exception e) {
            /*设置处理结果*/
            return ContentCleanResContext.builder()
                    .isCleanDone(false)
                    .content(context.getContent())
                    /*记录下中间态数据*/
                    .cleanContent(context.getCleanContent())
                    .contentAttr(context.getContentAttr())
                    .reason("数据清洗异常：去除相关emoji信息失败")
                    .build();
        }
    }

    /**
     * 本处举例，实际应该放置到指定的配置页面来实时生效
     *
     * @return 相关Emoji集合
     */
    private static Set<Integer> getEmojis() {
        List<String> emojiRes = Lists.newArrayList();
        String emojis = "\uD83D\uDC36,\uD83D\uDC15,\uD83D\uDC59,\uD83D\uDEAD,\uD83D\uDEAC,\uD83D\uDC8A,\uD83C\uDF47,\uD83C\uDF48," +
                "\uD83C\uDF49,\uD83C\uDF4A,\uD83C\uDF4B,\uD83C\uDF4C,\uD83C\uDF4D,\uD83C\uDF4E,\uD83C\uDF4F,\uD83C\uDF50," +
                "\uD83C\uDF51,\uD83C\uDF52,\uD83C\uDF53,\uD83E\uDD5D,\uD83C\uDF45,\uD83E\uDD65,\uD83E\uDD51,\uD83C\uDF46," +
                "\uD83E\uDD54,\uD83E\uDD55,\uD83C\uDF3D,\uD83C\uDF36️,\uD83E\uDD52,\uD83E\uDD66,\uD83C\uDF44,\uD83E\uDD5C," +
                "\uD83C\uDF30,\uD83C\uDF5E,\uD83E\uDD50,\uD83E\uDD56,\uD83E\uDD68,\uD83E\uDD5E,\uD83E\uDDC0,\uD83C\uDF56," +
                "\uD83C\uDF57,\uD83E\uDD69,\uD83E\uDD53,\uD83C\uDF54,\uD83C\uDF5F,\uD83C\uDF55,\uD83C\uDF2D,\uD83E\uDD6A," +
                "\uD83C\uDF2E,\uD83C\uDF2F,\uD83E\uDD59,\uD83E\uDD5A,\uD83C\uDF73,\uD83E\uDD58,\uD83C\uDF72,\uD83E\uDD63," +
                "\uD83E\uDD57,\uD83C\uDF7F,\uD83E\uDD6B,\uD83C\uDF71,\uD83C\uDF58,\uD83C\uDF59,\uD83C\uDF5A,\uD83C\uDF5B," +
                "\uD83C\uDF5C,\uD83C\uDF5D,\uD83C\uDF60,\uD83C\uDF62,\uD83C\uDF63,\uD83C\uDF64,\uD83C\uDF65,\uD83C\uDF61," +
                "\uD83E\uDD5F,\uD83E\uDD60,\uD83E\uDD61,\uD83C\uDF66,\uD83C\uDF67,\uD83C\uDF68,\uD83C\uDF69,\uD83C\uDF6A," +
                "\uD83C\uDF82,\uD83C\uDF70,\uD83E\uDDC1,\uD83E\uDD67,\uD83C\uDF6B,\uD83C\uDF6C,\uD83C\uDF6D,\uD83C\uDF6E," +
                "\uD83C\uDF6F,\uD83C\uDF7C,\uD83E\uDD5B,☕,\uD83E\uDED6,\uD83C\uDF75,\uD83C\uDF76,\uD83C\uDF7E,\uD83C\uDF77," +
                "\uD83C\uDF78,\uD83C\uDF79,\uD83C\uDF7A,\uD83C\uDF7B,\uD83E\uDD42,\uD83E\uDD43,\uD83E\uDD64,\uD83E\uDD62," +
                "\uD83C\uDF7D️,\uD83C\uDF74,\uD83E\uDD44,\uD83D\uDEA3,\uD83D\uDDFE,\uD83C\uDFD4️,⛰️,\uD83C\uDF0B,\uD83D\uDDFB," +
                "\uD83C\uDFD5️,\uD83C\uDFD6️,\uD83C\uDFDC️,\uD83C\uDFDD️,\uD83C\uDFDE️,\uD83C\uDFDF️,\uD83C\uDFDB️,\uD83C\uDFD7️,⛩️," +
                "\uD83D\uDD4B,⛲,⛺,\uD83C\uDF01,\uD83C\uDF03,\uD83C\uDFD9️,\uD83C\uDF04,\uD83C\uDF05,\uD83C\uDF06,\uD83C\uDF07," +
                "\uD83C\uDF09,\uD83C\uDFA0,\uD83C\uDFA1,\uD83C\uDFA2,\uD83D\uDE82,\uD83D\uDE83,\uD83D\uDE84,\uD83D\uDE85," +
                "\uD83D\uDE86,\uD83D\uDE87,\uD83D\uDE88,\uD83D\uDE89,\uD83D\uDE8A,\uD83D\uDE9D,\uD83D\uDE9E,\uD83D\uDE8B," +
                "\uD83D\uDE8C,\uD83D\uDE8D,\uD83D\uDE8E,\uD83D\uDE90,\uD83D\uDE91,\uD83D\uDE92,\uD83D\uDE93,\uD83D\uDE94," +
                "\uD83D\uDE95,\uD83D\uDE96,\uD83D\uDE97,\uD83D\uDE98,\uD83D\uDE99,\uD83D\uDE9A,\uD83D\uDE9B,\uD83D\uDE9C," +
                "\uD83C\uDFCE️,\uD83C\uDFCD️,\uD83D\uDEF5,\uD83D\uDEB2,\uD83D\uDEF4,\uD83D\uDE8F,\uD83D\uDEE3️,\uD83D\uDEE4️,⛽," +
                "\uD83D\uDEA8,\uD83D\uDEA5,\uD83D\uDEA6,\uD83D\uDEA7,⚓,⛵,\uD83D\uDEA4,\uD83D\uDEF3️,⛴️,\uD83D\uDEE5️," +
                "\uD83D\uDEA2,✈️,\uD83D\uDEE9️,\uD83D\uDEEB,\uD83D\uDEEC,⛱️,\uD83C\uDF86,\uD83C\uDF87,\uD83C\uDF91,\uD83D\uDCB4," +
                "\uD83D\uDCB5,\uD83D\uDCB6,\uD83D\uDCB7,\uD83D\uDDFF,\uD83D\uDEC2,\uD83D\uDEC3,\uD83D\uDEC4,\uD83D\uDEC5," +
                "\uD83D\uDE48,\uD83D\uDE49,\uD83D\uDE4A,\uD83D\uDCA5,\uD83D\uDCAB,\uD83D\uDCA6,\uD83D\uDCA8,\uD83D\uDC35," +
                "\uD83D\uDC12,\uD83E\uDD8D,\uD83D\uDC36,\uD83D\uDC15,\uD83D\uDC29,\uD83D\uDC3A,\uD83E\uDD8A,\uD83D\uDC31," +
                "\uD83D\uDC08,\uD83D\uDC08\u200D⬛,\uD83E\uDD81,\uD83D\uDC2F,\uD83D\uDC05,\uD83D\uDC06,\uD83D\uDC34,\uD83D\uDC0E," +
                "\uD83E\uDD84,\uD83E\uDD93,\uD83E\uDD8C,\uD83E\uDDAC,\uD83D\uDC2E,\uD83D\uDC02,\uD83D\uDC03,\uD83D\uDC04," +
                "\uD83D\uDC37,\uD83D\uDC16,\uD83D\uDC17,\uD83D\uDC3D,\uD83D\uDC0F,\uD83D\uDC11,\uD83D\uDC10,\uD83D\uDC2A," +
                "\uD83D\uDC2B,\uD83E\uDD99,\uD83E\uDD92,\uD83D\uDC18,\uD83E\uDD8F,\uD83D\uDC2D,\uD83D\uDC01,\uD83D\uDC00," +
                "\uD83D\uDC39,\uD83D\uDC30,\uD83D\uDC07,\uD83D\uDC3F️,\uD83E\uDDAB,\uD83E\uDD94,\uD83E\uDD87,\uD83D\uDC3B," +
                "\uD83D\uDC3B\u200D❄️,\uD83D\uDC28,\uD83D\uDC3C,\uD83D\uDC3E,\uD83E\uDD83,\uD83D\uDC14,\uD83D\uDC13," +
                "\uD83D\uDC23,\uD83D\uDC24,\uD83D\uDC25,\uD83D\uDC26,\uD83D\uDC27,\uD83D\uDD4A️,\uD83E\uDD85,\uD83E\uDD86," +
                "\uD83E\uDD89,\uD83D\uDC38,\uD83D\uDC0A,\uD83D\uDC22,\uD83E\uDD8E,\uD83D\uDC0D,\uD83D\uDC32,\uD83D\uDC09," +
                "\uD83E\uDD95,\uD83E\uDD96,\uD83D\uDC33,\uD83D\uDC0B,\uD83D\uDC2C,\uD83D\uDC1F,\uD83D\uDC20,\uD83D\uDC21," +
                "\uD83E\uDD88,\uD83D\uDC19,\uD83D\uDC1A,\uD83D\uDC0C,\uD83E\uDD8B,\uD83D\uDC1B,\uD83D\uDC1C,\uD83D\uDC1D,☘️," +
                "\uD83C\uDF40,\uD83C\uDF41,\uD83C\uDF42,\uD83C\uDF43,\uD83C\uDF44,\uD83C\uDF30,\uD83E\uDD80,\uD83E\uDD9E," +
                "\uD83E\uDD90,\uD83E\uDD91,\uD83C\uDF0D,\uD83C\uDF0E,\uD83C\uDF0F,\uD83C\uDF10,\uD83C\uDF11,\uD83C\uDF12," +
                "\uD83C\uDF13,\uD83C\uDF14,\uD83C\uDF15,\uD83C\uDF16,\uD83C\uDF17,\uD83C\uDF18,\uD83C\uDF19,\uD83C\uDF1A," +
                "\uD83C\uDF1B,\uD83C\uDF1C,☀️,\uD83C\uDF1D,\uD83C\uDF1E,⭐,\uD83C\uDF1F,\uD83C\uDF20,☁️,⛅,⛈️,\uD83C\uDF24️," +
                "\uD83C\uDF25️,\uD83C\uDF26️,\uD83C\uDF27️,\uD83C\uDF28️,\uD83C\uDF29️,\uD83C\uDF2A️,\uD83C\uDF2B️,\uD83C\uDF2C️," +
                "\uD83C\uDF08,☂️,☔,⚡,❄️,☃️,⛄,☄️,\uD83D\uDD25,\uD83D\uDCA7,\uD83C\uDF0A,\uD83C\uDF84,✨,\uD83C\uDF8B," +
                "\uD83C\uDF8D,\uD83D\uDE00,\uD83D\uDE03,\uD83D\uDE04,\uD83D\uDE01,\uD83D\uDE06,\uD83D\uDE05,\uD83E\uDD23," +
                "\uD83D\uDE02,\uD83D\uDE42,\uD83D\uDE43,\uD83D\uDE09,\uD83D\uDE0A,\uD83D\uDE07,\uD83E\uDD70,\uD83D\uDE0D," +
                "\uD83E\uDD29,\uD83D\uDE18,\uD83D\uDE17,☺️,\uD83D\uDE1A,\uD83D\uDE19,\uD83E\uDD72,\uD83D\uDE0B,\uD83D\uDE1B," +
                "\uD83D\uDE1C,\uD83E\uDD2A,\uD83D\uDE1D,\uD83E\uDD11,\uD83E\uDD17,\uD83E\uDD2D,\uD83E\uDD2B,\uD83E\uDD14," +
                "\uD83E\uDD10,\uD83E\uDD28,\uD83D\uDE10,\uD83D\uDE11,\uD83D\uDE36,\uD83D\uDE0F,\uD83D\uDE12,\uD83D\uDE44," +
                "\uD83D\uDE2C,\uD83E\uDD25,\uD83D\uDE0C,\uD83D\uDE14,\uD83D\uDE2A,\uD83E\uDD24,\uD83D\uDE34,\uD83D\uDE37," +
                "\uD83E\uDD12,\uD83E\uDD15,\uD83E\uDD22,\uD83E\uDD2E,\uD83E\uDD27,\uD83D\uDE35,\uD83E\uDD2F,\uD83E\uDD20," +
                "\uD83D\uDE0E,\uD83E\uDD13,\uD83E\uDDD0,\uD83D\uDE15,\uD83D\uDE1F,\uD83D\uDE41,☹️,\uD83D\uDE2E,\uD83D\uDE2F," +
                "\uD83D\uDE32,\uD83D\uDE33,\uD83E\uDD7A,\uD83D\uDE26,\uD83D\uDE27,\uD83D\uDE28,\uD83D\uDE30,\uD83D\uDE25," +
                "\uD83D\uDE22,\uD83D\uDE2D,\uD83D\uDE31,\uD83D\uDE16,\uD83D\uDE23,\uD83D\uDE1E,\uD83D\uDE13,\uD83D\uDE29," +
                "\uD83D\uDE2B,\uD83E\uDD71,\uD83D\uDE24,\uD83D\uDE21,\uD83D\uDE20,\uD83E\uDD2C,\uD83D\uDE08,\uD83D\uDC7F," +
                "\uD83D\uDC80,☠️,\uD83D\uDCA9,\uD83E\uDD21,\uD83D\uDC79,\uD83D\uDC7A,\uD83D\uDC7B,\uD83D\uDC7D,\uD83D\uDC7E," +
                "\uD83E\uDD16,\uD83D\uDE3A,\uD83D\uDE38,\uD83D\uDE39,\uD83D\uDE3B,\uD83D\uDE3C,\uD83D\uDE3D,\uD83D\uDE40," +
                "\uD83D\uDE3F,\uD83D\uDE3E,\uD83D\uDC8B,\uD83D\uDC4B,\uD83E\uDD1A,\uD83D\uDD90️,✋,\uD83D\uDD96,\uD83D\uDC4C," +
                "✌️,\uD83E\uDD1E,\uD83E\uDD1F,\uD83E\uDD18,\uD83E\uDD19,\uD83D\uDC48,\uD83D\uDC49,\uD83D\uDC46,\uD83D\uDD95," +
                "\uD83D\uDC47,☝️,\uD83D\uDC4D,\uD83D\uDC4E,✊,\uD83D\uDC4A,\uD83E\uDD1B,\uD83E\uDD1C,\uD83D\uDC4F,\uD83D\uDE4C," +
                "\uD83D\uDC50,\uD83E\uDD32,\uD83E\uDD1D,\uD83D\uDE4F,✍️,\uD83D\uDC85,\uD83E\uDD33,\uD83D\uDCAA,\uD83D\uDC42," +
                "\uD83E\uDDBB,\uD83D\uDC43,\uD83E\uDDE0,\uD83D\uDC40,\uD83D\uDC41️,\uD83D\uDC45,\uD83D\uDC44,\uD83D\uDC76," +
                "\uD83E\uDDD2,\uD83D\uDC66,\uD83D\uDC67,\uD83E\uDDD1,\uD83D\uDC71,\uD83D\uDC68,\uD83E\uDDD4,\uD83D\uDC69," +
                "\uD83D\uDC71\u200D♀️,\uD83D\uDC71\u200D♂️,\uD83E\uDDD3,\uD83D\uDC74,\uD83D\uDC75,\uD83D\uDE4D,\uD83D\uDE4D\u200D♂️," +
                "\uD83D\uDE4D\u200D♀️,\uD83D\uDE4E,\uD83D\uDE4E\u200D♂️,\uD83D\uDE4E\u200D♀️,\uD83D\uDE45,\uD83D\uDE45\u200D♂️," +
                "\uD83D\uDE45\u200D♀️,\uD83D\uDE46,\uD83D\uDE46\u200D♂️,\uD83D\uDE46\u200D♀️,\uD83D\uDC81,\uD83D\uDC81\u200D♂️," +
                "\uD83D\uDC81\u200D♀️,\uD83D\uDE4B,\uD83D\uDE4B\u200D♂️,\uD83D\uDE4B\u200D♀️,\uD83D\uDE47,\uD83D\uDE47\u200D♂️," +
                "\uD83D\uDE47\u200D♀️,\uD83E\uDD26,\uD83E\uDD26\u200D♂️,\uD83E\uDD26\u200D♀️,\uD83E\uDD37,\uD83E\uDD37\u200D♂️," +
                "\uD83E\uDD37\u200D♀️,\uD83E\uDDD1\u200D⚕️,\uD83D\uDC68\u200D⚕️,\uD83D\uDC69\u200D⚕️,\uD83E\uDDD1\u200D\uD83C\uDF93," +
                "\uD83D\uDC68\u200D\uD83C\uDF93,\uD83D\uDC69\u200D\uD83C\uDF93,\uD83E\uDDD1\u200D\uD83C\uDFEB," +
                "\uD83D\uDC68\u200D\uD83C\uDFEB,\uD83D\uDC69\u200D\uD83C\uDFEB,\uD83E\uDDD1\u200D⚖️,\uD83D\uDC68\u200D⚖️," +
                "\uD83D\uDC69\u200D⚖️,\uD83E\uDDD1\u200D\uD83C\uDF3E,\uD83D\uDC68\u200D\uD83C\uDF3E,\uD83D\uDC69,\uD83C\uDF3E," +
                "\uD83E\uDDD1\u200D\uD83C\uDF73,\uD83D\uDC68\u200D\uD83C\uDF73,\uD83D\uDC69," +
                "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66,\uD83D\uDC68\u200D\uD83D\uDC67," +
                "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66,\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC67," +
                "\uD83D\uDC69\u200D\uD83D\uDC66,\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66,\uD83D\uDC69\u200D\uD83D\uDC67," +
                "\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66,\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67,\uD83D\uDDE3️," +
                "\uD83D\uDC64,\uD83D\uDC65,⛑️,\uD83D\uDC84,\uD83D\uDC8D,\uD83D\uDCBC,\uD83E\uDE78,\uD83D\uDC8C,\uD83D\uDD73️,\uD83D\uDCA3," +
                "\uD83D\uDEC0,\uD83D\uDECC,\uD83D\uDD2A,\uD83C\uDFFA,\uD83D\uDDFA️,\uD83D\uDC88,\uD83D\uDEE2️,\uD83D\uDECE️," +
                "⌛,⏳,⌚,⏰,⏱️,⏲️,\uD83D\uDD70️,\uD83C\uDF21️,⛱️,⌨️,\uD83D\uDDB1️,\uD83D\uDDB2️,\uD83D\uDCBD,\uD83D\uDCBE,\uD83D\uDCBF," +
                "\uD83D\uDCC0,\uD83C\uDFA5,\uD83C\uDF9E️,\uD83D\uDCFD️,\uD83D\uDCFA,\uD83D\uDCF7,\uD83D\uDCF8,\uD83D\uDCF9,\uD83D\uDCFC," +
                "\uD83D\uDD0D,\uD83D\uDD0E,\uD83D\uDD6F️,\uD83D\uDCA1,\uD83D\uDD26,\uD83C\uDFEE,\uD83D\uDCD4,\uD83D\uDCD5,\uD83D\uDCD6," +
                "\uD83D\uDCD7,\uD83D\uDCD8,\uD83D\uDCD9,\uD83D\uDCDA,\uD83D\uDCD3,\uD83D\uDCD2,\uD83D\uDCC3,\uD83D\uDCDC,\uD83D\uDCC4," +
                "\uD83D\uDCF0,\uD83D\uDDDE️,\uD83D\uDCD1,\uD83D\uDD16,\uD83C\uDFF7️,\uD83D\uDCB0,\uD83D\uDCB4,\uD83D\uDCB5,\uD83D\uDCB6," +
                "\uD83D\uDCB7,\uD83D\uDCB8,\uD83D\uDCB3,✉️,\uD83D\uDCE7,\uD83D\uDCE8,\uD83D\uDCE9,\uD83D\uDCE4,\uD83D\uDCE5,\uD83D\uDCE6," +
                "\uD83D\uDCEB,\uD83D\uDCEA,\uD83D\uDCEC,\uD83D\uDCED,\uD83D\uDCEE,\uD83D\uDDF3️,✏️,✒️,\uD83D\uDD8B️,\uD83D\uDD8A️," +
                "\uD83D\uDD8C️,\uD83D\uDD8D️,\uD83D\uDCDD,\uD83D\uDCC1,\uD83D\uDCC2,\uD83D\uDDC2️,\uD83D\uDCC5,\uD83D\uDCC6,\uD83D\uDDD2️," +
                "\uD83D\uDDD3️,\uD83D\uDCC7,\uD83D\uDCC8,\uD83D\uDCC9,\uD83D\uDCCA,\uD83D\uDCCB,\uD83D\uDCCC,\uD83D\uDCCD,\uD83D\uDCCE," +
                "\uD83D\uDD87️,\uD83D\uDCCF,\uD83D\uDCD0,✂️,\uD83D\uDDC3️,\uD83D\uDDC4️,\uD83D\uDDD1️,\uD83D\uDD12,\uD83D\uDD13,\uD83D\uDD0F," +
                "\uD83D\uDD10,\uD83D\uDD11,\uD83D\uDDDD️,\uD83D\uDD28,⛏️,⚒️,\uD83D\uDEE0️,\uD83D\uDDE1️,⚔️,\uD83D\uDD2B,\uD83D\uDEE1️," +
                "\uD83D\uDD27,\uD83D\uDD29,⚙️,\uD83D\uDDDC️,⚖️,\uD83E\uDDAF,\uD83D\uDD17,⛓️,⚗️,⚰️,⚱️,\uD83D\uDDFF,\uD83D\uDEB0," +
                "\uD83D\uDD74️,\uD83E\uDDD7,\uD83E\uDDD7\u200D♂️,\uD83E\uDDD7\u200D♀️,\uD83E\uDD3A,\uD83C\uDFC7,⛷️,\uD83C\uDFC2," +
                "\uD83C\uDFCC️,\uD83C\uDFCC️\u200D♂️,\uD83C\uDFCC️\u200D♀️,\uD83C\uDFC4,\uD83C\uDFC4\u200D♂️,\uD83C\uDFC4\u200D♀️," +
                "\uD83D\uDEA3,\uD83D\uDEA3\u200D♂️,\uD83D\uDEA3\u200D♀️,\uD83C\uDFCA,\uD83C\uDFCA\u200D♂️,\uD83C\uDFCA\u200D♀️,⛹️" +
                ",⛹️\u200D♂️,⛹️\u200D♀️,\uD83C\uDFCB️,\uD83C\uDFCB️\u200D♂️,\uD83C\uDFCB️\u200D♀️,\uD83D\uDEB4,\uD83D\uDEB4\u200D♂️," +
                "\uD83D\uDEB4\u200D♀️,\uD83D\uDEB5,\uD83D\uDEB5\u200D♂️,\uD83D\uDEB5\u200D♀️,\uD83E\uDD38,\uD83E\uDD38\u200D♂️," +
                "\uD83E\uDD38\u200D♀️,\uD83E\uDD3C,\uD83E\uDD3C\u200D♂️,\uD83E\uDD3C\u200D♀️,\uD83E\uDD3D,\uD83E\uDD3D\u200D♂️," +
                "\uD83E\uDD3D\u200D♀️,\uD83E\uDD3E,\uD83E\uDD3E\u200D♂️,\uD83E\uDD3E\u200D♀️,\uD83E\uDD39,\uD83E\uDD39\u200D♂️," +
                "\uD83E\uDD39\u200D♀️,\uD83E\uDDD8,\uD83E\uDDD8\u200D♂️,\uD83E\uDDD8\u200D♀️,\uD83C\uDFAA,\uD83D\uDEF6,\uD83C\uDF97️," +
                "\uD83C\uDF9F️,\uD83C\uDFAB,\uD83C\uDF96️,\uD83C\uDFC6,\uD83C\uDFC5,\uD83E\uDD47,\uD83E\uDD48,\uD83E\uDD49,⚽,⚾," +
                "\uD83C\uDFC0,\uD83C\uDFD0,\uD83C\uDFC8,\uD83C\uDFC9,\uD83C\uDFBE,\uD83C\uDFB3,\uD83C\uDFCF,\uD83C\uDFD1,\uD83C\uDFD2," +
                "\uD83C\uDFD3,\uD83C\uDFF8,\uD83E\uDD4A,\uD83E\uDD4B,\uD83E\uDD45,⛳,⛸️,\uD83C\uDFA3,\uD83C\uDFBD,\uD83C\uDFBF," +
                "\uD83D\uDEF7,\uD83E\uDD4C,\uD83C\uDFAF,\uD83C\uDFB1,\uD83C\uDFAE,\uD83C\uDFB0,\uD83C\uDFB2,♟️,\uD83C\uDFAD,\uD83C\uDFA8," +
                "\uD83C\uDFBC,\uD83C\uDFA4,\uD83C\uDFA7,\uD83C\uDFB7,\uD83C\uDFB8,\uD83C\uDFB9,\uD83C\uDFBA,\uD83C\uDFBB,\uD83E\uDD41," +
                "\uD83E\uDE98,\uD83C\uDFAC,\uD83C\uDFF9,\uD83D\uDC98,\uD83D\uDC9D,\uD83D\uDC96,\uD83D\uDC97,\uD83D\uDC93,\uD83D\uDC9E," +
                "\uD83D\uDC95,\uD83D\uDC9F,❣️,\uD83D\uDC94,❤️,\uD83E\uDDE1,\uD83D\uDC9B,\uD83D\uDC9A,\uD83D\uDC99,\uD83D\uDC9C," +
                "\uD83D\uDDA4,\uD83D\uDCAF,\uD83D\uDCA2,\uD83D\uDCAC,\uD83D\uDC41️\u200D\uD83D\uDDE8️,\uD83D\uDDE8️,\uD83D\uDDEF️," +
                "\uD83D\uDCAD,\uD83D\uDCA4,\uD83D\uDCAE,♨️,\uD83D\uDC88,\uD83D\uDED1,\uD83D\uDD5B,\uD83D\uDD67,\uD83D\uDD50,\uD83D\uDD5C," +
                "\uD83D\uDD51,\uD83D\uDD5D,\uD83D\uDD52,\uD83D\uDD5E,\uD83D\uDD53,\uD83D\uDD5F,\uD83D\uDD54,\uD83D\uDD60,\uD83D\uDD55," +
                "\uD83D\uDD61,\uD83D\uDD56,\uD83D\uDD62,\uD83D\uDD57,\uD83D\uDD63,\uD83D\uDD58,\uD83D\uDD64,\uD83D\uDD59,\uD83D\uDD65," +
                "\uD83D\uDD5A,\uD83D\uDD66,\uD83C\uDF00,♠️,♥️,♦️,♣️,\uD83C\uDCCF,\uD83C\uDC04,\uD83C\uDFB4,\uD83D\uDD07,\uD83D\uDD08," +
                "\uD83D\uDD09,\uD83D\uDD0A,\uD83D\uDCE2,\uD83D\uDCE3,\uD83D\uDCEF,\uD83D\uDD14,\uD83D\uDD15,\uD83C\uDFB5,\uD83C\uDFB6," +
                "\uD83D\uDCB9,\uD83C\uDFE7,\uD83D\uDEAE,\uD83D\uDEB0,♿,\uD83D\uDEB9,\uD83D\uDEBA,\uD83D\uDEBB,\uD83D\uDEBC,\uD83D\uDEBE," +
                "⚠️,\uD83D\uDEB8,⛔,\uD83D\uDEAB,\uD83D\uDEB3,\uD83D\uDEAD,\uD83D\uDEAF,\uD83D\uDEB1,\uD83D\uDEB7,\uD83D\uDCF5," +
                "\uD83D\uDD1E,☢️,☣️,⬆️,↗️,➡️,↘️,⬇️,↙️,⬅️,↖️,↕️,↔️,↩️,↪️,⤴️,⤵️,\uD83D\uDD03,\uD83D\uDD04,\uD83D\uDD19,\uD83D\uDD1A," +
                "\uD83D\uDD1B,\uD83D\uDD1C,\uD83D\uDD1D,\uD83D\uDED0,⚛️,\uD83D\uDD49️,✡️,☸️,☯️,✝️,☦️,☪️,☮️,\uD83D\uDD4E,\uD83D\uDD2F" +
                ",♈,♉,♊,♋,♌,♍,♎,♏,♐,♑,♒,♓,⛎,\uD83D\uDD00,\uD83D\uDD01,\uD83D\uDD02,▶️,⏩,⏭️,⏯️,◀️,⏪,⏮️,\uD83D\uDD3C,⏫," +
                "\uD83D\uDD3D,⏬,⏸️,⏹️,⏺️,⏏️,\uD83C\uDFA6,\uD83D\uDD05,\uD83D\uDD06,\uD83D\uDCF6,\uD83D\uDCF3,\uD83D\uDCF4,♀️,♂️,✖️,➕" +
                ",➖,➗,♾️,‼️,⁉️,❓,❔,❕,❗,〰️,\uD83D\uDCB1,\uD83D\uDCB2,⚕️,♻️,⚜️,\uD83D\uDD31,\uD83D\uDCDB,\uD83D\uDD30,⭕,✅,☑️,✔️" +
                ",❌,❎,➰,➿,,〽️,✳️,✴️,❇️,©️,®️,™️,#️⃣,*️⃣,0️⃣,1️⃣,2️⃣,3️⃣,4️⃣,5️⃣,6️⃣,7️⃣,8️⃣,9️⃣,\uD83D\uDD1F,\uD83D\uDD20,\uD83D\uDD21," +
                "\uD83D\uDD22,\uD83D\uDD23,\uD83D\uDD24,\uD83C\uDD70️,\uD83C\uDD8E,\uD83C\uDD71️,\uD83C\uDD91,\uD83C\uDD92,\uD83C\uDD93," +
                "ℹ️,\uD83C\uDD94,Ⓜ️,\uD83C\uDD95,\uD83C\uDD96,\uD83C\uDD7E️,\uD83C\uDD97,\uD83C\uDD7F️,\uD83C\uDD98,\uD83C\uDD99," +
                "\uD83C\uDD9A,\uD83C\uDE01,\uD83C\uDE02️,\uD83C\uDE37️,\uD83C\uDE36,\uD83C\uDE2F,\uD83C\uDE50,\uD83C\uDE39,\uD83C\uDE1A," +
                "\uD83C\uDE32,\uD83C\uDE51,\uD83C\uDE38,\uD83C\uDE34,\uD83C\uDE33,㊗️,㊙️,\uD83C\uDE3A,\uD83C\uDE35,\uD83D\uDD34," +
                "\uD83D\uDD35,⚫,⚪,⬛,⬜,◼️,◻️,◾,◽,▪️,▫️,\uD83D\uDD36,\uD83D\uDD37,\uD83D\uDD38,\uD83D\uDD39,\uD83D\uDD3A," +
                "\uD83D\uDD3B,\uD83D\uDCA0,\uD83D\uDD18,\uD83D\uDD33,\uD83D\uDD32,\uD83C\uDFC1,\uD83D\uDEA9,\uD83C\uDF8C,\uD83C\uDFF4," +
                "\uD83C\uDFF3️,\uD83C\uDFF3️\u200D\uD83C\uDF08,\uD83C\uDFF3️\u200D⚧️,\uD83C\uDFF4\u200D☠️,\uD83C\uDFF4\u200D☠️," +
                "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F," +
                "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F," +
                "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F," +
                "\uD83C\uDFF4\uDB40\uDC75\uDB40\uDC73\uDB40\uDC74\uDB40\uDC78\uDB40\uDC7F\uDB40\uDC75\uDB40\uDC73\uDB40\uDC74\uDB40\uDC78\uDB40\uDC7F";
        if (StringUtils.isNotBlank(emojis)) {
            for (String emoji : emojis.split(",")) {
                emojiRes.add(emoji);
            }
        }

        Set<Integer> emojisSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(emojiRes)) {
            char[] chs;
            for (String curr : emojiRes) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    emojisSet.add(BCConvert.charConvert(c));
                }
            }
        }
        return emojisSet;
    }

    public static void main(String[] args) {
        String content = "你♋♌♍♎好✴️，官⏳⌚⏰彦⚗️⚰️峰✌⛳！☕";
        char[] valueChars = content.toCharArray();
        Set<Integer> emojis = getEmojis();
        StringBuilder cleanContent = new StringBuilder();
        for (char valueChar : valueChars) {
            int temp = BCConvert.charConvert(valueChar);
            if (emojis.contains(temp)) {
                /*过滤特殊字符*/
                continue;
            }
            cleanContent.append(valueChar);
        }
        System.out.println(cleanContent.toString());
    }
}
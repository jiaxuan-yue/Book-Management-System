package com.example.common;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * 富文本消毒，防止存储型 XSS（如 img onerror 偷 Token）
 */
public final class HtmlSanitizer {

    private HtmlSanitizer() {
    }

    /** 允许常见排版标签，禁止 script / 事件属性 / javascript: */
    private static final Safelist RICH = Safelist.relaxed()
            .addAttributes(":all", "class", "style")
            .addProtocols("a", "href", "http", "https", "mailto")
            .addProtocols("img", "src", "http", "https");

    public static String cleanRich(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        return Jsoup.clean(html, RICH);
    }

    /** 纯文本（评论等） */
    public static String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Jsoup.clean(text, Safelist.none());
    }
}

package com.spiderdt.common.notice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.service
 * @Description:
 * @date 2017/6/15 17:38
 */
public class RegexExampleTest {

    public static void main(String args[]) {
//        String content = "I am noob " + "from runoob.com.";
//        String pattern = ".*runoob.*";
//        boolean isMatch = Pattern.matches(pattern, content);
//        System.out.println(isMatch);

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "<title>Document</title>\n" +
                "</head>\n" +
                "<body style=\"color: #666\">\n" +
                "<p><strong>您好:</strong></p >\n" +
                "<img src=\"http://localhost:8080/track/open123\" width=\"0\" height=\"0\" style=\"display: none\">\n"+
                "</body>\n" +
                "<style>\n" +
                "a:hover {\n" +
                "color: #5b9ed8 !important;\n" +
                "text-decoration: underline !important;\n" +
                "}\n" + "<a style=\"color: #337ab7; text-decoration: none;\" href=\"https://cocacola.spiderdt.com/reset-password?ticket=b9387ba1606a5537e18acef496f033c516551b8a\" target=\"_blank\">点击此处更改密码</a>" +
                "}\n" + "<a  href=\"https://cocacola.spiderdt.com/reset-passwor\" target=\"_blank\">点击此处更改密码</a>" +
                "</style>\n" +
                "<a href=\"https://www.google.com#/bai\">3</a>" +
                "<img src=\"http://blog.csdn.net/yaerfeng/article/details/18402569\">\n"+
                "</html>";
//        html = String.format(html, "http://localhost:8080/track/open123");
//        System.out.println(html);
//        Pattern patternHttp = Pattern.compile("((http|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?");
//        Pattern patternHttp = Pattern.compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
//        html = "<div><a href='http://www.baidu.com' title='tip'>aaa</a>" +
//                "\n<a href='https://www.baidu.com?param=1' title='baiduSearch'>bbb</a><span>" +
//                "\n<a href='file:///d:/test/Arr.txt'>ccc</a></span></div>";
//        Pattern patternHttp = Pattern.compile("<a.*?href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");
        String htmlMessage = getAndReplaceALink(html);
        System.out.println(htmlMessage);
    }

    public static String getAndReplaceALink(String htmlMessage) {

        Pattern patternHttp = Pattern.compile("<a.*?href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");
        Matcher matcher = patternHttp.matcher(htmlMessage);
        Map<Integer, String> linkOrderMap = new HashMap<Integer, String>();
        int i = 0;
        while(matcher.find()) {
            String link = matcher.group(1).trim();
            linkOrderMap.put(i, link);
            System.out.println("link: " + link);
            i++;
        }
        for (Map.Entry<Integer, String> entry : linkOrderMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = "  + entry.getValue());
            htmlMessage = htmlMessage.replace(entry.getValue(), entry.getValue()+entry.getKey().toString());
        }

        return htmlMessage;
    }

}

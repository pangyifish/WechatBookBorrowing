package org.liufeng.course.service;


import java.util.ArrayList;  
import java.util.Date;  
import java.util.List;  
import java.util.Map;  
  
import javax.servlet.http.HttpServletRequest;  
  
import org.liufeng.course.message.resp.Article;  
import org.liufeng.course.message.resp.NewsMessage;  
import org.liufeng.course.message.resp.TextMessage;  
import org.liufeng.course.util.MessageUtil;  
  
/** 
 * 核心服务类 
 *  
 *  
 * @date 2013-07-25 
 */  
public class CoreService {  
    /** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;  
        try {  
            // xml请求解析  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
  
            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");  
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");  
            // 消息类型  
            String msgType = requestMap.get("MsgType");  
  
            // 默认回复此文本消息  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
        
            // 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义  
            textMessage.setContent("欢迎访问<a href=\"http://www.sufe.edu.cn/main.htm\">上海财经大学</a>!");
            // 将文本消息对象转换成xml字符串  
            respMessage = MessageUtil.messageToXml(textMessage);  
  
            // 文本消息  
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
                // 接收用户发送的文本消息内容  
                String content = requestMap.get("Content");  
  
                // 创建图文消息  
                NewsMessage newsMessage = new NewsMessage();  
                newsMessage.setToUserName(fromUserName);  
                newsMessage.setFromUserName(toUserName);  
                newsMessage.setCreateTime(new Date().getTime());  
                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
               
  
                List<Article> articleList = new ArrayList<Article>();  
                // 单图文消息  
                if ("1".equals(content)) {          
                	String url=java.net.URLEncoder.encode("http://www.duopao.com","utf-8");
                	System.out.println("url is"+url);
                	Article article = new Article();  
                    article.setTitle("上海财经大学");
                    article.setDescription("大学生创新创业项目");
                    
                    articleList.add(article);  
                    // 设置图文消息个数  
                    newsMessage.setArticleCount(articleList.size());  
                    // 设置图文消息包含的图文集合  
                    newsMessage.setArticles(articleList);  
                    // 将图文消息对象转换成xml字符串  
                    respMessage = MessageUtil.messageToXml(newsMessage);  
                }  
                // 单图文消息---不含图片  
                else if ("2".equals(content)) {  
                    Article article = new Article();  
                    article.setTitle("上海财经大学");
                    // 图文消息中可以使用QQ表情、符号表情  
                    article.setDescription("大学生创新创业项目" + emoji(0x1F6B9));
                    // 将图片置为空  
                    article.setPicUrl("");  
                    //article.setUrl("http://b1083dfd.ngrok.io/wydl/index.jsp");  
                   
                    article.setUrl("http://d4e6c76b.ngrok.io/wydl/test");  
                    
                    
                    articleList.add(article);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                    respMessage = MessageUtil.messageToXml(newsMessage);  
                }  
                // 多图文消息  
                else if ("3".equals(content)) {  
                    Article article1 = new Article();  
                    article1.setTitle("微信公众帐号开发");
                    article1.setDescription("");  
                    article1.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/ic92t4dNf0qCUopLrNTENQ8t1uGt3oeAUbX2boXa9XOHS8GeDCQdKmKicI2c9hO3ZJOwdSXkqW0Lqa9xEUia0ByuQ/0?wx_fmt=jpeg");  
                    article1.setUrl("http://blog.csdn.net/lyq8479/article/details/8937622");  
  
                    Article article2 = new Article();  
                    article2.setTitle("第2篇\n微信公众帐号的类型");  
                    article2.setDescription("");  
                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8941577");  
  
                    Article article3 = new Article();  
                    article3.setTitle("第3篇\n开发模式启用及接口配置");  
                    article3.setDescription("");  
                    article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8944988");  
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                    respMessage = MessageUtil.messageToXml(newsMessage);  
                }  
                // 多图文消息---首条消息不含图片  
                else if ("4".equals(content)) {  
                    Article article1 = new Article();  
                    article1.setTitle("微信公众帐号开发教程Java版");  
                    article1.setDescription("");  
                    // 将图片置为空  
                    article1.setPicUrl("");  
                    article1.setUrl("http://blog.csdn.net/lyq8479");  
  
                    Article article2 = new Article();  
                    article2.setTitle("第4篇\n消息及消息处理工具的封装");  
                    article2.setDescription("");  
                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8949088");  
  
                    Article article3 = new Article();  
                    article3.setTitle("第5篇\n各种消息的接收与响应");  
                    article3.setDescription("");  
                    article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8952173");  
  
                    Article article4 = new Article();  
                    article4.setTitle("第6篇\n文本消息的内容长度限制揭秘");  
                    article4.setDescription("");  
                    article4.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article4.setUrl("http://blog.csdn.net/lyq8479/article/details/8967824");  
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3);  
                    articleList.add(article4);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                    respMessage = MessageUtil.messageToXml(newsMessage);  
                }  
                // 多图文消息---最后一条消息不含图片  
                else if ("5".equals(content)) {  
                    Article article1 = new Article();  
                    article1.setTitle("第7篇\n文本消息中换行符的使用");  
                    article1.setDescription("");  
                    article1.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/ic92t4dNf0qCUopLrNTENQ8t1uGt3oeAUbX2boXa9XOHS8GeDCQdKmKicI2c9hO3ZJOwdSXkqW0Lqa9xEUia0ByuQ/0?wx_fmt=jpeg");  
                    article1.setUrl("http://blog.csdn.net/lyq8479/article/details/9141467");  
  
                    Article article2 = new Article();  
                    article2.setTitle("第8篇\n文本消息中使用网页超链接");  
                    article2.setDescription("");  
                    article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");  
                    article2.setUrl("http://blog.csdn.net/lyq8479/article/details/9157455");  
  
                    Article article3 = new Article();  
                    article3.setTitle("希望大学生创新创业大赛圆满成功");  
                    article3.setDescription("");  
                    // 将图片置为空  
                    article3.setPicUrl("");  
                    article3.setUrl("http://blog.csdn.net/lyq8479");  
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3);  
                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                    respMessage = MessageUtil.messageToXml(newsMessage);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return respMessage;  
    }  
  
    /** 
     * emoji表情转换(hex -> utf-16) 
     *  
     * @param hexEmoji 
     * @return 
     */  
    public static String emoji(int hexEmoji) {  
        return String.valueOf(Character.toChars(hexEmoji));  
    }  
}  

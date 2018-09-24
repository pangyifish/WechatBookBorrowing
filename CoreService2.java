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

import com.bean.Tborrow;
import com.bean.Tuser;
import com.util.BookUtil;

/**
 * 核心服务类
 * 
 * 
 * @date 2018-01-02
 */
public class CoreService2 {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return xml
	 */
	public static String processRequest(HttpServletRequest request) {
		// xml格式的消息数据
		String respXml = null;
		try {
			// 调用parseXml方法解析请求消息
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			
			System.out.println("msgType is:"+msgType);
			System.out.println("fromUserName is:"+fromUserName);
			System.out.println("toUserName is:"+toUserName);
			
			if(BookUtil.userexist(fromUserName)==false){
				String nickname=BookUtil.getUsernickeName(fromUserName);
				//BookUtil.insertUser(fromUserName,nickname);
				if(request.getSession().getAttribute("user")!=null){
					Tuser user=(Tuser)request.getSession().getAttribute("user");
					System.out.println("currentopenid is:"+user.getId());
				}else{
					Tuser user=new Tuser();
					user.setId(fromUserName);
					request.getSession().setAttribute("user",user);
					System.out.println("currentopenid is:"+user.getId());
				}	
			}else{
				if(request.getSession().getAttribute("user")!=null){
					Tuser user=(Tuser)request.getSession().getAttribute("user");
					System.out.println("currentopenid is:"+user.getId());
				}else{
					Tuser user=new Tuser();
					user.setId(fromUserName);
					request.getSession().setAttribute("user",user);
					System.out.println("currentopenid is:"+user.getId());
				}	
			}
			
			if(msgType==null){
				msgType="";
			}

			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			// 事件推送
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				
				if(eventType==null){
					eventType="";
				}
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					textMessage.setContent("您好，欢迎关注图书管理系统公众账号，从这里开！");
					// 将消息对象转换成xml
					respXml = MessageUtil.messageToXml(textMessage);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					textMessage.setContent("我们将进最大加油的改进");
				
					respXml = MessageUtil.messageToXml(textMessage);
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建菜单时的key值对应
					String eventKey = requestMap.get("EventKey");
					// 根据key值判断用户点击的按钮
					if (eventKey.equals("aboutauthor")) {
					
						Article article = new Article();
						article.setTitle("作者信息");
						article.setDescription("大学生课程设计");

						List<Article> articleList = new ArrayList<Article>();
						articleList.add(article);
					
						NewsMessage newsMessage = new NewsMessage();
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respXml = MessageUtil.messageToXml(newsMessage);
					
					
					} else if (eventKey.equals("mynotice")) {
						List borrowList=BookUtil.getMyNote(fromUserName);
						List<Article> articleList = new ArrayList<Article>();
						if(borrowList!=null&&borrowList.size()>0){
						for (int i=0;i<borrowList.size();i++){
								Tborrow borrow=(Tborrow)borrowList.get(i);
								Article article = new Article();
								article.setTitle(borrow.getUserName()+",您借阅的:"+borrow.getBookName()+",已于"+borrow.getReturnDate()+"到期,请尽快归还");
							    articleList.add(article);
							}
						NewsMessage newsMessage = new NewsMessage();
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respXml = MessageUtil.messageToXml(newsMessage);
						}
						else{
						 
						  textMessage.setContent("暂时还没有到期图书");
						  respXml = MessageUtil.messageToXml(textMessage);
						}	
					}
					
				}
			}
			// 当用户发消息时
			else {
				System.out.println("请通过菜单使用网址导航服务,谢谢");
				textMessage.setContent("请通过菜单使用网址导航服务,谢谢");
				respXml = MessageUtil.messageToXml(textMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respXml;
	}
}

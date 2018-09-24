package org.liufeng.weixin.main;

import org.liufeng.course.menu.Button;
import org.liufeng.course.menu.ClickButton;
import org.liufeng.course.menu.ComplexButton;
import org.liufeng.course.menu.Menu;
import org.liufeng.course.menu.ViewButton;
import org.liufeng.course.pojo.Token;
import org.liufeng.course.util.CommonUtil;
import org.liufeng.course.util.MenuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 菜单管理器类
 * 
 * @author 
 * @date 2016-10-17
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	/**
	 * 定义菜单结构
	 * 
	 * @return
	 */
	private static Menu getMenu(String url) {
		ClickButton btn11 = new ClickButton();
		btn11.setName("用户绑定");
		btn11.setType("click");
		btn11.setKey("aboutauthor");

		ClickButton btn12 = new ClickButton();
		btn12.setName("催还通知");
		btn12.setType("click");
		btn12.setKey("mynotice");

	
		
		
		ViewButton btn21 = new ViewButton();
		btn21.setName("我的借阅");
		btn21.setType("view");
		btn21.setUrl(url+"book?type=myborrow");

		ViewButton btn22 = new ViewButton();
		btn22.setName("图书信息");
		btn22.setType("view");
		btn22.setUrl(url+"page/bookquery.jsp");
		
		
		
		ViewButton btn23 = new ViewButton();
		btn23.setName("管理员登陆");
		btn23.setType("view");
		btn23.setUrl(url+"page/login.jsp");

		
	
		
		ComplexButton mainBtn = new ComplexButton();
		mainBtn.setName("图书室");
		mainBtn.setSub_button(new Button[] {btn23,btn11,btn21,btn22 ,btn12});
		
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn});

		return menu;
	}

	public static void main(String[] args) {
		//这行需要修改的地方
		String url="http://47.94.2.184/weixinbook/";
		// 第三方用户唯一凭证
		String appId = "wxd638fcf47e42b1be";
		// 第三方用户唯一凭证密钥
		String appSecret = "e867c2e02a354189949a9ecade3cc2d3";

		// 调用接口获取凭证
		Token token = CommonUtil.getToken(appId, appSecret);

		if (null != token) {
			System.out.println("menu is:"+MenuUtil.deleteMenu(token.getAccessToken()));	
			boolean result = MenuUtil.createMenu(getMenu(url), token.getAccessToken());
			System.out.println("menu is:"+MenuUtil.getMenu(token.getAccessToken()));
		    System.out.println("menu is:"+MenuUtil.getMenu(token.getAccessToken()));
		    // 判断菜单创建结果
			if (result)
				log.info("菜单创建成功！");
			else
			  log.info("菜单创建失败！");
	}
	}
}






package org.liufeng.weixin.main;

import org.liufeng.course.pojo.Token;
import org.liufeng.course.pojo.WeixinUserInfo;
import org.liufeng.course.pojo.WeixinUserList;
import org.liufeng.course.util.AdvancedUtil;
import org.liufeng.course.util.CommonUtil;

public class TestAdvancedFace {

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wx06b5f6e887e33af3";
		// 第三方用户唯一凭证密钥
		String appSecret = "f2fd7a29e1fe75d1a1cd3f4d8dae302e";

		// 调用接口获取凭证
		Token token = CommonUtil.getToken(appId, appSecret);
		
		WeixinUserList weixinUserList =AdvancedUtil.getUserList(token.getAccessToken(), "");
		
		
		System.out.println("总关注用户数：" + weixinUserList.getTotal());
		System.out.println("本次获取用户数：" + weixinUserList.getCount());
		System.out.println("OpenID列表：" + weixinUserList.getOpenIdList().toString());
		System.out.println("next_openid：" + weixinUserList.getNextOpenId());
		
		for( int j=0;j<weixinUserList.getOpenIdList().size();j++){
			
			
			WeixinUserInfo user = AdvancedUtil.getUserInfo(token.getAccessToken(),weixinUserList.getOpenIdList().get(j));
			System.out.println("OpenID：" + user.getOpenId());
			System.out.println("关注状态：" + user.getSubscribe());
			System.out.println("关注时间：" + user.getSubscribeTime());
			System.out.println("昵称：" + user.getNickname());
			System.out.println("性别：" + user.getSex());
			System.out.println("国家：" + user.getCountry());
			System.out.println("省份：" + user.getProvince());
			System.out.println("城市：" + user.getCity());
			System.out.println("语言：" + user.getLanguage());
			System.out.println("头像：" + user.getHeadImgUrl());
		}
		
		
		 AdvancedUtil.getMediaList(token.getAccessToken());
	}
	
	
}

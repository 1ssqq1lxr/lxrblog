package com.it.jfinal.interceptor;

import org.apache.commons.lang3.Validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月3日下午1:50:08 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class BlogValidator extends Validator{

//	校验错误
	protected void handleError(Controller me) {

		
	}
//	验证
	protected void validate(Controller me) {
//		validateRequiredString("blog.title", "titleMsg", "请输入Blog标题!");
//		validateRequiredString("blog.content", "contentMsg", "请输入Blog内容!");
//		validateRequiredString("comment.email", "emailMsg", "请输入邮箱以便联系!");
//		validateRequiredString("comment.username", "userMsg", "请输入用户名!");
		validateRequiredString("title", "titleMsg", "请输入Blog标题!");
		validateRequiredString("content", "contentMsg", "请输入Blog内容!");
		validateRequiredString("email", "emailMsg", "请输入邮箱以便联系!");
		validateRequiredString("username", "userMsg", "请输入用户名!");
	}

}

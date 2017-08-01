package com.it.jfinal.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月3日上午11:09:17 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class PicHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] bs) {
			if(target.startsWith("file")||target.startsWith("F:")){
				bs[0]=true;
				next.handle(target, request, response, bs);
			}
			else{
				next.handle(target, request, response, bs);
			}
	}

}

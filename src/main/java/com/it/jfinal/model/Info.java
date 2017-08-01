package com.it.jfinal.model;

import com.jfinal.plugin.activerecord.Model;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月2日下午11:13:24 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class Info extends Model<Info> {
		public  static Info dao=new Info();
		public Info getAll(){
			return (Info) find("select *"," from info");
		}
}

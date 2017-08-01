package com.it.jfinal.utils;

import org.apache.solr.common.SolrDocument;

import com.it.jfinal.model.Blog;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月5日下午1:50:33 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class BlogUtils {
		public static Blog getBlog(SolrDocument solrDocument){
			Blog blog=new Blog();
			blog.set("id", solrDocument.get("id"));
			blog.set("title", solrDocument.get("title"));
			blog.set("content", solrDocument.get("content"));
			blog.set("create_time", solrDocument.get("create_time"));
			blog.set("pic_url", solrDocument.get("pic_url"));
			return  blog;
		}
}

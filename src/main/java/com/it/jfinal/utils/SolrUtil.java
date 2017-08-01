package com.it.jfinal.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.it.jfinal.DataimportUtils;
import com.it.jfinal.model.Blog;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月5日上午10:03:02 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class SolrUtil {
		//增加
		public static void addIndex(Blog blog ) throws SolrServerException, IOException{
			SolrClient client=DataimportUtils.createSolrServer();
					SolrInputDocument solrInputDocument=new SolrInputDocument();
					solrInputDocument.addField("id", blog.get("id"));
					solrInputDocument.addField("title", blog.get("title"));
					solrInputDocument.addField("content", blog.get("content"));
					solrInputDocument.addField("pic_url", blog.get("pic_url"));
					solrInputDocument.addField("create_time", blog.get("create_time"));
					client.add(solrInputDocument);
					client.commit();
					client.close();
		}
		//删除
		
		//查询
		public static Page<Blog> queryIndex(String param) throws SolrServerException, IOException{
			SolrClient client=DataimportUtils.createSolrServer();
			SolrQuery solrQuery=new SolrQuery();
			solrQuery.addSort("create_time", ORDER.desc);
//			solrQuery.setStart(pageNum);
//			solrQuery.setRows(8);
			if(StrKit.notBlank(param)){
				solrQuery.setQuery("title:"+param+" OR "+"content:"+param);
			}else{
				solrQuery.setQuery("*:*");
			}
			QueryResponse queryResponse= client.query(solrQuery);
			SolrDocumentList list=  queryResponse.getResults();
			List<Blog> lists=new ArrayList<Blog>();  
			if(list.getNumFound()>0&&list!=null){
				for(SolrDocument solrDocument:list){
//					将solrDocument转换为Blog对象
					Blog blog = BlogUtils.getBlog(solrDocument);
					List<Blog> find = blog.find("select click_times,comment_times,agree_with_times from blog where id="+blog.get("id"));
					blog.set("click_times", find.get(0).get("click_times"));
					blog.set("comment_times", find.get(0).get("comment_times"));
					blog.set("agree_with_times", find.get(0).get("agree_with_times"));
					lists.add(blog);
				}
			}
//			int s=getCount(param); s%8==0?s%8:s%8+1
			Page<Blog> page=new Page<Blog>(lists,1, 8,8,1);
			return page;
		}
		public static int getCount(String params) throws SolrServerException, IOException{//获得总记录数
			SolrClient client=DataimportUtils.createSolrServer();
			SolrQuery solrQuery=new SolrQuery();
			solrQuery.setQuery("*:*");
			QueryResponse queryResponse= client.query(solrQuery);
			SolrDocumentList list=  queryResponse.getResults();
			return (int) list.getNumFound();
		}
}

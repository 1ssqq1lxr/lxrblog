package com.it.jfinal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

import com.it.jfinal.model.Blog;

public class DataimportUtils{
	public final static String  SOLR_URL="http://192.168.121.135:8080/solr/lxr";	

	public static  SolrClient createSolrServer(){
		SolrClient solrClient=null;
		try {
			solrClient=new HttpSolrClient(SOLR_URL);

			SolrPingResponse response=solrClient.ping();
			System.out.println(response.getElapsedTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return solrClient;
	}
	public static void importData() throws SolrServerException, IOException{
		SolrClient client=createSolrServer();
		List<Blog> blogs=Blog.dao.find("select id,title,content,create_time,pic_url from blog");
		if(blogs!=null&&blogs.size()>0){
			for(int i=0;i<blogs.size();i++){
				Blog blog=blogs.get(i);
				SolrInputDocument solrInputDocument=new SolrInputDocument();
				solrInputDocument.addField("id", blog.get("id"));
				solrInputDocument.addField("title", blog.get("title"));
				solrInputDocument.addField("content", blog.get("content"));
				solrInputDocument.addField("create_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blog.get("create_time")));
				solrInputDocument.addField("pic_url", blog.get("pic_url"));
				client.add(solrInputDocument);

			}
			client.commit();
		}
	}
	@Test
	public void test() throws SolrServerException, IOException{
		SolrClient client=DataimportUtils.createSolrServer();
		SolrQuery solrQuery=new SolrQuery();
		solrQuery.setQuery("id:*");
		QueryResponse response=client.query(solrQuery);
		SolrDocumentList document=response.getResults();
		System.out.println(document.getNumFound());
	} 
	@Test
	public void add() throws SolrServerException, IOException{
		SolrClient client=DataimportUtils.createSolrServer();
		SolrInputDocument solrInputDocument=new SolrInputDocument();
		solrInputDocument.addField("id", "1");
		solrInputDocument.addField("content", "你是猪吗");
		solrInputDocument.addField("title", "asdd");
		solrInputDocument.addField("create_time", "ss");
		solrInputDocument.addField("pic_url", "adad");
		client.add(solrInputDocument);
		client.commit();
	} 
	@Test
	public void delete() throws SolrServerException, IOException{
		SolrClient client=DataimportUtils.createSolrServer();
		client.deleteByQuery("id:*");
		client.commit();
	} 
	@Test
	public void query() throws SolrServerException, IOException{
		SolrClient client=DataimportUtils.createSolrServer();
		SolrQuery solrQuery=new SolrQuery();
//		solrQuery.addSort("create_time", ORDER.desc);
//		solrQuery.set("title", "jsp");	
		//直接查询
//		solrQuery.setQuery("content:jsp");
//		solrQuery.setQuery("*:*");
//		查询  
		solrQuery.set("q", "content:成功 title:成功");

		QueryResponse query = client.query(solrQuery);
		SolrDocumentList results = query.getResults();
		System.out.println(results.getNumFound());
	
	}
}

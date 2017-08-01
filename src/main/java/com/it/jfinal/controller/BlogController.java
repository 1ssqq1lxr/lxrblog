package com.it.jfinal.controller;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.catalina.ant.StartTask;
import org.apache.solr.client.solrj.SolrServerException;

import com.it.jfinal.DataimportUtils;
import com.it.jfinal.interceptor.BlogValidator;
import com.it.jfinal.model.Album;
import com.it.jfinal.model.Blog;
import com.it.jfinal.model.Comment;
import com.it.jfinal.model.Info;
import com.it.jfinal.utils.RedisUtils;
import com.it.jfinal.utils.SolrUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.upload.UploadFile;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月2日下午7:28:27 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class BlogController extends Controller {
//	主页
	 public void index() throws SolrServerException, IOException {
		 DataimportUtils.importData();//初始化数据
		 //访问人数
		 Info info=Info.dao.findById("1"); 
		 String str=getSessionAttr("now");
		 setAttr("info", info);
		 if(StrKit.isBlank(str)){
//			 访问人数加一
			 info.set("today_click_times",info.getInt("today_click_times")+1);
			 info.set("history_click_times",info.getInt("history_click_times")+1);
			 setSessionAttr("now", "now");
			 info.dao.update();
		 }
//		加载最新blog
		 	Page<Blog> 
			 	page=Blog.dao.paginateSome(getParaToInt(0, 1), 8);
			 	 setAttr("blogPage",page);
		
			//阅读量最多的文章
			setAttr("mostLookBlogList",Blog.dao.mostCommentTimes());
			//评论量最多的文章
			setAttr("mostCommentBlogList",Blog.dao.mostClicktims());
			//最近评论
			setAttr("commentList",Comment.dao.lastedComment());
			//最近照片
			setAttr("picList", Album.dao.getSome());
			setAttr("pageSearchType", 0);
		
			render("blog_index.html");
	}
//	 博客详情
	 public void detail(){
		 int id = getParaToInt();
			Blog blog = Blog.dao.findById(id);
			setAttr("x", blog);
			setAttr("commentList", Comment.dao.getCommentList(id));
			blog.set("click_times",blog.getInt("click_times")+1);
			blog.update();
			setAttr("pageSearchType",3);
			render("detail.html");
	 }
//	 跳转到登陆页面
	 public void adminHtml(){
		 String sessionId=getSessionAttr("user");
		 if(StrKit.notBlank(sessionId)){
			 render("blog_add.html");
		 }else{
			 render("blog_admin.html");
		 }
	 }
//	登陆验证
//	 @Before(BlogValidator.class)
	 public void admin(){
			String username = getPara("username");
			String password = getPara("password");
		 if(
				 username.equals("lxr")&&password.equals("123")){
			 	setSessionAttr("user", "lxr");
				render("blog_add.html");
		}else
				renderJson("用户名密码错误");
	 }
//	 上传图片
	 public void uploadPic(){
		 		UploadFile uploadFile=getFile("pic", "", 1240*24*2,"utf-8");
		 		String path=uploadFile.getUploadPath();
		 		String houzhui=uploadFile.getOriginalFileName().substring(uploadFile.getOriginalFileName().lastIndexOf("."));
		 		File file=uploadFile.getFile();
		 		String date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		 		file.renameTo(new File(path+date+houzhui));
		 		setAttr("picUrl", "/upload/"+date+houzhui);
		 		render("blog_add.html");
	 }
//	 增加blog
//	 @Before(BlogValidator.class)
	 public void add() throws SolrServerException, IOException{
		 	int type = getParaToInt("type");
			String content = getPara("content");
			String title = getPara("title");
			String picUrl = getPara("picUrl");
			Blog blog = new Blog();
			blog.set("type",type);
			blog.set("title",title);
			blog.set("content",content);
			Date date=new Date();
			blog.set("create_time",date);
			blog.set("pic_url",picUrl);
			blog.save();
			Blog blog2=new Blog();
//			此方法可以拿到id
			int id=blog.getInt("id");
			blog2.set("id", blog.getInt("id"));
			blog2.set("title", title);
			blog2.set("content", content);
			blog2.set("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
			blog2.set("pic_url", picUrl);
			SolrUtil.addIndex(blog2);
			this.clearCache();
			redirect("/");
	 }
//	 清初缓存
	 public void clearCache(){
			Redis.use(RedisUtils.REDIS_CACHE_NAME).del(RedisUtils.REDIS_MOST_CLICKS);//删除最大点击量
			Redis.use(RedisUtils.REDIS_CACHE_NAME).del(RedisUtils.REDIS_MOST_COMMENTS);//删除最大评论
	 }
//	 查询
	
	 public void searchByType(){
//		 1 type 2 pageNum
//		 	放入redis中 3种类型
		 	int typeid=getParaToInt(0);
		 	Page<Blog> blogs=Blog.dao.searchByType(typeid,getParaToInt(1,1));
		 	setAttr("blogPage", blogs);
		 	setAttr("type", typeid);
		 	setAttr("pageSearchType", 1);
		 	render("blog_search.html");
	 }
	 public void searchByTitle() throws SolrServerException, IOException{
		 String str=getPara("titleSearch");
		 Page<Blog> queryIndex = SolrUtil.queryIndex(str);
		 setAttr("blogPage", queryIndex);
//		 if(StrKit.notBlank(str)){
//		 setAttr("type", str);
//		 }
		 setAttr("pageSearchType", 2);
		 render("blog_search.html");
	 }
}

package com.it.jfinal.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.it.jfinal.interceptor.BlogValidator;
import com.it.jfinal.model.Blog;
import com.it.jfinal.model.Comment;
import com.it.jfinal.utils.RedisUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.redis.Redis;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月3日下午12:17:59 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/

public class CommentController extends Controller {
//	 @Before(BlogValidator.class)
	public void add(){
		 Comment comment = getModel(Comment.class,"comment");
	     comment.set("create_time",new Date());
	     comment.save();
	     int blog_id=comment.getInt("blog_id");
	     Blog blog= Blog.dao.findById(blog_id); 
	     blog.set("comment_times", blog.getInt("comment_times")+1);
	     blog.update();
	    setAttr("x", blog);
	    setAttr("commentList", Comment.dao.getCommentList(blog_id));
        setAttr("pageSearchType",3);
//        清除缓存
    	Redis.use(RedisUtils.REDIS_CACHE_NAME).del(RedisUtils.REDIS_COMMENTS);
		redirect("/detail/"+blog_id+"#comment_id");
		
	}
}

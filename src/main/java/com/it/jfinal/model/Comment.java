package com.it.jfinal.model;

import java.util.List;

import com.it.jfinal.utils.RedisUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Redis;

/** *@author:作者 :
 *@E-mail：
 *@date:创建时间：2016年9月2日下午9:01:07 
 *@version:1.0 
 *@parameter: 
 *@since: 
 *@return: 
 */
public class Comment extends Model<Comment>{
	public static Comment dao=new Comment();
	//最近评论
	public List<Record> lastedComment(){
		List<Record> page=null;
		//		加入redis缓存
	
			page=Redis.use(RedisUtils.REDIS_CACHE_NAME).get(RedisUtils.REDIS_COMMENTS);
			if(page!=null&&page.size()>0){
				return page;
			}else{
				String sql="select blog.id,blog.title,comment.content,comment.create_time,comment.username " +
						"from comment left join blog on blog.id = comment.blog_id " +
						"ORDER BY comment.create_time desc LIMIT 0,5";
				page=(List<Record>) Db.find(sql);
				Redis.use(RedisUtils.REDIS_CACHE_NAME).set(RedisUtils.REDIS_COMMENTS, page);
				Redis.use(RedisUtils.REDIS_CACHE_NAME).expire(RedisUtils.REDIS_COMMENTS, 60*60*24);
				return page;
			}
	}
	public Object getCommentList(int id) {
	
		return 	find("select c.* from comment c where c.blog_id= "+id+" order by create_time");
		
	}
}

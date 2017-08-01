package com.it.jfinal.model;

import java.util.List;

import com.it.jfinal.utils.RedisUtils;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Redis;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月2日下午7:27:59 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class Blog extends Model<Blog>{
	public static Blog dao=new Blog();
	public Page<Blog> paginateSome(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from blog order by create_time desc");
	}
	//阅读量最多的文章
	public List<Blog> mostClicktims(){
//		加入缓存
		List<Blog> blogs=Redis.use(RedisUtils.REDIS_CACHE_NAME).get(RedisUtils.REDIS_MOST_CLICKS);
		if(blogs!=null&&blogs.size()>0){
			return blogs;
		}
		else{
			blogs=find("select * from blog order by click_times desc limit 0,6");
			Redis.use(RedisUtils.REDIS_CACHE_NAME).set(RedisUtils.REDIS_MOST_CLICKS,blogs);
			Redis.use(RedisUtils.REDIS_CACHE_NAME).expire(RedisUtils.REDIS_MOST_CLICKS, 60*60*24);
			return blogs;
		}
		
	}
	//评论量最多的文章
	public List<Blog> mostCommentTimes(){
		List<Blog> blogs=Redis.use(RedisUtils.REDIS_CACHE_NAME).get(RedisUtils.REDIS_MOST_COMMENTS);
		if(blogs!=null&&blogs.size()>0){
			return blogs;
		}
		else{
			blogs=find( "select * from blog order by comment_times desc limit 0, 6");
			Redis.use(RedisUtils.REDIS_CACHE_NAME).set(RedisUtils.REDIS_MOST_COMMENTS,blogs);
			Redis.use(RedisUtils.REDIS_CACHE_NAME).expire(RedisUtils.REDIS_MOST_COMMENTS, 60*60*24);
			return blogs;
		}
	}
	public Page<Blog> searchByType(int para,int pageNum) {
		Page<Blog> page=
			paginate(pageNum,8,"select * ","from blog  where type="+para+" order by create_time desc ");
			Redis.use(RedisUtils.REDIS_CACHE_NAME).set(RedisUtils.REDIS_TYPE_+para,page);
			return page;
		
	}

	
}

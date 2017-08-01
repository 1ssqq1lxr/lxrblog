package com.it.jfinal.model;

import java.util.List;

import com.it.jfinal.controller.AlbumController;
import com.it.jfinal.utils.RedisUtils;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Redis;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月2日下午8:23:43 
	*@version:1.0 
	*@parameter: 轮展示图片
	*@since: 
	*@return: 
*/
public class Album extends Model<Album>{

	public static Album dao=new Album();
//	分页查询轮播图片
	public List<Album> getSome(){
		List<Album>  albums=Redis.use(RedisUtils.REDIS_CACHE_NAME).get(RedisUtils.REDIS_ALBUMS);
		if(albums!=null&&albums.size()>0){
			return albums;
		}else{
			albums =find("select * from album order by create_time desc limit 0,5");
			Redis.use(RedisUtils.REDIS_CACHE_NAME).set(RedisUtils.REDIS_ALBUMS, albums)	;
			return  albums;
					
		}
	
	}
	public Page<Album> paginateAl(Integer pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from album order by create_time desc");
		
	}	

}

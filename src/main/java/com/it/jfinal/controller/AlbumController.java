package com.it.jfinal.controller;

import java.util.Date;
import java.util.List;

import com.it.jfinal.interceptor.BlogValidator;
import com.it.jfinal.model.Album;
import com.it.jfinal.utils.RedisUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Redis;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月2日下午8:24:46 
	*@version:1.0 
	*@parameter: 相册
	*@since: 
	*@return: 
*/

public class AlbumController extends Controller {
//	 @Before(BlogValidator.class)
		public void add(){
			  Album album = getModel(Album.class,"album");
		        album.set("create_time",new Date());
		        album.save();
		        Page<Album> page = Album.dao.paginateAl(getParaToInt(0, 1), 16);
		        int size = (page.getList().size());
		        setAttr("albumPage", page);
		        int finalSize = (size/4+1)*4;
		        setAttr("finalSize", finalSize);
//		        清除缓存
		    	Redis.use(RedisUtils.REDIS_CACHE_NAME).del(RedisUtils.REDIS_ALBUMS)	;
		        render("album.html");

		}
		public void albumHtml(){
				Page<Album> page=Album.dao.paginateAl(getParaToInt(0,1), 16);
		        int size = (page.getList().size());
		        setAttr("albumPage", page);
		        int finalSize = (size/4+1)*4;
		        setAttr("finalSize", finalSize);
		        render("album.html");
		}
}

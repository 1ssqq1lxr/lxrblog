package com.it.jfinal.common;

import com.it.jfinal.controller.AlbumController;
import com.it.jfinal.controller.BlogController;
import com.it.jfinal.controller.CommentController;
import com.it.jfinal.handlers.PicHandler;
import com.it.jfinal.model.Album;
import com.it.jfinal.model.Blog;
import com.it.jfinal.model.Comment;
import com.it.jfinal.model.Info;
import com.it.jfinal.utils.RedisUtils;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;

/** *@author:作者 :
	*@E-mail：
	*@date:创建时间：2016年9月2日下午7:05:49 
	*@version:1.0 
	*@parameter: 
	*@since: 
	*@return: 
*/
public class BaseConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("config.properties");
		me.setBaseUploadPath("E:/workspace/lxrBlog/src/main/webapp//upload");
		me.setDevMode(true);//开发者模式，打印了调试信息
		me.setViewType(ViewType.FREE_MARKER);
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("base"));
	}

	@Override
	public void configInterceptor(Interceptors me) {
			
		
	}

	@Override
	public void configPlugin(Plugins me) {
//		阿里巴巴连接池
		DruidPlugin druidPlugin=new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));
		me.add(druidPlugin);

		/* redis缓存*/	
		RedisPlugin redisPlugin=new RedisPlugin(RedisUtils.REDIS_CACHE_NAME,RedisUtils.REDIS_HOST , RedisUtils.REDIS_PORT, RedisUtils.REDIS_PASSWORD);
		me.add(redisPlugin);
		
		
//		配置activeRecordPlugin
		ActiveRecordPlugin activeRecordPlugin=new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setShowSql(true);
		me.add(activeRecordPlugin);
//		可以添加参数主键Id
		activeRecordPlugin.addMapping("blog", Blog.class);
		activeRecordPlugin.addMapping("album", Album.class);
		activeRecordPlugin.addMapping("comment", Comment.class);
		activeRecordPlugin.addMapping("info", Info.class);
	}

	public void configRoute(Routes me) {
//			设置路由
		me.add("/", BlogController.class,"/blog");
		me.add("/comment", CommentController.class,"/blog");
		me.add("/album",AlbumController.class,"/blog");
	}

}

package com.spiderdt.common.notice.common;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

public class Jredis {

	private static Logger log = LoggerFactory.getLogger(Jredis.class);

	private static JedisPool pool = null;
	private static JedisSentinelPool spool = null;
	private static Jredis jredis = getInstance();

	public static synchronized Jredis getInstance(){
		if( null == jredis){
			jredis = new Jredis();
		}
		return jredis;
	}

	/**
	 * 建立连接池 真实环境，一般把配置参数缺抽取出来。
	 * 
	 */
	private static void createJedisPool() {
		// 建立连接池配置参数
		JedisPoolConfig config = new  JedisPoolConfig();
		// 设置最大连接数,和系统句柄有关
		config.setMaxTotal(1024);
		// 设置最大阻塞时间，是毫秒数milliseconds
		config.setMaxWaitMillis(1000);
		// 设置空间连接
		config.setMaxIdle(10);
		//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		config.setBlockWhenExhausted(true);
		//最小空闲连接数, 默认0
		config.setMinIdle(1);
		//在获取连接的时候检查有效性, 默认false
		config.setTestOnBorrow(true);
		//在空闲时检查有效性, 默认false
		config.setTestWhileIdle(false);
		//在还给pool时，是否提前进行validate操作
		config.setTestOnReturn(true);
		// 创建连接池
		pool = new JedisPool(config, "127.0.0.1", 6379,1000);
		log.info("create jedis pool:"+pool.toString());
	}
	private static void createSentinelPool() {
		// 建立连接池配置参数
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		// 设置最大连接数,和系统句柄有关
		config.setMaxTotal(1024);
		// 设置最大阻塞时间，记住是毫秒数milliseconds
		config.setMaxWaitMillis(1000);
		// 设置空间连接
		config.setMaxIdle(10);
		//config.setTestOnBorrow(true);
		//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		config.setBlockWhenExhausted(true);
		// 创建连接池
		Set<String> sentinels = new HashSet<String>();
        sentinels.add("192.168.180.50:26379");
		sentinels.add("192.168.180.50:26380");
		sentinels.add("192.168.180.50:26381");  
		spool = new JedisSentinelPool("mymaster",sentinels,config,1000);
		log.info("create jedis pool:"+spool.toString());
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (pool == null){
			createJedisPool();
		}
	}
	private static synchronized void spoolInit() {
		if (spool == null){
			createSentinelPool();
		}
	}

	/**
	 * 获取一个jedis 对象
	 * 
	 * @return
	 */
	public static Jedis getJedis() {
		if (pool == null)
			poolInit();
		Jedis tmp = pool.getResource();
		int pactive = pool.getNumActive();
		int idel = pool.getNumIdle();
		int waiters = pool.getNumWaiters();
		log.debug("get jedis:active:"+pactive+" idel:"+idel+" waiters:"+waiters);
		return tmp;
	}
	public static Jedis getJedisByS() {
		if (spool == null)
			spoolInit();
		Jedis tmp = spool.getResource();
		HostAndPort hp = spool.getCurrentHostMaster();
		int port = hp.getPort();
		String host = hp.getHost();
		String lhq = hp.getLocalHostQuietly();
		int pactive = spool.getNumActive();
		int idel = spool.getNumIdle();
		int waiters = spool.getNumWaiters();
		log.debug("get jedis:active:"+pactive+" idel:"+idel+" waiters:"+waiters+" port"+port
				+" host:"+host+" lhq:"+lhq);
		return tmp;
	}

	public static void returnRes(Jedis jedis) {
	    returnRes(jedis,false);
    }
	/**
	 * 归还一个连接
	 * 
	 * @param jedis
	 */
	public static void returnRes(Jedis jedis,Boolean connectionBroken) {
		if(jedis != null){
			try{
				if(connectionBroken == true){
					pool.returnBrokenResource(jedis);
				}else{
					pool.returnResource(jedis);
				}
			}catch(Exception ee){
				log.error("return res error:"+ee.getMessage());
			}
			log.info("return res"+jedis.toString());
		}
	}
	public static void returnResByS(Jedis jedis) {
		if(jedis != null){
			spool.returnResource(jedis);
			log.info("return res"+jedis.toString());
		}
	}

	    
}

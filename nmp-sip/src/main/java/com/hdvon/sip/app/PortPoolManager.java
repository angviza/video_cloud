package com.hdvon.sip.app;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;


/**
 *  
* <p>Title: PortPoolManager.java</p>  
* <p>Description:sip客户端端口连接池管理类 </p>  
* <p>Copyright: 广州弘度信息科技有限公司 版权所有(C) 2018</p>  
* @author wanshaojian  
* @date 2018年8月30日  
* @version 1.0
 */
public class PortPoolManager {
	/**
	 *  连接池存放
	 */
	private static BlockingQueue<Integer> queue =  new LinkedBlockingQueue<Integer>(1000);
    
	private static final int startProt = 5600;
	private static final int pools_size = 1000;
    private PortPoolManager(){
        init();
    }

    /**
     *  单例实现
     * @return
     */
    public static PortPoolManager getInstance(){
        return Singtonle.instance;
    }
    
    private static class Singtonle {
        private static PortPoolManager instance =  new PortPoolManager();
    }
    
    /**
     *  初始化所有的连接池
     */
    private void init(){
        for(int i =0;i<pools_size;i++){
            int prot = startProt + i;
            try {
                queue.put(prot);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }            
        }
    }



    /**
     *  关闭，回收端口
     * @param prot
     */
    public void destroy(int port){
        try {
            queue.put(port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  
    }

    /**
     *  获得端口
     * @return
     */
    public Integer getPool(){
        return queue.poll();
    }

    
    public static void main(String[] args) {
    	PortPoolManager manager = PortPoolManager.getInstance();
    	
    	
    	ExecutorService executorService = new ThreadPoolExecutor(10,100,30,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1000),Executors.defaultThreadFactory(), new  ThreadPoolExecutor.AbortPolicy());
		for(int i =0;i<50;i++) {
			executorService.submit(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int port = manager.getPool();
					System.out.println("获取端口名称为："+port);
					System.out.println("获取端口池大小："+JSON.toJSONString(queue.size()));
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					manager.destroy(port);
					
					System.out.println("获取端口池大小："+JSON.toJSONString(queue.size()));
				}
			});
		}
		
	    /**
	     * 如果不再需要新任务，请适当关闭executorService并拒绝新任务
	     */
	    executorService.shutdown();
	}

}

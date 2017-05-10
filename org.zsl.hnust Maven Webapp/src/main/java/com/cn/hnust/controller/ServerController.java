package com.cn.hnust.controller;


import com.cn.hnust.utils.CloseUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 服务端，用于接收xml文件并进行解析
 * 
 * @author shang-xiaobin
 * @since 2016-8-9
 */
@Controller
public class ServerController {
	
	
	/**
	 * 日志记录
	 */
	private static final Logger logger = Logger.getLogger(ServerController.class);

	private final ServerSocket serverSocket;
	
	private static boolean flag = true;

	/**
	 * 线程池
	 */
	private ExecutorService executorService;
	
	/**
	 * 单个cpu线程池大小
	 */
	private final int POOL_SIZE = 10;

	/**
	 * 初始化serverSocket，以及线程池
	 * @author shang-xiaobin
	 * @param port
	 * @throws IOException
	 */
	public ServerController(int port) throws IOException {

		this.serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool
				(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
		logger.info(("监听服务器启动，端口号：" + port));
	}

	public static void main(String[] args) {
		StartService();
		
	}
	
	/**
	 * 启动服务
	 */
	public static void StartService()  {
		try {
			new ServerController(2100).service();
		} catch (Exception e) {
			logger.error("服务启动失败！" + e);
		}
	}
	
	/**
	 * 关闭服务
	 */
	public static void CloseService() {
		flag = false;
	}

	
	public void service() {
		try {
			while (flag) {
				Socket clientSocket =null;
				try {
					// 接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
					clientSocket = serverSocket.accept();
					// 启动一个子线程，执行SocketHandler的run()方法
					executorService.execute(new SocketHandler(clientSocket));
				} catch (Exception e) {
					logger.error("子线程开启失败" + e);
				}
			}
		}  finally {
			CloseUtils.closeServerSocket(serverSocket,logger);
		}
	}
}

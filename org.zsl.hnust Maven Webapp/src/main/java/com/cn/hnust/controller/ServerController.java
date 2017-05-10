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
 * ����ˣ����ڽ���xml�ļ������н���
 * 
 * @author shang-xiaobin
 * @since 2016-8-9
 */
@Controller
public class ServerController {
	
	
	/**
	 * ��־��¼
	 */
	private static final Logger logger = Logger.getLogger(ServerController.class);

	private final ServerSocket serverSocket;
	
	private static boolean flag = true;

	/**
	 * �̳߳�
	 */
	private ExecutorService executorService;
	
	/**
	 * ����cpu�̳߳ش�С
	 */
	private final int POOL_SIZE = 10;

	/**
	 * ��ʼ��serverSocket���Լ��̳߳�
	 * @author shang-xiaobin
	 * @param port
	 * @throws IOException
	 */
	public ServerController(int port) throws IOException {

		this.serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool
				(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
		logger.info(("�����������������˿ںţ�" + port));
	}

	public static void main(String[] args) {
		StartService();
		
	}
	
	/**
	 * ��������
	 */
	public static void StartService()  {
		try {
			new ServerController(2100).service();
		} catch (Exception e) {
			logger.error("��������ʧ�ܣ�" + e);
		}
	}
	
	/**
	 * �رշ���
	 */
	public static void CloseService() {
		flag = false;
	}

	
	public void service() {
		try {
			while (flag) {
				Socket clientSocket =null;
				try {
					// ���տͻ�����,ֻҪ�ͻ�����������,�ͻᴥ��accept();�Ӷ���������
					clientSocket = serverSocket.accept();
					// ����һ�����̣߳�ִ��SocketHandler��run()����
					executorService.execute(new SocketHandler(clientSocket));
				} catch (Exception e) {
					logger.error("���߳̿���ʧ��" + e);
				}
			}
		}  finally {
			CloseUtils.closeServerSocket(serverSocket,logger);
		}
	}
}

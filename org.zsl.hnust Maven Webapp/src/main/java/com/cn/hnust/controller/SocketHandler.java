package com.cn.hnust.controller;


import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.Socket;

import java.util.List;

import com.cn.hnust.utils.CloseUtils;

/**
 * 处理socket请求
 * @author Administrator
 *
 */
@Controller
public class SocketHandler implements Runnable{
	
	/**
	 * IOC容器管理
	 */
	private static ApplicationContext ctx = null;
	
	/**
	 * 日志管理
	 */
	private static final Logger logger = Logger.getLogger(SocketHandler.class);

	
	private Socket socket = null;
	
	static {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}


	
	public SocketHandler() {}
	public SocketHandler (Socket socket) {
		this.socket = socket;
	}

	public void run() {
		InputStream is = null;
		OutputStream os = null;	
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			// 接受xml报文并解析
			receiveXmlMess(socket, is);
			// 发送响应报文
			sendReturnXml(socket, os);
		} catch (IOException e) {
			logger.error("获取流异常" + e);
		} catch (Exception e) { 
			logger.error("服务端异常" + e);
		} finally {
			CloseUtils.closeInputStream(is, logger);
			CloseUtils.closeOutputStream(os, logger);
			CloseUtils.closeSocket(socket, logger);
		}
	}
	
	/**
	 * 服务端获取响应报文，并发送给客户端
	 * 
	 * @author shang-xiaobin
	 * @since 2016-08-10
	 * @param s
	 * @param os
	 * @throws Exception 
	 */
	private void sendReturnXml(Socket s, OutputStream os) throws Exception {
		FileInputStream fis = null;
		try {
			File file = new File("d:/socketXml/sendServerXml/reviceReturn.xml");
			fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			os.write(b);
			logger.debug("服务端响应客户端报文：" + new String(b));
			// 当输出完毕时，切断输出流
			s.shutdownOutput();
		} catch (Exception e) {	
			logger.error("发送响应报文失败！" + e);
			throw new Exception(e);
		} finally {
			CloseUtils.closeInputStream(fis, logger);
			
		}
	}

	
	
	/**
	 * 获取客户端发送的xml报文,并进行解析
	 * 
	 * @author shang-xiaobin
	 * @since 2016-08-10
	 * @param s
	 * @param is
	 * @throws Exception 
	 */
	private void receiveXmlMess(Socket s, InputStream is) throws Exception {
		InputStream is2 = null;
		try {
			// 由于dom4j解析会关闭输入流，从而导致socket关闭，故创建新的输入流
			is2 = createInputStream(is);
			// 创建解析器实例
			SAXReader saxReader = new SAXReader();
			// 解析输入流获得DOM对象
			Document document = saxReader.read(is2);
			// 获取根节点
			Element rootElement = document.getRootElement();
			// 获取person节点及子节点值，组装javaBean,调用dao方法持久化
			List<Element> elements = rootElement.elements("PERSON");


		} catch (Exception e) {
			logger.error("解析报文异常！",e);
			throw new Exception(e);
		} finally {
			CloseUtils.closeInputStream(is2, logger);
		}
	}

	/**
	 * @author shang_xiaobin
	 * @param is
	 * @return
	 * @since 2016-08-16
	 */
	private InputStream createInputStream(InputStream is) {
		// 创建临时文件
		File file = new File("temp.xml");
		InputStream is2 = null;
		OutputStream os = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			byte[] b = new byte[is.available()];
			is.read(b);
			os.write(b);
			logger.debug("服务端接受客户端报文：" + new String(b));
			// 读取临时报文文件进行解析
			is2 = new FileInputStream(file);
		} catch (Exception e) {
			logger.error(e);
		}
		return is2;
	}
	
	
}
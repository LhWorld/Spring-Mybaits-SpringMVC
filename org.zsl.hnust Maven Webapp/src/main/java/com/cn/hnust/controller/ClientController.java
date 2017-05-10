package com.cn.hnust.controller;


import com.cn.hnust.utils.CloseUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 客户端程序，向服务端发送报文，接收服务端响应报文
 * @author shang-xiaobin
 * @since 2016-08-12
 */
@Controller
public class ClientController {

	private static final Logger logger = Logger.getLogger(ClientController.class);




	public static void main(String[] args) {
		ClientController c = new ClientController();
		c.sendXML();
	}
	

	

	/**
	 * 向服务端发送xml报文，并获取响应
	 * @author shang-xiaobin
	 * @since 2016-08-12
	 */
	public void sendXML() {
		Socket s = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			s = new Socket(InetAddress.getByName("127.0.0.1"),2100);
			String fileName = "d:/socketXml/sendClientXml/send.xml";
			os = s.getOutputStream();
			is = s.getInputStream();
			//发送xml报文
			sendXmlMess(s,fileName,os);
			//接受响应xml报文
			receiveReturnXml(s,is);
		}catch (IOException e) {
			logger.error("获取流异常" + e);
		} catch (Exception e) { 
			logger.error("客户端异常！" + e);
		}  finally {
			CloseUtils.closeInputStream(is, logger);
			CloseUtils.closeOutputStream(os, logger);
			CloseUtils.closeSocket(s, logger);
		}
	}


	/**
	 * 接受服务端响应的xml报文，并存储到xml文件中去
	 * @author shang-xiaobin
	 * @since 2016-08-10
	 * @param s
	 * @param is 
	 * @throws Exception 
	 */
	private void receiveReturnXml(Socket s, InputStream is) throws Exception {
		FileOutputStream fos = null;
		try {
			String path = "d:/socketXml/returnXml/" ;
			//注:文件格式中不能有":"号
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hhmmss");
			String date = sdf.format(new Date());
			String fileName = InetAddress.getLocalHost().getHostName() + "-" + date + ".xml";
			File file = new File(path,fileName);
			//判断文件目录是否存在，不存在则创建
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
				file.createNewFile();
			}else {
				if(!file.exists()) {
					file.createNewFile();
				}
			}
			fos = new FileOutputStream(file);
			//等待服务端响应报文
			while(true) {
				if(is.available() > 0) {
					byte[] b = new byte[is.available()];
					//服务端响应报文写入到文件中去
					is.read(b);
					fos.write(b);
					break;
				}	
			}
		} catch (Exception e) {
			logger.error("接收响应报文时出现异常！",e);
			throw new Exception(e);
		} finally {
			CloseUtils.closeOutputStream(fos, logger);
		}
	}

	
	/**
	 * 向服务端发送xml报文
	 * @author shang-xiaobin
	 * @since 2016-08-10
	 * @param s
	 * @param fileName
	 * @param os 
	 * @throws Exception 
	 */
	private void sendXmlMess(Socket s, String fileName, OutputStream os) throws Exception {
		FileInputStream fis  = null;
		try {
			File file = new File(fileName);
			fis = new FileInputStream(file);
			byte[] b =  new byte[fis.available()];
			fis.read(b);
			os.write(b);
			s.shutdownOutput();		
		} catch (Exception e) {
			logger.error("发送xml报文异常 " + e);
			throw new Exception(e);
		} finally {
			CloseUtils.closeInputStream(fis, logger);	
		}
	}
}

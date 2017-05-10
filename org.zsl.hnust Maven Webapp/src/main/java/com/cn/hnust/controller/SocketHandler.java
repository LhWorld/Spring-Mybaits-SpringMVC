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
 * ����socket����
 * @author Administrator
 *
 */
@Controller
public class SocketHandler implements Runnable{
	
	/**
	 * IOC��������
	 */
	private static ApplicationContext ctx = null;
	
	/**
	 * ��־����
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
			// ����xml���Ĳ�����
			receiveXmlMess(socket, is);
			// ������Ӧ����
			sendReturnXml(socket, os);
		} catch (IOException e) {
			logger.error("��ȡ���쳣" + e);
		} catch (Exception e) { 
			logger.error("������쳣" + e);
		} finally {
			CloseUtils.closeInputStream(is, logger);
			CloseUtils.closeOutputStream(os, logger);
			CloseUtils.closeSocket(socket, logger);
		}
	}
	
	/**
	 * ����˻�ȡ��Ӧ���ģ������͸��ͻ���
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
			logger.debug("�������Ӧ�ͻ��˱��ģ�" + new String(b));
			// ��������ʱ���ж������
			s.shutdownOutput();
		} catch (Exception e) {	
			logger.error("������Ӧ����ʧ�ܣ�" + e);
			throw new Exception(e);
		} finally {
			CloseUtils.closeInputStream(fis, logger);
			
		}
	}

	
	
	/**
	 * ��ȡ�ͻ��˷��͵�xml����,�����н���
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
			// ����dom4j������ر����������Ӷ�����socket�رգ��ʴ����µ�������
			is2 = createInputStream(is);
			// ����������ʵ��
			SAXReader saxReader = new SAXReader();
			// �������������DOM����
			Document document = saxReader.read(is2);
			// ��ȡ���ڵ�
			Element rootElement = document.getRootElement();
			// ��ȡperson�ڵ㼰�ӽڵ�ֵ����װjavaBean,����dao�����־û�
			List<Element> elements = rootElement.elements("PERSON");


		} catch (Exception e) {
			logger.error("���������쳣��",e);
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
		// ������ʱ�ļ�
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
			logger.debug("����˽��ܿͻ��˱��ģ�" + new String(b));
			// ��ȡ��ʱ�����ļ����н���
			is2 = new FileInputStream(file);
		} catch (Exception e) {
			logger.error(e);
		}
		return is2;
	}
	
	
}
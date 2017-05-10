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
 * �ͻ��˳��������˷��ͱ��ģ����շ������Ӧ����
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
	 * �����˷���xml���ģ�����ȡ��Ӧ
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
			//����xml����
			sendXmlMess(s,fileName,os);
			//������Ӧxml����
			receiveReturnXml(s,is);
		}catch (IOException e) {
			logger.error("��ȡ���쳣" + e);
		} catch (Exception e) { 
			logger.error("�ͻ����쳣��" + e);
		}  finally {
			CloseUtils.closeInputStream(is, logger);
			CloseUtils.closeOutputStream(os, logger);
			CloseUtils.closeSocket(s, logger);
		}
	}


	/**
	 * ���ܷ������Ӧ��xml���ģ����洢��xml�ļ���ȥ
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
			//ע:�ļ���ʽ�в�����":"��
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hhmmss");
			String date = sdf.format(new Date());
			String fileName = InetAddress.getLocalHost().getHostName() + "-" + date + ".xml";
			File file = new File(path,fileName);
			//�ж��ļ�Ŀ¼�Ƿ���ڣ��������򴴽�
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
				file.createNewFile();
			}else {
				if(!file.exists()) {
					file.createNewFile();
				}
			}
			fos = new FileOutputStream(file);
			//�ȴ��������Ӧ����
			while(true) {
				if(is.available() > 0) {
					byte[] b = new byte[is.available()];
					//�������Ӧ����д�뵽�ļ���ȥ
					is.read(b);
					fos.write(b);
					break;
				}	
			}
		} catch (Exception e) {
			logger.error("������Ӧ����ʱ�����쳣��",e);
			throw new Exception(e);
		} finally {
			CloseUtils.closeOutputStream(fos, logger);
		}
	}

	
	/**
	 * �����˷���xml����
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
			logger.error("����xml�����쳣 " + e);
			throw new Exception(e);
		} finally {
			CloseUtils.closeInputStream(fis, logger);	
		}
	}
}

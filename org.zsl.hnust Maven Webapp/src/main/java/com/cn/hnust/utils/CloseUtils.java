package com.cn.hnust.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 稀缺资源关闭工具类，
 * @author shang-xiaobin
 * @since 2016-08-19
 *
 */
public class CloseUtils {
	
	
	/**
	 * 输入流关闭方法
	 * @author shang-xiaobin
	 * @since 2016-08-19
	 * @param is
	 * @param logger
	 */
	public static void  closeInputStream(InputStream is,Logger logger) {
		if(is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("输入流关闭异常！" + e);
			}
		}
		
	}
	
	/**
	 * 输出流关闭
	 * @author shang-xiaobin
	 * @since 2016-08-19
	 * @param os
	 * @param logger
	 */
	public static void closeOutputStream(OutputStream os,Logger logger) {
		if(os != null) {
			try {
				os.close();
			} catch (IOException e) {
				logger.error("输出流关闭异常！" + e);
			}
		}	
	}
	
	
	/**
	 * 关闭socket
	 * @author shang-xiaobin
	 * @since 2016-08-19
	 * @param s
	 * @param logger
	 */
	public static void closeSocket(Socket s,Logger logger) {
		if(s != null) {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("socket关闭异常！" + e);
			}
		}
		
	}

	/**
	 * 关闭ServerSocket
	 * @author shang-xiaobin
	 * @since 2016-08-19
	 * @param logger
	 */
	public static void closeServerSocket(ServerSocket serverSocket,
			Logger logger) {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		
	}

}

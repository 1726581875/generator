package zhangyu.fool.generate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 目录操作工具类
 * @author xmz
 * 2020年9月14日
 *
 */
public class FileUtil {

	public static Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	
	/**
	 * 
	 * 如果目录不存在，创建目录
	 */
	public static void mkdirs(String dirPath){		
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
			/*log.info("创建目录  [{}]",dirPath);*/
		}	
	}

	/**
	 * 如果目录不存在则创建目录
	 * @param destPath
	 */
	public static void checkAndCreateDir(String destPath) {
		File destDir = new File(destPath);
		if (!destDir.exists()) {
			try {
				log.debug("目录{} 不存在", destPath);
				destDir.mkdirs();
				log.debug("已创建目录{} ", destPath);
			}catch (Exception e){
				log.debug("创建目录{} 出现异常",destPath,e);
			}
		}
	}
	
}

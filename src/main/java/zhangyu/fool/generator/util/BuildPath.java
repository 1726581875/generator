package zhangyu.fool.generator.util;

import java.io.File;

/**
 * @author xmz
 * @date 2020/09/25
 * 帮助构建目录路径
 * 目录以File.separator分割
 */
public class BuildPath {
	
	/**
	 * 
	 * @param paths 可变成参数，传入要拼接的目录路径
	 * @return 完整目录路径
	 */
	public static String buildDir(String... paths){
		StringBuilder dirPath= new StringBuilder();
		for (int i = 0; i < paths.length; i++) {
			dirPath.append(paths[i]);
			dirPath.append(File.separator);			
		}
		/* 去重复的分割符// */
		int index;
		while((index = dirPath.indexOf(File.separator + File.separator)) != -1){
			dirPath.replace(index, index +2, File.separator);
		}
		String string = dirPath.toString();
		return string;
	}
	
	/**
	 * 拼接包名
	 * @param names
	 * @return
	 */
	public static String buildPackageName(String... names){			
		StringBuilder packageName= new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			packageName.append(names[i]);
			if(i != names.length - 1) {
				packageName.append('.');
			}
		}		
		return packageName.toString();
	}
	
	/**
	 * 包名转目录名
	 * aa.bb.cc => /aa/bb/cc/
	 * @param packageName
	 * @return
	 */
	public static String converToDir(String packageName){			
		StringBuilder dirPath= new StringBuilder(packageName);
		int index;
		while((index = dirPath.indexOf(".")) != -1){
			dirPath.replace(index, index + 1, File.separator);
		}
		dirPath.insert(0, File.separator);
		dirPath.append(File.separator);	
		return dirPath.toString();
	}
	
	
	public static void main(String[] args) {
		String name = buildDir("aaa","bbb","ccc").toString();
		System.out.println(name);
		
		String packageName = buildPackageName("aaa","bbb","ccc").toString();
		System.out.println(packageName);
		
		System.out.println(converToDir("aaa.bbb.ccc.ddd"));
	}

}

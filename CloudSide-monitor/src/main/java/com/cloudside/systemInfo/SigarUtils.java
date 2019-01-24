package com.cloudside.systemInfo;

import java.io.File;
import java.net.InetAddress;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

public class SigarUtils {

	private static Sigar sigar;
	
	public static void main(String... args) {
		try {
			Sigar sigar = getInstance();
			final String filePath = "D:/";// classPath().getResource("").getPath();
			double cpuUsedPerc = sigar.getCpuPerc().getCombined();// cpu
			String cpuUsedPercStr = String.format("%.2f", cpuUsedPerc) + " %";
			CpuPerc[] cpuPercs = sigar.getCpuPercList();
			double cpuUsedPer = 0;
			for (CpuPerc cpuPerc : cpuPercs) {
				cpuUsedPer += cpuPerc.getCombined();
			}

			String cpuUsedPercStr1 = String.format("%.2f", cpuUsedPer) + " %";

			double memUsed = sigar.getMem().getActualUsed();// mem
			double memTotal = sigar.getMem().getTotal();
			double memUsedPerc = sigar.getMem().getUsedPercent();
			String memUsedStr = String.format("%.2f", memUsed / 1024 / 1024 / 1024) + "GB";// mem  to string GB
			String memTotalStr = String.format("%.2f", memTotal / 1024 / 1024 / 1024) + "GB";
			String memUsedPercStr = String.format("%.2f", memUsedPerc) + " %";

			double diskUsed = sigar.getFileSystemUsage(filePath).getUsed();// disk
			double diskTotal = sigar.getFileSystemUsage(filePath).getTotal();
			double diskUsedPerc = sigar.getFileSystemUsage(filePath).getUsePercent();
			String diskUsedStr = String.format("%.2f", diskUsed / 1024 / 1024) + "GB";// disk to String GB
			String diskTotalStr = String.format("%.2f", diskTotal / 1024 / 1024) + "GB";
			String diskUsedPercStr = String.format("%.2f", diskUsedPerc * 100) + " %";

			String fqdn = sigar.getFQDN();// FQDN
			System.out.println("CPU 占比:" + cpuUsedPercStr);
			System.out.println("CPU 占比:" + cpuUsedPercStr1);
			System.out.println("内存总计:" + memTotalStr);
			System.out.println("内存使用 占比:" + memUsedStr);
			System.out.println("内存使用 占比:" + memUsedPercStr);
			System.out.println("磁盘总计:" + diskTotalStr);
			System.out.println("磁盘使用:" + diskUsedStr);
			System.out.println("磁盘使用 占比:" + diskUsedPercStr);
			
			System.out.println("全称域名(FQDN):"+fqdn);
			System.out.println("主机IP地址:"+InetAddress.getLocalHost().getHostAddress());

			Runtime r = Runtime.getRuntime();
			System.out.println("JVM的总内存:" + String.format("%.2f", (double)r.totalMemory() / 1024 / 1024) + "GB");
			System.out.println("JVM的剩余内存:" + String.format("%.2f", (double)r.freeMemory() / 1024 / 1024) + "GB");
			System.out.println("JVM的内存占比:" + String.format("%.2f", (double)(r.totalMemory()-r.freeMemory())/r.totalMemory()) + "%");
			System.out.println("JVM的处理器个数:" + r.availableProcessors());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Sigar getInstance() {
		if (null == sigar) {
			sigar = initSigar();
		}
		return sigar;
	}

	private static Sigar initSigar() {
		try {
			// 此处只为得到依赖库文件的目录，可根据实际项目自定义
			File classPath = new File(classPath().getResource("/").getPath() + "/dll");
			System.out.println(classPath);
			String path = System.getProperty("java.library.path");
			String sigarLibPath = classPath.getCanonicalPath();
			// 为防止java.library.path重复加，此处判断了一下
			if (!path.contains(sigarLibPath)) {
				if (isOSWin()) {
					path += ";" + sigarLibPath;
				} else {
					path += ":" + sigarLibPath;
				}
				System.out.println(path);
				System.setProperty("java.library.path", path);
			}
			return new Sigar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private static boolean isOSWin() {// OS 版本判断
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			return true;
		} else
			return false;
	}

	private static Class classPath() {
		return new SecurityManager() {}.getClass();
	}
}

package com.cloudside.terminal.systemInfo;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SigarUtils {

	private static Logger log = LoggerFactory.getLogger(SigarUtils.class);
	
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

			String cpuUsedPercStr1 = String.format("%.2f", getCpuRatioForWindows()) + " %";

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
			String path = System.getProperty("java.library.path");
			String sigarLibPath = classPath.getCanonicalPath();
			// 为防止java.library.path重复加，此处判断了一下
			if (!path.contains(sigarLibPath)) {
				if (isOSWin()) {
					path += ";" + sigarLibPath;
				} else {
					path += ":" + sigarLibPath;
				}
				//System.out.println(System.getProperty("java.home"));
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
	
	
	//可以设置长些，防止读到运行此次系统检查时的cpu占用率，就不准了   
    private static final int CPUTIME = 5000;   
  
    private static final int PERCENT = 100;   
  
    private static final int FAULTLENGTH = 10;
    
	/** *//**  
     * 获得CPU使用率.  
     * @return 返回cpu使用率  
     * @author amg     * Creation date: 2008-4-25 - 下午06:05:11  
     */  
    private static double getCpuRatioForWindows() {   
        try {   
            String procCmd = System.getenv("windir")   
                    + "//system32//wbem//wmic.exe process get Caption,CommandLine,"  
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";   
            // 取进程信息   
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));   
            Thread.sleep(CPUTIME);   
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));   
            if (c0 != null && c1 != null) {   
                long idletime = c1[0] - c0[0];   
                long busytime = c1[1] - c0[1];   
                return Double.valueOf(   
                        PERCENT * (busytime) / (busytime + idletime))   
                        .doubleValue();   
            } else {   
                return 0.0;   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
            return 0.0;   
        }   
    }   
  
    /** *//**  
     * 读取CPU信息.  
     * @param proc  
     * @return  
     * @author amg     * Creation date: 2008-4-25 - 下午06:10:14  
     */  
    private static long[] readCpu(final Process proc) {   
        long[] retn = new long[2];   
        try {   
            proc.getOutputStream().close();   
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());   
            LineNumberReader input = new LineNumberReader(ir);   
            String line = input.readLine();   
            if (line == null || line.length() < FAULTLENGTH) {   
                return null;   
            }   
            int capidx = line.indexOf("Caption");   
            int cmdidx = line.indexOf("CommandLine");   
            int rocidx = line.indexOf("ReadOperationCount");   
            int umtidx = line.indexOf("UserModeTime");   
            int kmtidx = line.indexOf("KernelModeTime");   
            int wocidx = line.indexOf("WriteOperationCount");   
            long idletime = 0;   
            long kneltime = 0;   
            long usertime = 0;   
            while ((line = input.readLine()) != null) {   
                if (line.length() < wocidx) {   
                    continue;   
                }   
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,   
                // ThreadCount,UserModeTime,WriteOperation   
                String caption = substring(line, capidx, cmdidx - 1)   
                        .trim();   
                String cmd = substring(line, cmdidx, kmtidx - 1).trim();   
                if (cmd.indexOf("wmic.exe") >= 0) {   
                    continue;   
                }   
                // log.info("line="+line);   
                if (caption.equals("System Idle Process")   
                        || caption.equals("System")) {   
                    idletime += Long.valueOf(   
                            substring(line, kmtidx, rocidx - 1).trim())   
                            .longValue();   
                    idletime += Long.valueOf(   
                            substring(line, umtidx, wocidx - 1).trim())   
                            .longValue();   
                    continue;   
                }   
  
                kneltime += Long.valueOf(   
                        substring(line, kmtidx, rocidx - 1).trim())   
                        .longValue();   
                usertime += Long.valueOf(   
                        substring(line, umtidx, wocidx - 1).trim())   
                        .longValue();   
            }   
            retn[0] = idletime;   
            retn[1] = kneltime + usertime;   
            return retn;   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        } finally {   
            try {   
                proc.getInputStream().close();   
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
        }   
        return null;   
    }
    
    /** *//** 
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 
     * 包含汉字的字符串时存在隐患，现调整如下： 
     * @param src 要截取的字符串 
     * @param start_idx 开始坐标（包括该坐标) 
     * @param end_idx   截止坐标（包括该坐标） 
     * @return 
     */  
    public static String substring(String src, int start_idx, int end_idx){  
        byte[] b = src.getBytes();  
        String tgt = "";  
        for(int i=start_idx; i<=end_idx; i++){  
            tgt +=(char)b[i];  
        }  
        return tgt;  
    }
}

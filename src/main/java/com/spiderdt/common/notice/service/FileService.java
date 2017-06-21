package com.spiderdt.common.notice.service;

import com.spiderdt.common.notice.common.Jlog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.service
 * @Description:
 * @date 2017/6/20 15:03
 */
@SuppressWarnings("ALL")
@Service("fileService")
public class FileService {

    @Value("${attachment.storePath}") String attachmentStorePath;

    /**
     * 下载文件到本地
     * @param urlString 被下载的文件地址
     * @param taskFileDir 每个 task 一个文件夹
     * @param filename 本地文件名
     */

    public void download(String urlString, String taskFileDir, String filename)
    {
        try
        {
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流s
            File saveDir = new File(taskFileDir);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            OutputStream os = new FileOutputStream(taskFileDir +"/"+ filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }
        catch (MalformedURLException e)
        {
            Jlog.error(e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Jlog.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if(file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }


    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        // 如果 sPath 不以文件分隔符結尾，自動添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath += File.separator;
        }

        File dirFile = new File(sPath);
        // 如果 dir 对应的文件不存在，或者不是一个目录，则退出
        if(!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }

        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        Jlog.info("files:" + files);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                Jlog.info( "file:" + files[i]);
                flag = deleteFile(files[i].getAbsolutePath());
                if(!flag) break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if(!flag) break;
            }
        }
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

}

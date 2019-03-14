package esaas.devops.jenkins.publish.remote.ftp;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import esaas.devops.jenkins.common.Util;
import esaas.devops.jenkins.publish.remote.RemoteAddrWrapper;
import esaas.devops.jenkins.publish.remote.Transporter;

/**
 * FTP的传输实现类
 */
public class FTPTransporter implements Transporter {

    private final FTPClient client;

    public FTPTransporter(FTPClient client) {
        this.client = client; // Do not escape this!
    }

    @Override
    public void transport(File src, RemoteAddrWrapper remoteAddr) {
        // 上传
        try {
            String appRoot = remoteAddr.getRelativePath();
            upload(client, src, appRoot);
        } catch (IOException e) {
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }
    }

    private void upload(FTPClient client, File uploadFile, final String uploadPath) throws IOException {
        try (InputStream in1 = new FileInputStream(uploadFile)) {
            Util.getLogger().println("开始上传文件，上传路径：" + uploadPath + "本地上传文件路径：" + uploadFile.getAbsolutePath());
            // 首先判断目录是否已经存在
            FTPFile[] listDirectories = 
                client.listFiles(new File(uploadPath).getParent(), new FTPFileFilter(){
                    public boolean accept(FTPFile file) {
                        return file.getName().equals(new File(uploadPath).getName());
                    }
                });
            if (listDirectories.length == 0 && !client.makeDirectory(uploadPath)) {
                throw new RuntimeException("创建目录失败，目录： " + uploadPath);
            }
            if (!client.changeWorkingDirectory(uploadPath)) {
                throw new RuntimeException("切换目录失败，目录： " + uploadPath);
            }
            if (!client.storeFile(uploadFile.getName(), in1)) {
                throw new RuntimeException("上传失败，目录： " + uploadPath);
            }
            Util.getLogger().println("上传成功");
        } catch (IOException e) {
            e.printStackTrace(Util.getLogger());
            throw e;
        }
    }
}

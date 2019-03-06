package esaas.devops.jenkins.publish.remote.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

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

    private void upload(FTPClient client, File uploadFile, String uploadPath) throws IOException {
        try (InputStream in1 = new FileInputStream(uploadFile)) {
            client.makeDirectory(uploadPath); // 创建对应version的目录
            client.changeWorkingDirectory(uploadPath);
            client.storeFile(uploadFile.getName(), in1); // 开始上传文件
        } catch (IOException e) {
            e.printStackTrace(Util.getLogger());
            throw e;
        }
    }
}

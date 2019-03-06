package esaas.devops.jenkins.publish.remote.ftp;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import esaas.devops.jenkins.common.Version;
import esaas.devops.jenkins.publish.remote.RemoteAddrWrapper;
import esaas.devops.jenkins.publish.remote.Transporter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FTP的传输实现类
 */
public class FTPTransporter implements Transporter {

    private final FTPClient client;
    private final Project project;

    public FTPTransporter(FTPClient client, Project project) {
        this.project = project;
        this.client = client; // Do not escape this!
    }

    @Override
    public void transport(File src, RemoteAddrWrapper remoteAddr) {
        // 上传 
        try {
            String appRoot = remoteAddr.getRemoteWorkDir();
            upload(client, src, appRoot);
        } catch (IOException e) {
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        } finally {
            // 删除清理
            clean(client, src);
        }
    }

    private void upload(FTPClient client, File uploadFile, String appRoot) throws IOException {
        try (InputStream in1 = new FileInputStream(uploadFile); 
                InputStream in2 = new FileInputStream(uploadFile)) {
            String uploadPath = appRoot + File.separator + project.getVersion().getStr();
            client.makeDirectory(uploadPath); // 创建对应version的目录
            client.changeWorkingDirectory(uploadPath);
            client.storeFile(uploadFile.getName(), in1); // 开始上传文件

            String latestPath = appRoot + File.separator + Version.LATEST;
            FTPFile[] ftpFiles = client.listFiles(latestPath);
            if (ftpFiles == null || ftpFiles.length == 0) {
                client.makeDirectory(latestPath);
            }
            client.changeWorkingDirectory(latestPath);
            client.storeFile(uploadFile.getName(), in2);
        } catch (IOException e) {
            e.printStackTrace(Util.getLogger());
            throw e;
        }
    }

    private void clean(FTPClient client, File src) {
        try {
            if (client != null) {
                client.logout();
                client.disconnect();
            }
            src.delete();
        } catch (IOException e) {
            Util.getLogger().println("关闭FTPClient出错！");
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }
    }

    public FTPClient getClient() {
        return client;
    }

    public Project getProject() {
        return project;
    }
}

package esaas.devops.jenkins.publish;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import esaas.devops.jenkins.common.Version;
import esaas.devops.jenkins.publish.remote.RemoteAddrWrapper;
import esaas.devops.jenkins.publish.remote.Transporter;
import esaas.devops.jenkins.publish.remote.ftp.FTPTransporter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用FTP协议传输的publisher
 */
public class FTPPublisher extends AbstractPublisher {
    private FTPClient client;

    @Override
    Transporter getTransporter(Project project) {
        try {
            // 建立连接
            RemoteAddrWrapper remoteAddr = project.getRemoteAddrWapper();
            this.client = Util.getFTPClient(remoteAddr.getUrl(), // 创建一个客户端实例
                    remoteAddr.getUsername(),
                    remoteAddr.getPassword(),
                    remoteAddr.getPort());
           return new FTPTransporter(this.client, project);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    Version getNextVersion(Project project) {
        try {
            // 确认版本
            String appRoot = project.getRemoteAddrWapper().getRemoteWorkDir();
            client.changeWorkingDirectory(appRoot);
            FTPFile[] ftpFiles = client.listDirectories(appRoot);
            List<Version> versions = new ArrayList<>(ftpFiles.length);
            for (FTPFile ftpFile : ftpFiles) {
                if (!ftpFile.getName().equals(Version.LATEST)) {
                    versions.add(Version.of(ftpFile.getName()));
                }
            }
            return Util.getNextVersion(versions, project.getVersionUpdateType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

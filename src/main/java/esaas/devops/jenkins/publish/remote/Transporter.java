package esaas.devops.jenkins.publish.remote;

import java.io.File;

/**
 * 实现工程传送的功能
 */
public interface Transporter {

    void transport(File src, RemoteAddrWrapper remoteAddr);
}

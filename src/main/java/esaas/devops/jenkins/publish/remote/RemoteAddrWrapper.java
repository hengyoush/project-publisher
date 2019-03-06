package esaas.devops.jenkins.publish.remote;


/**
 * 远程路径包装类
 */
public class RemoteAddrWrapper {

    /** 远程url **/
    private final String url;
    /** 远程用户名 **/
    private final String username;
    /** 远程密码 **/
    private final String password;
    /** 远程端口号 **/
    private final Integer port;
    /** 远程传输目录 */
    private final String remoteWorkDir;

    public RemoteAddrWrapper(String url, String username, String password, Integer port, String remoteWorkDir) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.port = port;
        this.remoteWorkDir = remoteWorkDir;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPort() {
        return port;
    }

    /**
     * @return the remoteWorkDir
     */
    public String getRemoteWorkDir() {
        return remoteWorkDir;
    }

    
}

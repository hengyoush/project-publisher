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

    public RemoteAddrWrapper(String url, String username, String password, Integer port) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.port = port;
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
}

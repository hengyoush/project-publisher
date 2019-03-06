package esaas.devops.jenkins.common;

import esaas.devops.jenkins.publish.remote.RemoteAddrWrapper;
import hudson.Launcher;

import java.io.File;

public abstract class Project {
    /** 工程标识，为工程的全称，例如：efp.console.middle, efp.console.front **/
    private String projectKey;
    /**  要打包的具体工程名称， 例如：efp.console.impl, efp.console.api **/
    private String projectName;
    /** 远程发布地址 **/
    private RemoteAddrWrapper remoteAddr;
    /** jenkins工作目录下工程的根目录，如.jenkins/workspaces/efp.console.middle **/
    private File projectRoot;
    /** 打包的临时目录 **/
    private File packageRoot;
    /** 打包完成的tar文件，在打包完成后设置值 **/
    private File tar;
    /** 打包方式：普通app、docker镜像 **/
    private PackageType packageType;
    /** 远程工程存放的根目录，如：/home/piccs/apps，生成脚本时用到 **/
    private String targetProjRoot;
    /** 该工程的version，在发布时根据目前已有的version以及更新类型决定发布的version **/
    private Version version;
    /** 分为大版本更新， 中更新， 小更新 **/
    private VersionUpdateType versionUpdateType;
    /** 传输协议， 如：ftp、sftp等 **/
    private ProtocolType protocolType;
    /** 用于运行命令行的工具 */
    private Launcher launcher;

    /**
     * 子类实现该方法，返回工程类型
     */
    public abstract ProjectType getProjectType();

    public static Project of(ProjectType projectType) {
        switch (projectType) {
            case front: {
                return new FrontProject();
            }
            case middle: {
                return new MiddleProject();
            }
            case sprboot:
                default: throw new IllegalArgumentException("尚不支持该projectType： " + projectType);
        }
    }

    /**-------------------getters and setters--------------------**/

    public File getTar() {
        return tar;
    }

    public void setTar(File tar) {
        this.tar = tar;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public RemoteAddrWrapper getRemoteAddrWapper() {
        return remoteAddr;
    }

    public void setRemoteAddr(RemoteAddrWrapper remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public File getProjectRoot() {
        return projectRoot;
    }

    public void setProjectRoot(File projectRoot) {
        this.projectRoot = projectRoot;
    }

    public File getPackageRoot() {
        return packageRoot;
    }

    public void setPackageRoot(File packageRoot) {
        this.packageRoot = packageRoot;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getTargetProjRoot() {
        return targetProjRoot;
    }

    public void setTargetProjRoot(String targetProjRoot) {
        this.targetProjRoot = targetProjRoot;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public VersionUpdateType getVersionUpdateType() {
        return versionUpdateType;
    }

    public void setVersionUpdateType(VersionUpdateType versionUpdateType) {
        this.versionUpdateType = versionUpdateType;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public Launcher getLauncher() {
        return launcher;
    }
    
    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }
}

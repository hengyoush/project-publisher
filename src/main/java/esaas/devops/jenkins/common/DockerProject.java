package esaas.devops.jenkins.common;

import java.io.File;

import esaas.devops.jenkins.publish.remote.RemoteAddrWrapper;
import hudson.Launcher;

public class DockerProject extends Project {

    private Project project;
    /** 需要进行构建镜像的project名称，如efp.console.impl */
    private String targetProject;
    
    public DockerProject(Project project) {
        this.project = project;
    }

    @Override
    public ProjectType getProjectType() {
        return project.getProjectType();
    }

    /**
     * @return the targetProject
     */
    public String getTargetProject() {
        return targetProject;
    }

    /**
     * @param targetProject the targetProject to set
     */
    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }


    /** 委托给真正的project对象 */

    public File getTar() {
        return project.getTar();
    }

    public void setTar(File tar) {
        this.project.setTar(tar);
    }

    public String getProjectName() {
        return project.getProjectName();
    }

    public void setProjectName(String projectName) {
        this.project.setProjectName(projectName);
    }

    public RemoteAddrWrapper getRemoteAddrWapper() {
        return project.getRemoteAddrWapper();
    }

    public void setRemoteAddr(RemoteAddrWrapper remoteAddr) {
        this.project.setRemoteAddr(remoteAddr);
    }

    public File getProjectRoot() {
        return project.getProjectRoot();
    }

    public void setProjectRoot(File projectRoot) {
        this.project.setProjectRoot(projectRoot);
    }

    public File getPackageRoot() {
        return project.getPackageRoot();
    }

    public void setPackageRoot(File packageRoot) {
        this.project.setPackageRoot(packageRoot);
    }

    public PackageType getPackageType() {
        return project.getPackageType();
    }

    public void setPackageType(PackageType packageType) {
        this.project.setPackageType(packageType);
    }

    public String getProjectKey() {
        return project.getProjectKey();
    }

    public void setProjectKey(String projectKey) {
        this.project.setProjectKey(projectKey);
    }

    public String getTargetProjRoot() {
        return project.getTargetProjRoot();
    }

    public void setTargetProjRoot(String targetProjRoot) {
        this.project.setTargetProjRoot(targetProjRoot);
    }

    public Version getVersion() {
        return project.getVersion();
    }

    public void setVersion(Version version) {
        this.project.setVersion(version);
    }

    public VersionUpdateType getVersionUpdateType() {
        return project.getVersionUpdateType();
    }

    public void setVersionUpdateType(VersionUpdateType versionUpdateType) {
        this.project.setVersionUpdateType(versionUpdateType);
    }

    public ProtocolType getProtocolType() {
        return project.getProtocolType();
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.project.setProtocolType(protocolType);
    }

    public Launcher getLauncher() {
        return project.getLauncher();
    }

    public void setLauncher(Launcher launcher) {
        this.project.setLauncher(launcher);
    }
}
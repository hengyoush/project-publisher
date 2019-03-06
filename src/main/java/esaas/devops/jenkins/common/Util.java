package esaas.devops.jenkins.common;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import esaas.devops.jenkins.publish.remote.RemoteAddrWrapper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class Util {

    /**
     * properties相关属性key
     */

    private static final String DEVOPS_PUBLISH_PROJECT = "devops.docker.project";
    private static final String DEVOPS_PROJECT_TYPE = "devops.projectType";
    private static final String DEVOPS_PROJECT_KEY = "devops.projectKey";
    private static final String DEVOPS_PROJECT_NAME = "devops.projectName";
    private static final String DEVOPS_PUBLISH_PROTOCOL = "devops.publish.protocol";
    private static final String DEVOPS_FTP_URL = "devops.remote.url";
    private static final String DEVOPS_FTP_LOGIN = "devops.remote.login";
    private static final String DEVOPS_FTP_PASSWORD = "devops.remote.password";
    private static final String DEVOPS_FTP_PORT = "devops.remote.port";
    private static final String DEVOPS_REMOTE_WORKDIR = "devops.remote.workdir";
    private static final String DEVOPS_PUBLISH_TYPE = "devops.publish.type";
    private static final String DEVOPS_TARGET_PROJ_ROOT = "devops.target.proj.root";
    private static final String DEVOPS_VERSION_CHANGE_TYPE = "devops.version.changeType";

    public static Project getProjectFromProperties(Properties props, AbstractBuild<?, ?> build, Launcher launcher) {
        final AbstractProject<?, ?> project = ((AbstractBuild<?, ?>) build).getProject();
        try {
            /**
             * 工程路径相关
             */
            String projectTypeStr = (String) props.get(DEVOPS_PROJECT_TYPE);
            ProjectType projectType = ProjectType.valueOf(projectTypeStr);
            Project projectInner = Project.of(projectType);
            String workspacePath = Objects.requireNonNull(build.getWorkspace()).toURI().getPath();
            projectInner.setProjectKey((String) props.get(DEVOPS_PROJECT_KEY)); // 类似efp.console.middle
            projectInner.setProjectName((String) props.get(DEVOPS_PROJECT_NAME));// 类似efp.console.impl
            // String projectRoot = workspacePath + File.separator + project.getName();
            projectInner.setProjectRoot(new File(workspacePath));
            projectInner.setPackageRoot(
                    new File(workspacePath + File.separator + projectInner.getProjectKey().replace(".", "-")));

            /**
             * 远程传输相关
             */
            ProtocolType protocol = ProtocolType.valueOf((String) props.get(DEVOPS_PUBLISH_PROTOCOL));
            projectInner.setProtocolType(protocol);
            RemoteAddrWrapper remoteAddr;
            if (protocol == ProtocolType.FTP) {
                remoteAddr = new RemoteAddrWrapper((String) props.get(DEVOPS_FTP_URL),
                        (String) props.get(DEVOPS_FTP_LOGIN), (String) props.get(DEVOPS_FTP_PASSWORD),
                        Integer.valueOf((String) props.get(DEVOPS_FTP_PORT)),
                        (String) props.get(DEVOPS_REMOTE_WORKDIR));
            } else {
                throw new IllegalArgumentException("不支持的protocol： " + protocol);
            }
            projectInner.setTargetProjRoot((String) props.get(DEVOPS_TARGET_PROJ_ROOT));
            projectInner.setRemoteAddr(remoteAddr);

            /**
             * 发布类型相关（如是否是docker）
             */
            projectInner.setPackageType(PackageType.valueOf((String) props.get(DEVOPS_PUBLISH_TYPE)));
            if (projectInner.getPackageType() == PackageType.docker) {
                DockerProject dockerProject = new DockerProject(projectInner);
                dockerProject.setTargetProject((String) props.get(DEVOPS_PUBLISH_PROJECT));
                projectInner = dockerProject;
            }
           
            /**
             * 版本管理相关
             */
            VersionUpdateType changeType;
            try {
                changeType = VersionUpdateType.valueOf((String) props.get(DEVOPS_VERSION_CHANGE_TYPE));
            } catch (IllegalArgumentException e) {
                changeType = VersionUpdateType.SMALL;
            }
            projectInner.setVersionUpdateType(changeType);

            /**
             *  其他
             *  */
            projectInner.setLauncher(launcher);

            return projectInner;
        } catch (Exception e) {
            getLogger().println("buildMiddleProject异常");
            e.printStackTrace(global_logger);
            throw new RuntimeException(e);
        }
    }

    public static Properties loadPropertiesFromString(String string) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.defaultCharset()));
        properties.load(inputStream);
        return properties;
    }

    public static PrintStream getLogger() { return global_logger; }

    public static void setLogger(PrintStream global_logger) {
        if (Util.global_logger == null) {
            Util.global_logger = global_logger;
        }
    }

    public static File getTrueProjectRoot(Project project) {
        File projectRoot = project.getProjectRoot();
        String projectLogicName = project.getProjectName();
        return new File(projectRoot.getAbsolutePath() + File.separator +
                projectLogicName);
    }

    public static FilePath getFilePathFromPathStr(String path) {
        return new FilePath(new File(path));
    }

    public static String getTargetDirStr(Project project, DirType dirType) {
        return project.getPackageRoot().getAbsolutePath() + File.separator +
                dirType.toString();
    }

    public static String getSrcClassPath(Project project) {
        return getTrueProjectRoot(project).getAbsolutePath() +
                File.separator +
                "target" + File.separator +
                "classes";
    }

    public static String getLibClassPath(Project project) {
        return getTrueProjectRoot(project).getAbsolutePath() +
                File.separator +
                "target" + File.separator +
                "lib";
    }

    public static FilePath getWarFileForFront(Project project) {
        File target = new File(getSrcTargetPath(project));
        File[] files = target.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("war");
            }
        });
        if (files != null && files.length == 1) {
            return new FilePath(files[0]);
        } else {
            Util.getLogger().println("该目录: " + target.getAbsolutePath() + "下不存在war包！");
            return null;
        }
    }

    public static String getSrcTargetPath(Project project) {
        return getTrueProjectRoot(project) + File.separator + "target";
    }

    private static PrintStream global_logger;

    // TODO
    public static String getPkgRootDot(Project project) {
        String projectName = project.getProjectKey();
        switch (project.getProjectType()) {
            case sprboot:
            case middle: {
                return projectName.replace(".middle", ".service");
            }
            case front:
                default: return "";
        }
    }

    private static final String SUFFIX_SH = ".sh";

    public static String getShellFileName(String projectHyphen, ShellType shellType) {
        switch (shellType) {
            case BACKUP: return "backup-remove-" + projectHyphen + SUFFIX_SH;
            case START: return "start-" +  projectHyphen + SUFFIX_SH;
            case STOP: return "stop-" +  projectHyphen + SUFFIX_SH;
            case INIT: return "init-" +  projectHyphen + SUFFIX_SH;
                default: return null;
        }
    }

    public static Version getNextVersion(List<Version> versions, VersionUpdateType versionUpdateType) {
        if (versions.isEmpty()) {
            return Version.START_VERSION;
        }
        Version version = Collections.max(versions);
        switch (versionUpdateType) {
            case BIG: return new Version((version.getLeft() + 1), 0, 0);
            case MIDDLE: return new Version(version.getLeft(), (version.getMid() + 1), 0);
            case SMALL: return new Version(version.getLeft(), version.getMid(), (version.getRight() + 1));
            default: throw new IllegalArgumentException("非法的versionChangeType： " + versionUpdateType);
        }
    }

    public static FTPClient getFTPClient(String ftpHost, String ftpUserName,
                                         String ftpPassword, int ftpPort) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                Util.getLogger().println("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                Util.getLogger().println("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace(Util.getLogger());
            Util.getLogger().println("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace(Util.getLogger());
            Util.getLogger().println("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }
}

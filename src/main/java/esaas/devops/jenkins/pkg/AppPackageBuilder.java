package esaas.devops.jenkins.pkg;

import esaas.devops.jenkins.common.Util;
import hudson.FilePath;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.DirType;

import java.io.*;

/**
 * 使用App类型打包
 * 特点：自动构建启停脚本，自动构建基础目录
 */
public class AppPackageBuilder implements PackageBuilder {

    /**
     * 处理路径
     * 打tar包
     * @param project
     */
    @Override
    public File build(Project project) {
        // if (!validatePath(project)) {
        //     throw new IllegalStateException("在进行APP类型的打包之前必须正确使用maven构建！");
        // } 
        String packageRoot = project.getPackageRoot().getAbsolutePath();
        if (!project.getPackageRoot().exists() && project.getPackageRoot().mkdirs()) {
            Util.getLogger().println("创建package root成功，路径：" + packageRoot);
        }
        scan(project.getProjectType().dirs(), project);
        return buildTar(project);
    }

    /**----------------------------------------------------------**/
    /**                      private methods                     **/
    /**----------------------------------------------------------**/

    /** 校验target、lib、classes目录是否存在 */
    // private boolean validatePath (Project project) {
    //     if (Util.pathExists(Util.getSrcTargetPath(project)) && 
    //         Util.pathExists(Util.getLibClassPath(project))  &&
    //         Util.pathExists(Util.getSrcClassPath(project))) {
    //         return true;
    //     } 
    //     return false;
    // }

    private void scan(DirType[] dirs, Project project)  {
        for (DirType dir : dirs) {
            File logs = new File(project.getPackageRoot().getAbsolutePath() + File.separator + dir.toString());
            if (dir.needPreCreate() && !logs.exists() && logs.mkdir()) {
                Util.getLogger().println("创建目录" + logs.getAbsolutePath() + "成功");
            } else if (logs.exists()) {
                Util.getLogger().println("目录：" + logs.getAbsolutePath() + "已存在");
            } else if (!dir.needPreCreate()) {
                Util.getLogger().println("目录类型：" + dir + " 不需要预先创建目录");
            } else {
                Util.getLogger().println("目录：" + logs.getAbsolutePath() + "创建失败");
            }
            dir.getHandler(project).handle(project);
        }
    }

    private File buildTar(Project project) {
        FilePath path = new FilePath(project.getPackageRoot());
        String tarPath = project.getProjectRoot() +
                File.separator +
                project.getProjectName() + ".tar";
        File tarFile = new File(tarPath);

        try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tarFile))) {
            path.tar(outputStream, new FileFilter() {
                public boolean accept(File pathname) {
                    return true;
                } });
            Util.getLogger().println("写入tar包成功，开始清除" + path);
            Util.getLogger().println("tar路径" + tarFile.getAbsolutePath());
            path.deleteRecursive();
            return tarFile;
        } catch (IOException | InterruptedException e) {
            Util.getLogger().println("写入tar包出错");
            throw new RuntimeException(e);
        }
    }
}

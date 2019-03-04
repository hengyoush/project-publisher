package esaas.devops.jenkins.pkg;

import esaas.devops.jenkins.common.Util;
import hudson.FilePath;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.DirType;

import java.io.*;

/**
 * 普通App应用打包处理类
 */
public class AppPackageBuilder implements PackageBuilder {

    /**
     * 处理路径
     * 打tar包
     * @param project
     */
    @Override
    public File build(Project project) {
        String packageRoot = project.getPackageRoot().getAbsolutePath();
        if (!project.getPackageRoot().exists() && project.getPackageRoot().mkdirs()) {
            Util.getLogger().println("创建package root成功，路径：" + packageRoot);
        }
        scan(project.getProjectType().dirs(), project);
        project.setTar(buildTar(project));
        return project.getTar();
    }

    /**----------------------------------------------------------**/
    /**                      private methods                     **/
    /**----------------------------------------------------------**/

    private void scan(DirType[] dirs, Project project) {
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
        String packageRoot = project.getPackageRoot().getAbsolutePath();
        FilePath path = new FilePath(project.getPackageRoot());
        String tarPath = packageRoot + File.separator + ".." +
                File.separator +
                project.getProjectName() + ".tar";
        File tarFile = new File(tarPath);

        try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tarFile))) {
            path.tar(outputStream, new FileFilter() {
                public boolean accept(File pathname) {
                    return true;
                } });
            Util.getLogger().println("写入tar包成功，开始清除");
            path.deleteRecursive();
            return tarFile;
        } catch (IOException | InterruptedException e) {
            Util.getLogger().println("写入tar包出错");
            throw new RuntimeException(e);
        }
    }
}

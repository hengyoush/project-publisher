package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.pkg.shell.DefaultShellGenerator;
import esaas.devops.jenkins.pkg.shell.ShellGenerator;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Shell;
import esaas.devops.jenkins.common.DirType;
import esaas.devops.jenkins.common.ShellType;
import esaas.devops.jenkins.common.Util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 处理App的Bin目录，存放一些脚本
 */
public class BinHandler implements DirHandler {

    public static final DirHandler INSTANCE = new BinHandler();
    private final ShellGenerator shellGenerator;

    private BinHandler() {
        this(new DefaultShellGenerator());
    }

    private BinHandler(ShellGenerator shellGenerator) {
        this.shellGenerator = shellGenerator;
    }

    @Override
    public void handle(Project project) {
        // 创建对应的目录，init，deploy，undeploy
        File init, deploy, unDeploy;
        try {
            String packageRoot = Util.getTargetDirStr(project, DirType.BIN);
            init = new File(packageRoot + File.separator + "init");
            init.mkdir();
            deploy = new File(packageRoot + File.separator + "deploy");
            deploy.mkdir();
            unDeploy = new File(packageRoot + File.separator + "undeploy");
            unDeploy.mkdir();
        } catch (Exception e) {
            Util.getLogger().println("创建目录失败！");
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }

        // 生成shell
        Shell initShell, startShell, backupShell, stopShell;
        initShell = shellGenerator.generate(project, ShellType.INIT);
        startShell = shellGenerator.generate(project, ShellType.START);
        backupShell = shellGenerator.generate(project, ShellType.BACKUP);
        stopShell = shellGenerator.generate(project, ShellType.STOP);

        // 将shell写入到文件中
        try {
            writeShellToFile(init, initShell);
            writeShellToFile(deploy, startShell);
            writeShellToFile(unDeploy, stopShell);
            writeShellToFile(unDeploy, backupShell);
        } catch (IOException e) {
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }
    }

    private void writeShellToFile(File parent, Shell shell) throws IOException {
        Files.write(Paths.get(parent.getAbsolutePath() + File.separator + shell.getFilename()),
                shell.getContent().getBytes(Charset.forName("utf-8")));
    }
}

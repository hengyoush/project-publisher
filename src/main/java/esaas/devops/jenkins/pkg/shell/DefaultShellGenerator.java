package esaas.devops.jenkins.pkg.shell;

import esaas.devops.jenkins.common.Shell;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.ShellType;
import esaas.devops.jenkins.common.Util;

/**
 * 根据模板生成shell的实现类
 */
public class DefaultShellGenerator implements ShellGenerator {

    @Override
    public Shell generate(Project project, ShellType shellType) {
        String hyphenName = project.getProjectKey().replace(".", "-");
        String content = doGenerate(shellType, project.getTargetProjRoot(), hyphenName,  getPkgRootDot(project));

        Shell shell = new Shell();
        shell.setContent(content);
        shell.setType(shellType);
        shell.setFilename(Util.getShellFileName(hyphenName, shellType));
        return shell;
    }

    private String doGenerate(ShellType shellType, String targetProjRoot, String projNameHyphen, String projPkgDot) {
        switch (shellType) {
            case INIT:
                return ShellHolder.getContentFromTemplate(ShellHolder.MIDDLE_INIT,
                        targetProjRoot, projNameHyphen, projPkgDot);
            case STOP:
                return ShellHolder.getContentFromTemplate(ShellHolder.MIDDLE_UNDEP_STOP,
                        targetProjRoot, projNameHyphen, projPkgDot);
            case START:
                return ShellHolder.getContentFromTemplate(ShellHolder.MIDDLE_DEPLOY_START,
                        targetProjRoot, projNameHyphen, projPkgDot);
            case BACKUP:
                return ShellHolder.getContentFromTemplate(ShellHolder.MIDDLE_UNDEP_BACKUP,
                        targetProjRoot, projNameHyphen, projPkgDot);
            default: throw new IllegalArgumentException("非法的SHellType");
        }
    }

    private String getPkgRootDot(Project project) {
        String projectName = project.getProjectKey();
        return projectName.replace(".middle", ".service");
    }

    private static class ShellHolder {
        public static final String PLACEHOLDER_PROJ_ROOT = "${target.proj.root}";
        public static final String PLACEHOLDER_PROJ_NAME_HYPHEN = "${proj.name.hyphen}";
        public static final String PLACEHOLDER_PROJ_PKG_DOT = "${proj.pkg.dot}";

        public static String getContentFromTemplate(String template, String targetProjRoot, String projNameHyphen, String projPkgDot) {
            return template.replace(PLACEHOLDER_PROJ_NAME_HYPHEN, projNameHyphen)
                    .replace(PLACEHOLDER_PROJ_PKG_DOT, projPkgDot)
                    .replace(PLACEHOLDER_PROJ_ROOT, targetProjRoot);
        }

        public static final String MIDDLE_INIT = "#!/bin/bash\n" +
                "\n" +
                "cd ${target.proj.root}/${proj.name.hyphen}/bin/init\n" +
                "\n" +
                "echo \"init-${proj.name.hyphen}  begin\"\n" +
                "export DEPLOY_HOME=${target.proj.root}/${proj.name.hyphen}/bin/deploy\n" +
                "if [ -d ${DEPLOY_HOME} ]\n" +
                "    then \n" +
                "        echo \"DEPLOY_HOME is existed,no more need create!\"\n" +
                "    else \n" +
                "        mkdir -p ${DEPLOY_HOME}\n" +
                "        echo \"DEPLOY_HOME create success!\"\n" +
                "fi\n" +
                "export UNDEPLOY_HOME=${target.proj.root}/${proj.name.hyphen}/bin/undeploy\n" +
                "if [ -d ${UNDEPLOY_HOME} ]\n" +
                "    then \n" +
                "        echo \"UNDEPLOY_HOME is existed,no more need create!\"\n" +
                "    else \n" +
                "        mkdir -p ${UNDEPLOY_HOME}\n" +
                "        echo \"UNDEPLOY_HOME create success!\"\n" +
                "fi\n" +
                "export CONF_HOME=${target.proj.root}/${proj.name.hyphen}/conf\n" +
                "if [ -d ${CONF_HOME} ]\n" +
                "    then \n" +
                "        echo \"CONF_HOME is existed,no more need create!\"\n" +
                "    else \n" +
                "        mkdir -p ${CONF_HOME}\n" +
                "        echo \"CONF_HOME create success!\"\n" +
                "fi\n" +
                "export APP_HOME=${target.proj.root}/${proj.name.hyphen}/lib\n" +
                "if [ -d ${APP_HOME} ]\n" +
                "    then \n" +
                "        echo \"APP_HOME is existed,no more need create!\"\n" +
                "    else \n" +
                "        mkdir -p ${APP_HOME}\n" +
                "        echo \"APP_HOME create success!\"\n" +
                "fi\n" +
                "export LOGS_HOME=${target.proj.root}/${proj.name.hyphen}/logs\n" +
                "if [ -d ${LOGS_HOME} ]\n" +
                "    then \n" +
                "        echo \"LOGS_HOME is existed,no more need create!\"\n" +
                "    else \n" +
                "        mkdir -p ${LOGS_HOME}\n" +
                "        echo \"LOGS_HOME create success!\"\n" +
                "fi\n" +
                "export BACKUP_HOME=${target.proj.root}/${proj.name.hyphen}/backup\n" +
                "if [ -d ${BACKUP_HOME} ]\n" +
                "    then \n" +
                "        echo \"BACKUP_HOME is existed,no more need create!\"\n" +
                "    else \n" +
                "        mkdir -p ${BACKUP_HOME}\n" +
                "        echo \"BACKUP_HOME create success!\"\n" +
                "fi\n" +
                "echo \"init-${proj.name.hyphen}  end\"\n";

        public static final String MIDDLE_DEPLOY_START = "#!/bin/bash\n" +
                "\n" +
                "cd ${target.proj.root}/${proj.name.hyphen}/bin/deploy\n" +
                "\n" +
                "if [ \"`ps -ef |grep 'com.irdstudio.${proj.pkg.dot}.boot'|grep -v grep`\" ];then\n" +
                "   kill `ps -ef |grep 'com.irdstudio.${proj.pkg.dot}.boot'|grep -v grep |awk '{print $2}'`\n" +
                "fi\n" +
                "export JAVA_HOME=/opt/jdk1.8.0_192\n" +
                "APP_HOME=${target.proj.root}/${proj.name.hyphen}/lib\n" +
                "cd $APP_HOME\n" +
                "for i in \"$APP_HOME\"/*.jar\n" +
                "do\n" +
                " CLASSPATH=\"$CLASSPATH\":\"$i\"\n" +
                "done\n" +
                " export CLASSPATH=$CLASSPATH\n" +
                " echo ${CLASSPATH}\n" +
                " echo $JAVA_HOME\n" +
                "/opt/jdk1.8.0_192/bin/java -Dlog4j.configurationFile=${target.proj.root}/${proj.name.hyphen}/conf/log4j2.xml -server -Xms512m -Xmx512m -Xmn390m -Xss256k -XX:MaxMetaspaceSize=64m -XX:MaxMetaspaceSize=256m -Xss256k -XX:SurvivorRatio=4 -XX:TargetSurvivorRatio=70 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSClassUnloadingEnabled -XX:+DisableExplicitGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSScavengeBeforeRemark -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintFlagsFinal -Xloggc:${target.proj.root}/${proj.name.hyphen}/logs/gc.log.`date +%Y%m%d.%H%M%S` -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${target.proj.root}/${proj.name.hyphen}/logs/dump_core_pid%p.hprof -XX:ErrorFile=${target.proj.root}/${proj.name.hyphen}/logs/hs_err_pid%p.log com.irdstudio.${proj.pkg.dot}.boot.DubboServiceMain &>${target.proj.root}/${proj.name.hyphen}/logs/${proj.name.hyphen}.log &\n";

        public static final String MIDDLE_UNDEP_BACKUP = "#!/bin/bash\n" +
                "\n" +
                "cd ${target.proj.root}/${proj.name.hyphen}/bin/undeploy\n" +
                "\n" +
                "if [ \"`ps -ef |grep 'com.irdstudio.${proj.pkg.dot}.boot'|grep -v grep`\" ];then\n" +
                "   kill `ps -ef |grep 'com.irdstudio.${proj.pkg.dot}.boot'|grep -v grep |awk '{print $2}'`\n" +
                "fi\n" +
                "APP_HOME=${target.proj.root}/${proj.name.hyphen}/lib\n" +
                "echo \"工程存放目录为：\"$APP_HOME\n" +
                "TAR_GZ_NAME=$(date +\"%Y%m%d%H%M%s\")\n" +
                "echo \"备份的压缩包名称为：\"$TAR_GZ_NAME\n" +
                "BACKUP_HOME=${target.proj.root}/${proj.name.hyphen}/backup\n" +
                "echo \"备份文件存放目录为：\"$BACKUP_HOME\n" +
                "cd $APP_HOME\n" +
                "echo \"将工程进行打包开始\"\n" +
                "tar -cvf $TAR_GZ_NAME.tar.gz  *.jar\n" +
                "echo \"将工程进行打包结束\"\n" +
                "echo \"将备份后文件拷贝到指定目录开始\"\n" +
                "mv $TAR_GZ_NAME.tar.gz  $BACKUP_HOME\n" +
                "echo \"将备份后文件拷贝到指定目录结束\"\n" +
                "echo \"将工程目录下文件删除开始\"\n" +
                "rm -f  $APP_HOME/*.jar\n" +
                "echo \"将工程目录下文件删除结束\"\n";
        public static final String MIDDLE_UNDEP_STOP = "#!/bin/bash\n" +
                "\n" +
                "cd ${target.proj.root}/${proj.name.hyphen}/bin/undeploy\n" +
                "\n" +
                "if [ \"`ps -ef |grep 'com.irdstudio.${proj.pkg.dot}.boot'|grep -v grep`\" ];then\n" +
                "   kill -9 `ps -ef |grep 'com.irdstudio.${proj.pkg.dot}.boot'|grep -v grep |awk '{print $2}'`\n" +
                "fi\n" +
                "\n" +
                "echo \"stop ${proj.pkg.dot} success\"\n";
    }
}

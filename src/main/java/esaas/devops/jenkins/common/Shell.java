package esaas.devops.jenkins.common;

public class Shell {
    /** 脚本文件的名称 **/
    private String filename;
    /** 脚本文件的内容 **/
    private String content;
    /** 脚本类型，如：初始化，启停脚本等 **/
    private ShellType type;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ShellType getType() {
        return type;
    }

    public void setType(ShellType type) {
        this.type = type;
    }
}

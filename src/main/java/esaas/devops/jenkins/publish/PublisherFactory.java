package esaas.devops.jenkins.publish;

import esaas.devops.jenkins.common.ProtocolType;

/**
 * Publisher的静态工厂类
 */
public class PublisherFactory {
    public static Publisher getPublisher(ProtocolType protocolType) {
        switch (protocolType) {
            case FTP: return new FTPPublisher();
                default: throw new IllegalArgumentException("不支持的协议类型： "  + protocolType);
        }
    }
}

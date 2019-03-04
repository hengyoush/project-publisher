package esaas.devops.jenkins.pkg;

import esaas.devops.jenkins.common.PackageType;

/**
 * PackageBuilder的静态工厂
 */
public class PackageBuilderFactory {
    // TODO
    public static PackageBuilder getPackageBuilder(PackageType packageType) {
        switch (packageType) {
            case app: return new AppPackageBuilder();
            case docker:
                default: return null;
        }

    }
}

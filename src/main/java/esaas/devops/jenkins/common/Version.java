package esaas.devops.jenkins.common;

import java.util.Objects;

public class Version implements Comparable<Version> {

    public static final String LATEST = "LATEST";
    public static final Version START_VERSION = Version.of("0.0.1");
    public static final String DOT = ".";

    private final Integer left;
    private final Integer mid;
    private final Integer right;
    private String origin;

    public Version(String left, String mid, String right, String origin) {
        this(Integer.valueOf(left), Integer.valueOf(mid), Integer.valueOf(right));
        this.origin = origin;
    }

    public Version(Integer left, Integer mid, Integer right) {
        this.left = left;
        this.mid = mid;
        this.right = right;
    }

    public static Version of(String origin) {
        String[] s = origin.split("\\.");
        return new Version(s[0], s[1], s[2], origin);
    }

    public String getStr() {
        return new StringBuilder().append(left).append(DOT).append(mid).append(DOT).append(right).toString();
    }

    public Integer getLeft() {
        return left;
    }

    public Integer getMid() {
        return mid;
    }

    public Integer getRight() {
        return right;
    }

    public String getOrigin() {
        return origin;
    }

    @Override
    public int compareTo(Version o) {
        Objects.requireNonNull(o);
        int temp;
        if ((temp = this.left.compareTo(o.left)) != 0 ||
                (temp = this.mid.compareTo(o.mid)) != 0 ||
                (temp = this.right.compareTo(o.right)) != 0) {

        }
        return temp;
    }

    @Override
    public String toString() {
        return getStr();
    }
}

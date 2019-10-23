package cn.choleece.base.jvm.hotload;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-23 23:22
 **/
public class VersionTest {

    private String version = "5564444456";

    public VersionTest() {
    }

    public VersionTest(String version) {
        this.version = version;
    }

    public void printVersion() {
        System.out.println("hello, I am version " + this.version);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

package mobi.infolife.limitNumAndAir.XKUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PinyinResource {

    private static Properties getResource(String resourceName) {
        InputStream is = PinyinResource.class.getResourceAsStream(resourceName);
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return props;
    }

    protected static Properties getPinyinTable() {
        Properties pro = new Properties();
        try {
            pro.load(PinyinResource.class.getResourceAsStream("/assets/pinyin.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
//		String resourceName = "/data/pinyin.db";
//		return getResource(resourceName);
    }

    protected static Properties getMutilPintinTable() {
        Properties pro = new Properties();
        try {
            pro.load(PinyinResource.class.getResourceAsStream("/assets/mutil_pinyin.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
//		String resourceName = "/data/mutil_pinyin.db";
//		return getResource(resourceName);
    }

    protected static Properties getChineseTable() {
        Properties pro = new Properties();
        try {
            pro.load(PinyinResource.class.getResourceAsStream("/assets/chinese.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
//		String resourceName = "/data/chinese.db";
//		return getResource(resourceName);
    }
}

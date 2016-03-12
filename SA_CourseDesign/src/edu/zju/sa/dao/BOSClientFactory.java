package edu.zju.sa.dao;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chen on 2016/2/15.
 */
public class BOSClientFactory {
    public static final String DEF_PROP_FILE = "bos.properties";

    public static final String KEY_ACCESS_KEY = "bos.access.key";
    public static final String KEY_ACCESS_SECRET = "bos.access.key.secret";
    public static final String KEY_BUCKET_NAME = "bos.bucket.name";


    public static BOSClient getClient() {
        Properties props = new Properties();
        try {
            InputStream is = BOSClientFactory.class.getClassLoader().getResourceAsStream(DEF_PROP_FILE);
            props.load(is);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getClient(props);
    }

    public static BOSClient getClient(Properties props) {
        String accessKey = props.getProperty(KEY_ACCESS_KEY);
        String secretKey = props.getProperty(KEY_ACCESS_SECRET);
        String bucketName = props.getProperty(KEY_BUCKET_NAME);
        BosClientConfiguration config = new BosClientConfiguration();

        //		config.setProxyHost("127.0.0.1");    //客户端的所有操作都会通过127.0.0.1地址的8080端口做代理执行。
//		config.setProxyPort("8080");
        config.setMaxConnections(10);    // 设置HTTP最大连接数为10
        config.setConnectionTimeoutInMillis(5000);   // 设置TCP连接超时为5000毫秒
        config.setSocketTimeoutInMillis(2000);   // 设置Socket传输数据超时的时间为2000毫秒
        config.setCredentials(new DefaultBceCredentials(accessKey, secretKey));
        BosClient client = new BosClient(config);
        return new BOSClient(client, bucketName);
    }
}

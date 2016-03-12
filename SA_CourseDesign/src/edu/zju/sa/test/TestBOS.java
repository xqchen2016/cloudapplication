package edu.zju.sa.test;

import edu.zju.sa.dao.BOSClient;
import edu.zju.sa.dao.BOSClientFactory;
import edu.zju.sa.dao.FileItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by chenbo on 2016/2/15.
 */
public class TestBOS {
    public static void main(String[] args) throws Exception{
        Properties props = new Properties();
        props.put(BOSClientFactory.KEY_ACCESS_KEY, "fbc939520ffd49cc91498abea05742b8");
        props.put(BOSClientFactory.KEY_ACCESS_SECRET, "a830572042b64c46ba21c621c81076ec");
        props.put(BOSClientFactory.KEY_BUCKET_NAME, "new-bucket");

        BOSClient client= BOSClientFactory.getClient(props);

        //client.listFiles();

        List<FileItem> list = new ArrayList<FileItem>();
        list.add(new FileItem("chenbo-test1"));
        list.add(new FileItem("chenbo-test2"));
        client.updateFileList(list);

        System.out.println(new String(client.getObject(".__list__.")));
    }
}

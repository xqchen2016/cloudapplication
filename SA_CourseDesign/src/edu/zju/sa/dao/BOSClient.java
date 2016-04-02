package edu.zju.sa.dao;

import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.GetObjectRequest;
import com.baidubce.services.bos.model.PutObjectResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2016/2/15.
 */
public class BOSClient {
    private BosClient client;
    private String bucketName;
    private JsonParser jsonParser = new JsonParser();

    private final static String FILE_PREFIX = ".__file__";
    private final static String LIST_KEY = ".__list__.";
    private final static int BUFFER_SIZE = 10240;
    private final static int DEF_FILE_SIZE = 1024000;
    private final static String FIELD_NAME = "n";

    public BOSClient(BosClient client, String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
    }

    public List<FileItem> listFiles() throws Exception {
        List<FileItem> retList = new ArrayList<FileItem>();
        byte[] arr = getObject(LIST_KEY);
        if (arr == null) return retList;
        String jsonStr = new String(arr);
        JsonArray jsonArray = jsonParser.parse(jsonStr).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); ++i) {
            FileItem item = new FileItem();
            item.setName(jsonArray.get(i).getAsJsonObject().get(FIELD_NAME).getAsString());
            retList.add(item);
        }
        return retList;
    }

    public void updateFileList(List<FileItem> list) throws Exception{
        JsonArray jsonArray = new JsonArray();
        for(FileItem item: list){
            JsonObject json = new JsonObject();
            json.addProperty(FIELD_NAME, item.getName());
            jsonArray.add(json);
        }
        byte[] content = jsonArray.toString().getBytes();
        putObject(LIST_KEY, content);
    }

    public void putFile(String key, byte[] content, boolean updateFileList)throws Exception {
        putObject(FILE_PREFIX + key, content);
        if (updateFileList) {
            List<FileItem> list = listFiles();
            FileItem item = new FileItem();
            item.setName(key);
            list.add(item);
            updateFileList(list);
        }
    }

    public byte[] getFile(String key) throws Exception{
        return getObject(FILE_PREFIX+key);
    }

    public void putObject(String key, byte[] content)throws Exception{
        PutObjectResponse putObjectFromFileResponse = client.putObject(bucketName, key, content);
        System.out.println("BOS PUT: "+ putObjectFromFileResponse.getETag());
    }

    public byte[] getObject(String key) throws Exception{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(DEF_FILE_SIZE);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, LIST_KEY);
        BosObject bos = client.getObject(getObjectRequest);
        if(bos==null) return null;
        InputStream is = bos.getObjectContent();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while((len=is.read(buffer))>0){
            byteArrayOutputStream.write(buffer, 0, len);
        }
        is.close();
        bos.close();
        return byteArrayOutputStream.toByteArray();
    }

}

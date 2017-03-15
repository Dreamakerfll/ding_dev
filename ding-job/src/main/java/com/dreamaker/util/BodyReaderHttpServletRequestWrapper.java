package com.dreamaker.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;  

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper{
	private Map<String, String[]> paramsMap;  
	  
    @Override
    public Map<String, String[]> getParameterMap() {  
        return paramsMap;  
    }  

    @Override  
    public String getParameter(String name) {// 重写getParameter，代表参数从当前类中的map获取  
        String[] values = paramsMap.get(name);  
        if (values == null || values.length == 0) {  
            return null;  
        }  
        return values[0];  
    }  

    @Override  
    public String[] getParameterValues(String name) {// 同上  
        return paramsMap.get(name);  
    }  

    @Override
    public Enumeration<String> getParameterNames() {  
        return Collections.enumeration(paramsMap.keySet());  
    }  

    private String getRequestBody(InputStream stream) {  
        String line = "";  
        StringBuilder body = new StringBuilder();  
        int counter = 0;  

        // 读取POST提交的数据内容  
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));  
        try {  
            while ((line = reader.readLine()) != null) {  
                if (counter > 0) {  
                    body.append("rn");  
                }  
                body.append(line);  
                counter++;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

        return body.toString();  
    }  

    private HashMap<String, String[]> getParamMapFromPost(HttpServletRequest request) {  

        String body = "";  
        try {  
            body = getRequestBody(request.getInputStream());
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        HashMap<String, String[]> result = new HashMap<String, String[]>();  

        if (null == body || 0 == body.length()) {  
            return result;  
        }
        
    	//获取上传文件类型
        if (ServletFileUpload.isMultipartContent(request)){
            //创建ServletFileUpload实例
            ServletFileUpload fileUpload = new ServletFileUpload();
            try {
                //解析request请求 返回FileItemStream的iterator实例
                FileItemIterator iter = fileUpload.getItemIterator(request);
                InputStream is = null;//输出流
                //迭代取出
                while (iter.hasNext()){
                    FileItemStream item = iter.next();//获取文件流
                    String name = item.getFieldName();//返回表单中标签的name值
                    is = item.openStream();//得到对应表单的输出流
                    if (item.isFormField()){//如果是非文件域,设置进入map,这里要注意多值处理
                        //如果不是文件上传,处理
                    	if (result.containsKey(name)){//判断当前值name是否已经存储过
                            String[] values = result.get(name);//取出已经存储过的值
                            values = Arrays.copyOf(values,values.length+1);//把当前数组扩大
                            values[values.length-1] = Streams.asString(is);//增加新值
                            result.put(name,values);//重新添加到map中
                            System.out.println(name);
                        }else {
                        	result.put(name,new String[]{Streams.asString(is)});//直接存入参数中
                        	System.out.println(name+"++++++++");
                        }
                    }
                }
                
                
 
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return result;
        }else {
        	return parseQueryString(body);  //如果不是文件上传请求,则直接设置map
        }

        
    }  
    

    // 自定义解码函数  
    private String decodeValue(String value) {  
        if (value.contains("%u")) {  
            return Encodes.urlDecode(value);  
        } else {  
            try {  
                return URLDecoder.decode(value, "UTF-8");  
            } catch (UnsupportedEncodingException e) {  
                return "";// 非UTF-8编码  
            }  
        }  
    }  

    public HashMap<String, String[]> parseQueryString(String s) {  
        String valArray[] = null;  
        if (s == null) {  
            throw new IllegalArgumentException();  
        }  
        HashMap<String, String[]> ht = new HashMap<String, String[]>();  
        StringTokenizer st = new StringTokenizer(s, "&");  
        while (st.hasMoreTokens()) {  
            String pair = (String) st.nextToken();  
            int pos = pair.indexOf('=');  
            if (pos == -1) {  
                continue;  
            }  
            String key = pair.substring(0, pos);  
            String val = pair.substring(pos + 1, pair.length());  
            if (ht.containsKey(key)) {  
                String oldVals[] = (String[]) ht.get(key);  
                valArray = new String[oldVals.length + 1];  
                for (int i = 0; i < oldVals.length; i++) {  
                    valArray[i] = oldVals[i];  
                }  
                valArray[oldVals.length] = decodeValue(val);  
            } else {  
                valArray = new String[1];  
                valArray[0] = decodeValue(val);  
            }  
            ht.put(key, valArray);  
        }  
        return ht;  
    }  

    private Map<String, String[]> getParamMapFromGet(HttpServletRequest request) {  
        return parseQueryString(request.getQueryString());  
    }  

    private final byte[] body; // 报文  

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {  
        super(request);  
        body = readBytes(request.getInputStream());  

        // 首先从POST中获取数据  
        if ("POST".equals(request.getMethod().toUpperCase())) {  
            paramsMap = getParamMapFromPost(this);  
        } else {  
            paramsMap = getParamMapFromGet(this);  
        }  

    }  

    @Override  
    public BufferedReader getReader() throws IOException {  
        return new BufferedReader(new InputStreamReader(getInputStream()));  
    }  

    @Override  
    public ServletInputStream getInputStream() throws IOException {  
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);  
        return new ServletInputStream() {  

            @Override  
            public int read() throws IOException {  
                return bais.read();  
            }  

        };  
    }  

    private static byte[] readBytes(InputStream in) throws IOException {  
        BufferedInputStream bufin = new BufferedInputStream(in);  
        int buffSize = 1024;  
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);  

        byte[] temp = new byte[buffSize];  
        int size = 0;  
        while ((size = bufin.read(temp)) != -1) {  
            out.write(temp, 0, size);  
        }  
        bufin.close();  

        byte[] content = out.toByteArray();  
        return content;  
    }  
}

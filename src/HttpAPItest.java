import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * 
 */

/**
 * @author heyi
 * @version 2013-6-25
 *
 */
public class HttpAPItest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpHost target = new HttpHost("localhost", 8080, "http");
//            HttpPost post = new HttpPost("/weishangbao/userManageAction!register");
            HttpGet get=new HttpGet("http://localhost:8080/weishangbao/bytalkLoginAction?loginname=%E8%90%A8%E6%BB%A1");
            HttpPost post = new HttpPost("/weishangbao/bytalkLoginAction");
/**           HttpHost proxy = new HttpHost("localhost", 8888);  
               httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);*/
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();    
//            String en=URLEncoder.encode("萨满", "utf-8");
//            System.out.println(en);
            //nameValuePairs.add(new BasicNameValuePair("registerJson", "{\"name\":\"华少\",\"sex\":\"1\",\"account\":\"hushaohua\",\"mobile\":\"12345678901\",\"org\":[\"133\"],\"pwd\":\"1\"}"));    
            nameValuePairs.add(new BasicNameValuePair("loginname", "萨满")); 
            
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
           // HttpResponse rsp=httpclient.execute(get);
            HttpResponse rsp = httpclient.execute(target, post);
            
            HttpEntity entity = rsp.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(rsp.getStatusLine());
            Header[] headers = rsp.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }

        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            httpclient.getConnectionManager().shutdown();
        }

	}

}

package spider;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyUtil {
	
	static Logger logger = Logger.getLogger(MyUtil.class);
	
	public static boolean checkMatchKey(String slug, String key){
		String[] arr = slug.split("-");
		for(int i=0; i<arr.length; i++){
			if(arr[i].equals(key)) return true;
		}
		return false;
	}
	public  static PageData getPage(String url,HttpClient client,HttpContext context){
		return getPage(url, false,client,context);
	}
	public  static PageData getPage(String url){
		return getPage(url, false,null,null);
	}
	public  static PageData getPage(String url, boolean check) {
		return getPage(url, check, null,null,null,null);
	}
	public  static PageData getPage(String url, boolean check,HttpClient client,HttpContext context) {
		return getPage(url, check, null,client,context,null);
	}
	public  static PageData getPage(String url, boolean check,HttpClient client,HttpContext context,String[] headers) {
		return getPage(url, check, null,client,context,headers);
	}
	public static PageData getPageByCmd(String url){
		PageData pd = null;
		  
		return pd;
		
	}
	/**
	 * 
	 * @param url
	 * @param check 是否检测有些好的spider 
	 * @param charset
	 * @return
	 */
	public  static PageData getPage(String url, boolean check,String charset,HttpClient client,HttpContext context,String[] headers) {
		// TODO Auto-generated method stub
		 BasicHttpParams httpParams = new BasicHttpParams();  
		   HttpConnectionParams.setConnectionTimeout(httpParams, 30000);  
		    HttpConnectionParams.setSoTimeout(httpParams, 30000); 
		    boolean shutdown = false;
		    if(client == null){
		    	shutdown = true;
		    	client = new DefaultHttpClient(httpParams);
		    }
		if(context == null)
			context = new BasicHttpContext();
		
		//System.out.println(context + " " + client);
		
		HttpGet get = new HttpGet(url);
		if(url.contains("google")){
			
		}
		if(url.contains("google")){
			get.setHeader("cookie","PREF=ID=9a471391775d2718:U=017be7dce7b55194:FF=1:LD=zh-CN:NW=1:TM=1382695331:LM=1386039904:GM=1:S=SvQnJWl081TFsWb7; HSID=A066eSNsZTseWRtOK; SSID=AV-7nyoQs45__A4V9; APISID=X0-97mhCRFpjclPE/ADd_Lk9TSH7-Etz-3; SAPISID=1c5b5JpKbcF4iQ0o/Ajw7cQopZF1r72g5l; SID=DQAAAMAAAAAh_aa2SI2mUBcQwXxD7ZaIcbm_lVeaBz7yBR5JvJi_3nX13QwGLknTzpkO4zYy8hKVtuQ6A3Je0k1uOcIQ1l6gGLA-EvD2pe0GvklG8Dohi6RdzlHbDJfN6hlOu6X_TazhAYg0vQZTPJvrHoEPuVNcN_damYs7huAd0VMZcxhypXTVME-iDbo4M-w8ceHXrcbsNpZ8LbqQjts0HCX4ZxCZgidIH6gr-FgeFn2nsSO2aZPagNOS2QOL0ibVmF4wWo0; NID=67=wfsWc4-JhuSOPFcOSHVzr4i2uA-8NABZv_IA8w2a4k-HG1TzUibg5SQuV3W2SltDEj7msm_0xjOhd7K7WN8m0lCDQGVerJxcPi7WJ4dX5lp9wG7I4nNMLG9f_xWd7mJOeFu-B5Oth07fCGYgbTcqST-l8IrhOMUE5xUzWXJuXW_27_eY3wsK9oEfPe_wcyhA2fVTxss-4CA");
			get.setHeader("x-chrome-variations","CP21yQEIhrbJAQiktskBCKm2yQEIxLbJAQiehsoBCNyHygEIlorKAQ==");
		}
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		get.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.setHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		get.setHeader("pragma","no-cache");
		if(headers != null){
			for(int i=0; i < headers.length; i+=2){
				get.setHeader(headers[i], headers[i+1]);
			}
		}
		
		PageData pd = null;
		String total = "";
		try {
			HttpResponse response = client.execute(get, context);
			HttpUriRequest actualRequest = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			HttpHost host = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			
//			if(check && !checkHost(host.toString())){
//System.out.println(host + "   没有spider，返回!" );
//				return null;
//			}
			if(response.getStatusLine().getStatusCode() == 404){
				System.out.println("404! ");
				System.out.println(url);
				return null;	
			}
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				if(charset == null)
				  charset = EntityUtils.getContentCharSet(entity);
				
				if(charset == null){
					if(url.contains("blog.163") ) charset = "gbk";
					else if(url.contains("acm.hdu.edu") || url.contains("blog.51cto.com")) charset="gbk";
					else charset="utf-8";
				}
				//System.out.println("charset:" + charset + "  ;  " + url );
				BufferedReader br = null;
				if(charset == null || charset == "")
					 br = new BufferedReader(new InputStreamReader(entity
						.getContent()));
				else
					 br = new BufferedReader(new InputStreamReader(entity
								.getContent(), charset));
				String line = null;
				while ((line = br.readLine()) != null) {
					total += line+ "\n";
					// System.out.println(line);
				}
				
//				System.out.println("charset: " + charset);
//				if(charset != null && !charset.toLowerCase().contains("utf")){
//					total = new String(total.getBytes(charset), "utf-8");
//				}
				pd = new PageData(total, host.toString(), host.toString()  + actualRequest.getURI().toString());

				br.close();
			
			}
			
			if(shutdown )client.getConnectionManager().shutdown();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pd;
	}
	
	private static boolean checkHost(String host) {
		if(host.endsWith("blog.csdn.net") && !host.contains("m.blog.csdn")) return true;
		else if(host.endsWith("cnblogs.com")) return true;
		return false;
	}

	public static <T extends TagNode> List<T> parseTags(String html, final Class<T> tagType, final String attr,final String value){
		return parseTags(html, tagType,attr ,value, false);
	}
	
	/**
	 * 
	 * @param <T> 标签类型
	 * @param html 需要解析的文本html
	 * @param tagType 标签类型 class
	 * @param attr 该标签应该有的树形
	 * @param value 属性的值 (Ϊnull ��Ϊ��ƥ��)
	 * @return
	 */
	public static <T extends TagNode> List<T> parseTags(String html, final Class<T> tagType, final String attr,final String value, final boolean isContain){
		Parser parser = new Parser();
		try {
			PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
			factory.registerTag(new PreTag());
			parser.setNodeFactory (factory);
			parser.setInputHTML(html);
			NodeList tagList = parser.parse(new NodeFilter() {
				@Override
				public boolean accept(Node node) {

					if(node.getClass() == tagType){
						if(attr == null) return true;
						T tn = (T)node;
						String attrv = tn.getAttribute(attr);
						if(value == null && attrv != null ){ //|| attrv.equals(value)
							return true;
						}
						if(isContain){
							if(value != null && attrv != null && attrv.contains(value)) return true;
						}else{
							if(value != null && attrv != null && attrv.equals(value)) return true;
						}
					}
					return false;
				}
			});
			
			List<T> tags = new ArrayList<T>();
			for(int i=0;i<tagList.size(); i++){
				tags.add((T)tagList.elementAt(i));
			}
			return tags;
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static NodeList parseAllTags(String html){
		Parser parser = new Parser();
		try {
			PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
			factory.registerTag(new PreTag());
			parser.setNodeFactory (factory);
			parser.setInputHTML(html);
			NodeList tagList = parser.parse(new NodeFilter() {
				@Override
				public boolean accept(Node node) {
					return true;
				}
			});
			
		
			return tagList;
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static <T extends TagNode> List<T> parseTags(Parser parser, final Class<T> tagType, final String attr,final String value){
		//Parser parser = new Parser();
		try {
			//parser.setInputHTML(html);
			NodeList tagList = parser.parse(new NodeFilter() {
				@Override
				public boolean accept(Node node) {
					
					if(tagType == null || node.getClass() == tagType){
						T tn = (T)node;
						String attrv = tn.getAttribute(attr);
						if(node instanceof Div)
							System.out.println(attrv);
						if(value == null && attrv != null ){ //|| attrv.equals(value)
							return true;
						}
						
						if(value != null && attrv != null && attrv.equals(value)) return true;
					}
					return false;
				}
			});
			
			List<T> tags = new ArrayList<T>();
			for(int i=0;i<tagList.size(); i++){
				tags.add((T)tagList.elementAt(i));
			}
			return tags;
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String[] inviod= new String[]{".", ",", ".", "!","@","#","$" ,"+", "'" ,  "*",  "^",  
		};
	public static String clearTitle(String title) {
		// TODO Auto-generated method stub
		title = clearChinese(title);
		String t = null;
		try {
			t = URLEncoder.encode(title.trim().replaceAll("\\s+", "-"), "utf-8");
			for(int i=0; i<inviod.length; i++)
				t = t.replaceAll("\\" + inviod[i], "");
			t = t.replaceAll("-+", "-"); //替换掉重复的连接符
			t = t.toLowerCase();
			if(t.length() > 0){
				while( ! checkWord( t.charAt(0))  )
					t = t.substring(1);
				while( ! checkWord( t.charAt( t.length()-1 ))  )
					t = t.substring(0, t.length()-1);
			}
			//t.replaceAll("", replacement)
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(t.trim().equals("")) return "blog";
		return t;
	}
	public static boolean checkWord(char c){
		return (c >= '0' && c <='9') || ( c >= 'a' && c <= 'z');
	}
	private static final String clearChinese(String str) { 
		List<Character> list = new ArrayList<Character>();
		str = str.toLowerCase();
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if( (c >= 'a' && c <= 'z') || c == ' ' || c == '-' || (c >= '0' && c <= '9') )list.add(str.charAt(i));
			else{
				char last = list.get(list.size()-1);
				if((last >= 'a' && last <= 'z') || (last >= '0' && last <= '9'))
					list.add('-');
			}
			//if( !isChinese(str.charAt(i))) list.add(str.charAt(i));
		}
		String res = "";
		for(Character c:list) res += c;
		return res;
	}
	private static final boolean isChinese(char c) { 
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
			    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
			    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
			    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
			    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
			    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 
	 * @param 
	 * @param 
	 * @return 从本地现存的文件中找答案
	 */
	
	public static String unescape(String s) {
	    int i=0, len=s.length();
	    char c;
	    StringBuffer sb = new StringBuffer(len);
	    while (i < len) {
	        c = s.charAt(i++);
	        if (c == '\\') {
	            if (i < len) {
	                c = s.charAt(i++);
	                if (c == 'u') {
	                    // TODO: check that 4 more chars exist and are all hex digits
	                    c = (char) Integer.parseInt(s.substring(i, i+4), 16);
	                    i += 4;
	                } // add other cases here as desired...
	            }
	        } // fall through: \ escapes itself, quotes any character but u
	        sb.append(c);
	    }
	    return sb.toString();
	}
	
	public static String getStringFromResponse(HttpResponse response)
	throws IOException, UnsupportedEncodingException {
String charset = null, total = "";
 HttpEntity entity = response.getEntity();
	if (entity != null) {
		if(charset == null)
		  charset = EntityUtils.getContentCharSet(entity);
		BufferedReader br = null;
		if(charset == null || charset == "")
			 br = new BufferedReader(new InputStreamReader(entity
				.getContent()));
		else
			 br = new BufferedReader(new InputStreamReader(entity
						.getContent(), charset));
		String line = null;
		while ((line = br.readLine()) != null) {
			total += line+ "\n";
			// System.out.println(line);
		}
	}
return total;
}
	
	public static void deleteFile(File file){ 
		   if(file.exists()){ 
		    if(file.isFile()){ 
		     file.delete();
		    }else if(file.isDirectory()){ 
		     File files[] = file.listFiles(); 
		     for(int i=0;i<files.length;i++){ 
		      deleteFile(files[i]); 
		     } 
		    } 
		    file.delete(); 
		   }else{ 
		    logger.warn("所删除的文件不存在！"+'\n'); 
		   } 
		} 
	
	public static HttpClient wrapClient(HttpClient base) { 
			        try { 
			            SSLContext ctx = SSLContext.getInstance("TLS"); 
			            X509TrustManager tm = new X509TrustManager() { 
			                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { 
			                } 
			                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { 
			                } 
			                public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
			                    return null; 
			                }

							@Override
							public void checkClientTrusted(
									java.security.cert.X509Certificate[] chain,
									String authType)
									throws java.security.cert.CertificateException {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void checkServerTrusted(
									java.security.cert.X509Certificate[] chain,
									String authType)
									throws java.security.cert.CertificateException {
								// TODO Auto-generated method stub
								
							}

							
							
			            }; 
			            ctx.init(null, new TrustManager[]{tm}, null); 
			            SSLSocketFactory ssf = new SSLSocketFactory(ctx); 
			            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
			            ClientConnectionManager ccm = base.getConnectionManager(); 
			            SchemeRegistry sr = ccm.getSchemeRegistry(); 
			            sr.register(new Scheme("https", ssf, 443)); 
			            return new DefaultHttpClient(ccm, base.getParams()); 
			        } catch (Exception ex) { 
			            ex.printStackTrace(); 
			            return null; 
		        } 
			    }

	public static String  getPlainHtml(String text){
		if(text == null) return "";
		String res = "", tmp;
		Scanner scanner = new Scanner(text);
		while(scanner.hasNextLine()){
			tmp = scanner.nextLine();
			String t = StringEscapeUtils.unescapeHtml4(tmp).replaceAll("[\\s\\u00A0]+$", "");
			if(t.trim().equals(""))continue;
			else{
				res += tmp +"<br>\n";
			}

		}
		return res;
	}
	
	
}

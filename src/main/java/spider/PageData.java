package spider;

public class PageData {
	
	public String html,host,url;

	public PageData(String html, String host, String url) {
		super();
		this.html = html;
		this.host = host;
		this.url = url;
	}
	public PageData(String host, String url) {
		this.host = host;
		this.url = url;
	}
	
	public PageData(){
		
	}
	
	
}

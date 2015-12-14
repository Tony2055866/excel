<%@ page import="util.StockUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>股票</title>
</head>
<body>

<%
  request.setCharacterEncoding("utf-8");
  String name = request.getParameter("name");
  String passwd = request.getParameter("passwd");
  String stock = request.getParameter("stock");
  String update = request.getParameter("update");
  String getStock = request.getParameter("getStock");
  String email = request.getParameter("email");
  if(StringUtils.isEmpty(email)){
    email = "zhongshuangyi001@163.com";
  }
  if ("cron".equals(request.getParameter("myaction"))){
      boolean success = StockUtil.getStocksExcel();
      if(success) out.println("<script>alert('获取数据成功'</script>");
      else  out.println("<script>alert('获取数据失败'</script>");
      return;
  }
  
  if(name != null && passwd != null){
    if(name.equals("admin") && passwd.equals("zhongshuangyi")){
      session.setAttribute("stock", true);
    }
  }else if(stock != null && update!=null){
    boolean success = StockUtil.saveStock(stock);
    if(success) out.println("<script>alert('数据项错误，更新失败'</script>");
    else  out.println("<script>alert('更新成功'</script>");
    
  }else if(getStock != null){
    boolean success = StockUtil.getStocksExcel(email);
    if(success) out.println("<script>alert('获取数据成功'</script>");
    else  out.println("<script>alert('获取数据失败'</script>");
  }

  
if(session.getAttribute("stock") == null){
%>
  <form method="post">
    用户名： <input name="name" type="text" value="admin"> <br>
    密码： <input name="passwd" type="password" value=""> <br>
    <input type="submit" value="Login">
  </form>

<% }else {
  String oldStock = StockUtil.getStock();

%>
  <form method="post">
    <textarea name="stock" rows="20" cols="50"><%=oldStock%></textarea>
    <br>
    <input type="submit" name="update" value="更新股票"> <br><br>
    
    <br>
    
    接收邮箱:<input type="text" name="email"  value="zhongshuangyi001@163.com">
    <input type="submit" name="getStock" value="获取数据"> <br>
  </form>    

<%}
%>

</body>
</html>
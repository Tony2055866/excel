package util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.htmlparser.tags.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.MyUtil;
import spider.PageData;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gaotong1 on 2015/12/2.
 */
public class StockUtil {
    public static String STOCK_FOLDER = "/root/config/";
    public static String STOCK_FILE = "stock.txt";
    public static String EXCEL_FILE = "stock.xls";

    private static Logger logger = LoggerFactory.getLogger(StockUtil.class);
    static {
        if(Config.isWin){
            STOCK_FOLDER = "F:\\我的网站和app\\stock\\";
        }
    }
    public static String getStock() throws IOException {
        //logger.info("getStock: " + STOCK_FOLDER+STOCK_FILE);
        File file = new File(STOCK_FOLDER+STOCK_FILE);
        if(!file.exists()){
            file.createNewFile();
            return "";
        }else{
            return FileUtils.readFileToString(file,"UTF-8");
        }
    }
    
    static class Stock{
        String code;
        String name;
        String date;
        String lastPrice;
        String shiying;
        String jieya;
        String fenhong;

        @Override
        public String toString() {
            return "Stock{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", date='" + date + '\'' +
                    ", lastPrice='" + lastPrice + '\'' +
                    ", shiying='" + shiying + '\'' +
                    ", jieya='" + jieya + '\'' +
                    ", fenhong='" + fenhong + '\'' +
                    '}';
        }
    }
    
    public static List<Stock> getFromConfigFile(String stock){
        List<Stock> stocks = new ArrayList<Stock>();
        String datas[] = stock.split("\n");
        if(datas == null || datas.length == 0) return  null;
        for(String data:datas){
            String stockData[] = data.split(",");
            if(stockData.length != 2)
                stockData = data.split("，");
            if(stockData.length != 2){
                return null;
            }
            if(stockData[0] == null || stockData[0].length() != 6)
                return null;
            if(stockData[0] == null || stockData[1].length() == 0 )
                return null;
            
            Stock stock1 = new Stock();
            stock1.code = stockData[0];
            stock1.name = stockData[1];
            stocks.add(stock1);
        }
        return stocks;
    }

    public static boolean saveStock(String stock) throws IOException {
        List<Stock> stocks = getFromConfigFile(stock);
        if(stocks == null){
            return false;
        }
        File file = new File(STOCK_FOLDER+STOCK_FILE);
        logger.info("saveStock:" + stock);
        if(!file.exists()){
            file.createNewFile();
        }else{
            FileUtils.writeStringToFile(file, stock, "UTF-8");
        }
        return true;
    }
    public static boolean getStocksExcel() throws IOException {
        return getStocksExcel("zhongshuangyi001@163.com");
    }
    public static boolean getStocksExcel(String email) throws IOException {
        initExcel();
        boolean success = true;
        File file = new File(STOCK_FOLDER +STOCK_FILE );
        String stockData = FileUtils.readFileToString(file, "utf-8");
        //logger.info("getStocksExcel:" + stockData);
        List<Stock> stocks = getFromConfigFile(stockData);
        if(stocks == null){
            logger.error("getFromConfigFile error.data:" + stockData);
            return false;
        }
        
        int col = 0;
        String  failStocksString = "";
        for(Stock stock:stocks){
            String fullCode = getFullStockCode(stock.code);
            if(fullCode != null && fullCode.length() == 8){
                stock = getStockFromWeb(fullCode, stock);
                if(stock == null){
                    success = false;
                    failStocksString += stock.code + ",";
                    continue;
                }
                writeStockToExcel(++col, stock);
            }else{
                logger.error("getFullStockCode error. code:{}, fullCode:{}", new Object[]{stock.code, fullCode});
            }
        }
        File newFile = new File(STOCK_FOLDER + Config.sdf.format(new Date()) + "-" +EXCEL_FILE);
        FileOutputStream fos = new FileOutputStream(newFile);
        workbook.write(fos);
        workbook.close();
        fs.close();
        fos.close();
        String content = "hello world. success";
        if(!success) content = "hello world. failed stocks: " + failStocksString;
        if(StringUtils.isEmpty(email)) email = "zhongshuangyi001@163.com";
        MailUtil.doSendHtmlEmail("股票信息v2-" + Config.sdfRead.format(new Date()), content, email, newFile);
        return true;
    }

    private static POIFSFileSystem fs;
    private static HSSFWorkbook workbook;
    private static HSSFSheet sheet;
    private static void initExcel() {
        File srcFile = new File(STOCK_FOLDER+EXCEL_FILE);
        try {
             fs = new POIFSFileSystem(new FileInputStream(srcFile));
             workbook = new HSSFWorkbook(fs);
             sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private static void writeStockToExcel(int col, Stock stock) {
       Cell namecell = getAndCreateCell(0, col);
        namecell.setCellValue(stock.name);
        
        Cell codeCell = getAndCreateCell(1, col);
        codeCell.setCellValue("【" +stock.code + "】");
        
        Cell cell = getAndCreateCell(2, col);
        cell.setCellValue(stock.date);

         cell = getAndCreateCell(5, col);
        cell.setCellValue(stock.lastPrice);
        
        cell = getAndCreateCell(6, col);
        cell.setCellValue(stock.shiying);

        cell = getAndCreateCell(9, col);
        cell.setCellValue(stock.jieya);

        cell = getAndCreateCell(10, col);
        cell.setCellValue(stock.fenhong);
    }

    private static Cell getAndCreateCell(int r,int col){
        Row row = sheet.getRow(r);
        Cell cell = row.getCell(col);
        if(cell == null){
            cell = row.createCell(col);
        }
        return cell;
    }
    
    public static String getFullStockCode(String code){
        PageData pageData = MyUtil.getPage("http://quote.eastmoney.com/" + code + ".html");
        logger.info(pageData.html);
        if(pageData != null && pageData.html != null && pageData.html.contains("location.href")){
            String fullCode = StringUtils.substringBetween(pageData.html,"location.href='",".html");
            if(fullCode.startsWith("..")){
                fullCode = fullCode.substring(3);
            }
            return  fullCode;
        }
        return null;
    }
    
    public static Stock getStockFromWeb(String fullCode, Stock stock){
        int type = 1;
        if(fullCode.startsWith("sz")) type = 2;
        PageData pageData = MyUtil.getPage("http://nuff.eastmoney.com/EM_Finance2015TradeInterface/JS.ashx?id=" + fullCode.substring(2) + type);
        if(pageData != null && pageData.html != null){
            String data = StringUtils.substringBetween(pageData.html, "\"Value\":[", "]})");
            logger.info(fullCode + " data:" + data);
            if(data == null || data.length() < 20){
                logger.error(fullCode + " err data:" + data);
            }else {
                data = data.replaceAll("\"","");
                String datas[] = data.split(",");
                if(datas.length != 51){
                    logger.error(fullCode + " err data:" + data);
                }else {
                    stock.name = datas[2];
                    if(Double.parseDouble(datas[25]) != 0.0){
                        stock.lastPrice = datas[25];
                    }else {
                        stock.lastPrice = datas[34];
                    }
                    stock.shiying = datas[38];
                }
            }
        }else{
            return null;
        }
        
        String fenhong = "无";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        stock.date = simpleDateFormat.format(date);
        
        pageData = MyUtil.getPage("http://f10.eastmoney.com/f10_v2/BonusFinancing.aspx?code=" + fullCode);
        if(pageData != null && pageData.html != null){
            List<TableColumn> cells = MyUtil.parseTags(pageData.html, TableColumn.class, "class", "bonusTime tips-dataL");
            if(cells != null){
                for(TableColumn column:cells){
                    //logger.info("debug cell:" + column.getStringText());
                    if(Config.sdfRead.format(date).equals(column.getStringText().trim())){
                        fenhong = "有";
                    }
                }
            }else{
                logger.error("get fenhong, data:" + pageData.html);
                fenhong = "";
            }
            
        }else{
            logger.error("get fenhong, data:" + pageData.html);
            fenhong = "";
        }
        stock.fenhong = fenhong;

        String jieya = "无";
        pageData = MyUtil.getPage("http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=" + fullCode);
        if(pageData != null && pageData.html != null){
            String html = StringUtils.substringBetween(pageData.html, "十大股东持股变动</strong>", "<strong>基金持股");
            //logger.info("html:" + html);
            if(html != null && html.length() >= 0){
                List<TableColumn> cells = MyUtil.parseTags(html, TableColumn.class, "class", "tips-dataL");
                if(cells != null){
                    for(TableColumn column:cells){
                        //logger.info("debug cell:" + column.getStringText());
                        if(Config.sdfRead.format(date).equals(column.getStringText().trim())){
                            jieya = "有";
                        }
                    }
                }else{
                    logger.error("get jieya, data:" + html);
                    jieya = "";
                }
            }else{
                logger.error("get jieya, data:" + pageData.html);
                jieya = "";
            }

        }else{
            logger.error("get jieya, data:" + pageData.html);
            jieya = "";
        }
        stock.jieya = jieya;

        logger.info("stock:{},  toString:{}", new Object[]{fullCode, stock.toString()});
        return stock;
    }


   
    public static void main(String args[]){
        /*initExcel();
        Date date = new Date();
        date.setMonth(7);
        date.setDate(12);
        System.out.println(util.Config.sdfRead.format(date));*/
       /* Row row = sheet.getRow(0);
        Cell cell = row.getCell(1);
        System.out.println(cell.getStringCellValue().toString());

        Cell cell2 = row.getCell(2);
        if (cell2 == null) {
            cell2 = row.createCell(2);
            
        }
        final CellStyle newCellStyle =
                workbook.createCellStyle();
        newCellStyle.cloneStyleFrom(cell2.getCellStyle());
        
        cell2.setCellStyle(cell.getCellStyle());
        cell2.setCellValue(cell.getRichStringCellValue());
        
        try {
            workbook.write(new FileOutputStream(new File(STOCK_FOLDER + util.Config.sdf.format(new Date()) + "-" +EXCEL_FILE)));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        System.out.println( getFullStockCode("600619"));
       /* Stock stock = new Stock();
        getStockFromWeb("sh600022", stock);*/
    }
}

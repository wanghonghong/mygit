package com.jm.staticcode.util.csv;

import com.jm.repository.jpa.JdbcUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>csv导入导出</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/12/10 16:39
 */
@Repository
public class CsvToolUtil {
    @Autowired
    protected  JdbcUtil jdbcUtil;

    /**
     *
     * @param sql  需要查询的sql
     * @param header  表头 例：列表1,列表2,列表3,列表4,
     * @param titleName 文件名字
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation("导出")
    public  void exportCsv(String sql, String header,String titleName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Object[]> obj = new ArrayList<>();
        CsvWriter writer = null;
        String[] headerA = header.split(","); //表头  ','分割		//CSV文件的第一行内容是
        response.reset();
        response.setContentType("application/octet-stream;charset=GBK");
        response.setHeader("content-disposition", "attachment; filename="
                + java.net.URLEncoder.encode(titleName, "UTF-8") + ".csv");
        OutputStream os = response.getOutputStream();
        writer = new CsvWriter(os, ',', Charset.forName("GBK"));
        for(int h = 0 ; h < headerA.length; h ++){
            writer.write(headerA[h], true);
        }
        writer.endRecord(); // 结束写入
        List<Map<String, Object>> list = jdbcUtil.queryList(sql);
        for (Map object :list) {
            Object[] values =  object.values().toArray();
            obj.add(values);
        }
        String[] cols = null;
        for(Object[] s : obj){
            //循环该行所有列
            cols = new String[s.length]; //s.length  代表有多少列
            // 循环列 写入
            for (int j=0; j<s.length; j++) {
                cols[j] = ("" + s[j]).equals("null") ? "" : "" + s[j] ;

                cols[j] =  cols[j].replaceAll(",", " ")
                        .replaceAll("\r\n", " ")
                        .replaceAll("\r", " ")
                        .replaceAll("\n", " ")+"\t";
                writer.write(cols[j], true);
            }
            writer.endRecord();
        }
        //写完关闭
        writer.flush();
        writer.close();
        os.flush();
        os.close();
    }

    /**
     * 根据list对象来导出
     * @param list
     * @param header
     * @param titleName
     * @param response
     * @throws IOException
     */
    @ApiOperation("导出")
    public  void exportCsv(List<Map<String, Object>> list, String header,String titleName, HttpServletResponse response) throws IOException {
        List<Object[]> obj = new ArrayList<>();
        CsvWriter writer = null;
        String[] headerA = header.split(","); //表头  ','分割		//CSV文件的第一行内容是
        response.reset();
        response.setContentType("application/octet-stream;charset=GBK");
        response.setHeader("content-disposition", "attachment; filename="
                + java.net.URLEncoder.encode(titleName, "UTF-8") + ".csv");
        OutputStream os = response.getOutputStream();
        writer = new CsvWriter(os, ',', Charset.forName("GBK"));
        for(int h = 0 ; h < headerA.length; h ++){
            writer.write(headerA[h], true);
        }
        writer.endRecord(); // 结束写入
        for (Map object :list) {
            Object[] values =  object.values().toArray();
            obj.add(values);
        }
        String[] cols = null;
        for(Object[] s : obj){
            //循环该行所有列
            cols = new String[s.length]; //s.length  代表有多少列
            // 循环列 写入
            for (int j=0; j<s.length; j++) {
                cols[j] = ("" + s[j]).equals("null") ? "" : "" + s[j] ;

                cols[j] =  cols[j].replaceAll(",", " ")
                        .replaceAll("\r\n", " ")
                        .replaceAll("\r", " ")
                        .replaceAll("\n", " ")+"\t";
                writer.write(cols[j], true);
            }
            writer.endRecord();
        }
        //写完关闭
        writer.flush();
        writer.close();
        os.flush();
        os.close();
    }

    @ApiOperation("读取csv数据")
    public static String importCsv(InputStream inputStream){
        String dataList= "";
        BufferedReader br=null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(
                            inputStream
                    )
            );
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                if(info[0].contains("\t")){
                    String orderNum = info[0].trim().substring(1,info[0].trim().length()-2);
                    dataList += orderNum + ",";
                }
            }
            if(dataList.length()>0){
                dataList = dataList.substring(0,dataList.length()-1);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }

}

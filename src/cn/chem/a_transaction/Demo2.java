package cn.chem.a_transaction;

import cn.chem.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Description:市辖区名称单独导入(有重叠)，使用父级市的经纬度
 * @Author:guofeng
 * @Date: 2019/5/23
 */
public class Demo2 {

    public static void main(String[] args) {
        Connection conn = null;
        Connection conn2 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        ResultSet rs6 = null;
        try {
            // 获得连接
            conn = JDBCUtils.getConnection();
            // 获得发送sql的对象
            String sql2 = test2();
            stmt2 = conn.prepareStatement(sql2);
            // 执行查询sql
            rs2 = stmt2.executeQuery();// 查询目标一级数据
            while (rs2.next()) {
                String areaId = rs2.getString("AREA_ID");
                String areaName = rs2.getString("AREA_NAME");
                String parentAreaCode = rs2.getString("PARENT_AREA_CODE");
                String sql5 = test5(parentAreaCode);// 查询主表父级，单条数据
                stmt5 = conn.prepareStatement(sql5);
                rs5 = stmt5.executeQuery();
                System.out.println("AREA_ID & AREA_NAME ="+areaId+","+areaName);
                String parentAreaName = "";
                // 获取第一条数据
                if (rs5.next()) {
                    parentAreaName = rs5.getString("AREA_NAME");// 市名称
                    System.out.println("parentAreaName ="+parentAreaName);
                    //break;
                }
                // 根据市名称查询关联表，单条数据
                String sql4 = test4(parentAreaName);
                stmt4 = conn.prepareStatement(sql4);
                rs4 = stmt4.executeQuery();// 查询关联市表数据
                if (rs4.next()){
                    String lng = rs4.getString("lng");// 经度
                    String lat = rs4.getString("lat");// 纬度
                    // 同级地区名相同，校验父级名称是否相同,相同则将关联表经纬度插入主表
                    String sql3 = test3(areaId,lng,lat);
                    stmt3 = conn.prepareStatement(sql3);
                    stmt3.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(conn, stmt);
        }
    }


    /**
     * 查询主表父级，单条数据
     * @param parentAreaCode
     * @return
     */
    private static String test5(String parentAreaCode) {
        String sql = "select * from d_area where AREA_CODE = '"+parentAreaCode+"'";
        return sql;
    }

    /**
     * 查询关联表的区域名
     * @param areaName
     * @return
     */
    private static String test4(String areaName) {
        String sql = "select * from region where name = '"+areaName+"'";
        return sql;
    }

    /**
     * 更新
     * @param areaId
     * @param lng
     * @param lat
     * @return
     */
    public static String test3(String areaId, String lng, String lat){
        String sql = "update d_area set LONGITUDE ='"+lng+"', LATITUDE = '"+lat+"' where AREA_ID = '"+areaId+"'";
        System.out.println("test3 sql=:"+sql);
        return sql;
    }

    /**
     * 查询
     * @return
     */
    public static String test2(){
        String sql = "select * from d_area where AREA_NAME = '市辖区'";
        return sql;
    }

}

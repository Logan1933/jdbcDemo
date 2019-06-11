package cn.chem.a_transaction;

import cn.chem.utils.JDBCUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Description:拆分公司经纬度
 * @Author:guofeng
 * @Date: 2019/5/23
 */
public class Demo4 {

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
            // 执行查询sqld
            rs2 = stmt2.executeQuery();// 查询目标一级数据
            while (rs2.next()) {
                String id = rs2.getString("id");
                String deptNamePosition = rs2.getString("DeptNamePosition");
                if (deptNamePosition != null && !"".equals(deptNamePosition)){
                    String[] split = deptNamePosition.split(",");
                    String latitude = split[0];
                    String longitude = split[1];
                    String sql3 = test3(id, latitude, longitude);
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

    // 更新
    public static String test3(String id, String latitude, String longitude){
        String sql = "update t_jg_supervisiondeptname set latitude ='"+latitude+"', longitude = '"+longitude+"' where id = '"+id+"'";
        System.out.println("test3 sql=:"+sql);
        return sql;
    }

    //查询
    public static String test2(){
        String sql = "select * from t_jg_supervisiondeptname";
        return sql;
    }

}

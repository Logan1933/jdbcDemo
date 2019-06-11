package cn.chem.a_transaction;

import cn.chem.utils.JDBCUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Description:根据身份证计算年龄
 * @Author:guofeng
 * @Date: 2019/5/23
 */
public class Demo3 {

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
            rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                String transport_id = rs2.getString("transport_id");
                String transport_id_card = rs2.getString("transport_id_card");
                if (transport_id_card != null && !"".equals(transport_id_card)){
                    String ageYear = transport_id_card.substring(6, 10);
                    BigDecimal decimal_2019 = new BigDecimal(2019);
                    BigDecimal age = decimal_2019.subtract(new BigDecimal(ageYear));
                    String sql3 = test3(transport_id,age.toString());
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
    public static String test3(String id, String age){
        String sql = "update d_transport set driver_age ='"+age+"' where transport_id = '"+id+"'";
        System.out.println("test3 sql=:"+sql);
        return sql;
    }

    //查询
    public static String test2(){
        String sql = "select * from d_transport";
        return sql;
    }

}

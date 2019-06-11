package cn.chem.a_transaction;

import cn.chem.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Description:公司人员关系表基础数据整理
 * @Author:guofeng
 * @Date: 2019/5/23
 */
public class Demo5 {

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
                String EmployeeID = rs2.getString("EmployeeID");//人员id
                String EmployeeName = rs2.getString("EmployeeName");//人员姓名
                String SupervisionDeptName = rs2.getString("SupervisionDeptName");//人员负责该公司list
                if (SupervisionDeptName != null && !"".equals(SupervisionDeptName)){
                    String[] split = SupervisionDeptName.split(",");
                    //根据公司名称查询公司信息，并插入新的表中
                    for (String companyName : split){
                        String sql3 = test3(companyName);
                        stmt3 = conn.prepareStatement(sql3);
                        // 执行查询sqld
                        rs3 = stmt3.executeQuery();// 查询目标一级数据
                        while (rs3.next()){
                            //插入
                            String DeptID = rs3.getString("DeptID");//公司id
                            String DeptName = rs3.getString("DeptName");//公司id
                            String sql4 = test4(EmployeeID, EmployeeName, DeptID, DeptName);
                            stmt4 = conn.prepareStatement(sql4);
                            stmt4.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(conn, stmt);
        }
    }

    // 插入
    public static String test4(String EmployeeID, String EmployeeName, String DeptID, String DeptName){
        String sql = "INSERT INTO t_staff_company_rel VALUES (null, '"+EmployeeID+"', '"+EmployeeName+"','"+DeptID+"','"+DeptName+"');";
        System.out.println("test4 sql=:"+sql);
        return sql;
    }

    //查询
    public static String test3(String DeptName){
        String sql = "select * from t_jg_supervisiondeptname where DeptName = '"+DeptName+"'";
        return sql;
    }

    //查询
    public static String test2(){
        String sql = "select * from t_jg_employee";
        return sql;
    }

}

package cn.chem.a_transaction;


/**
 * @Description:
 * @Author:guofeng
 * @Date: 2019/5/27
 */
public class Test {
    public static void main(String[] args) {
        String DeptNamePosition = "39.011646,121.555145";
        String[] split = DeptNamePosition.split(",");
        for (String str : split){
            System.out.println("str=:"+str);
        }
    }
}

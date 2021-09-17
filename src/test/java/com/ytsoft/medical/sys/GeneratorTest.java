package com.demo.sys;

//import com.demo.common.CodeGenerator;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneratorTest {

//    public static void main(String[] args) {
//        String jdbc_diver_class = "oracle.jdbc.driver.OracleDriver";
//        String jdbc_url = "jdbc:oracle:thin:@192.168.1.30:1521:slyyhis";
//        String jdbc_username = "shyt";
//        String jdbc_password = "shyt010203";
//        String base_package = "com.demo.sys";//项目基础包名称，根据自己公司的项目修改
//        String model = "sys";//模块
//        String project_path = System.getProperty("user.dir");//项目在硬盘上的基础路径
////        project_path = "D:/workSpace1/demo.sys";
//        CodeGenerator generator = new CodeGenerator(base_package, project_path, jdbc_diver_class, jdbc_url, jdbc_username, jdbc_password, model);
//        String[] tables = {"customer_form_relation"};
//        Arrays.stream(tables).forEach(table -> {
////          这里生成表对应的文件名所以第一个字母大写
//            String modelName = CodeGenerator.tableNameConvertLowerCamel(table,true);
//            generator.generatorCode(table, modelName);
//        });
//
//    }

}

package com.hdvon.generator.main;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

public class GeneratorMain {
	/**
	 * 请直接修改以下代码调用不同的方法以执行相关生成任务.
	 */
	public static void main(String[] args) throws Exception {
        Generator busCodeGenerator = new Generator();
		busCodeGenerator.addTemplateRootDir(getBaseDir()+"\\"+"template");
		busCodeGenerator.setOutRootDir(getBaseDir()+"\\"+GeneratorProperties.getProperty("outRoot"));
		GeneratorFacade generatorFacade = new GeneratorFacade();
		generatorFacade.setGenerator(busCodeGenerator);
		generatorFacade.deleteOutRootDir();
		/**设置开发人员姓名**/
        GeneratorProperties.setProperty("creator","huanhongliang");
        /**设置生成表，多表用逗号分隔**/
		generatorFacade.generateByTable("t_sip_log");
	}

	private static String getBaseDir(){
        /*修复idea和eclipse的user.dir不一致问题*/
        String projectDir = System.getProperty("user.dir");
        String projectName = "nmp-generator";
        projectDir += projectDir.contains(projectName)? "":"\\"+ projectName;
        System.out.println(projectDir);
        return projectDir;
    }
}

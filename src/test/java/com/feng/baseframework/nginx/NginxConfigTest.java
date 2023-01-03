package com.feng.baseframework.nginx;

import com.feng.baseframework.util.NetUtil;
import com.feng.baseframework.util.ShellUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * baseframework
 * 2019/5/28 15:18
 * 测试nginx配置文件
 *
 * 1.如何确保读写nginx配置文件线程安全：生成外部nginx配置文件，采用include /etc/nginx/soc/conf/*.conf方式导入配置
 * 2.如何备份和还原
 * 3.检测端口是否可用
 * 4.开放防火墙
 * 5.重启nginx
 *
 * @author lanhaifeng
 * @since
 **/
public class NginxConfigTest {

	@Test
	public void backupNginxConfig(){

	}

	@Test
	public void generateNginxListenPort(){
		Assert.assertTrue("端口已占用，工具类错误", NetUtil.isHostConnectable("192.168.230.205", 80));
		Assert.assertTrue("端口未占用，工具类错误", !NetUtil.isHostConnectable("192.168.230.205", 12346));
	}

	@Test
	public void openPort(){
		String port = "";
		String openPortCmd = "sudo iptables -A INPUT -p tcp --dport " + port + " -j ACCEPT";
		ShellUtils.execShell(openPortCmd);

		String startPort = "30001";
		String endPort = "30001";
		openPortCmd = "sudo iptables -A INPUT -p tcp --dport "+ startPort+":"+endPort+" -j ACCEPT";
		ShellUtils.execShell(openPortCmd);

		openPortCmd = "sudo iptables -A OUTPUT -p tcp --sport "+ startPort+":"+endPort+" -j ACCEPT";
		ShellUtils.execShell(openPortCmd);
	}

	@Test
	public void generateProxyNginxConfig() throws IOException {
		String host = "192.168.230.230";
		Integer proxyPort = 1201;
		Integer forwardPort = 443;
		String fileTempPostfix = ".temp";
		String filePostfix = ".conf";
		String filePrefix = "D:/etc/nginx/soc/conf/";
		String tempFilePath = filePrefix + "192.168.230.230".replaceAll("\\.", "_") + fileTempPostfix;
		String filePath = filePrefix + "192.168.230.230".replaceAll("\\.", "_") + filePostfix;

		String nginxConfig = "    server {\n" +
				"        listen       ${_proxyPort} ssl;\n" +
				"        server_name  _;\n" +
				"        \n" +
				"        ssl_certificate     /etc/nginx/ssl/capaa.mchz.com.crt;\n" +
				"        ssl_certificate_key /etc/nginx/ssl/capaa.mchz.com.key;\n" +
				"\n" +
				"        ssl_prefer_server_ciphers on;\n" +
				"        ssl_session_cache shared:SSL:10m;\n" +
				"\n" +
				"        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;\n" +
				"        ssl_ciphers ECDH+AESGCM:DH+AESGCM:ECDH+AES256:DH+AES256:ECDH+AES128:DH+AES:RSA+AESGCM:RSA+AES:!aNULL:!MD5:!DSS;\n" +
				"\n" +
				"        location / {\n" +
				"            proxy_redirect off;\n" +
				"            proxy_set_header Host $http_host;\n" +
				"            proxy_set_header X-Real-IP $remote_addr;\n" +
				"            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n" +
				"            proxy_pass https://${_forwardIp}:{_forwardPort}/;\n" +
				"        }\n" +
				"\n" +
				"        location /capaa {\n" +
				"            proxy_redirect off;\n" +
				"            proxy_set_header Host $http_host;\n" +
				"            proxy_set_header X-Real-IP $remote_addr;\n" +
				"            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n" +
				"            proxy_pass https://${_forwardIp}:{_forwardPort}/capaa;\n" +
				"        }    \n" +
				"    }\n"
				;
		nginxConfig = nginxConfig.replaceAll("\\$\\{_proxyPort}", proxyPort.toString()).replaceAll("\\$\\{_forwardPort}", forwardPort.toString()).replaceAll("\\$\\{_forwardIp;}", host);

		File file = new File(filePrefix);
		if(!file.exists()){
			file.mkdirs();
		}

		RandomAccessFile rf = new RandomAccessFile(tempFilePath, "rw");
		rf.writeBytes(nginxConfig);
		rf.close();

		System.out.println(nginxConfig);
	}

	@Test
	public void rewriteNginxConfig() throws IOException {
		String fileName = "D:\\tools\\nginx-1.13.12\\conf\\nginx.conf";
		String startFlag = "#soc forward flag\n";
		RandomAccessFile raf = new RandomAccessFile(fileName,"rw");
		String line = null;
		int i = 0;
		while ((line = raf.readLine()) != null){
			i++;
			if(line.contains(startFlag)){
				break;
			}
		}
		System.out.println(i);

		String start = "\n#soc ${_host} forward start\n";
		String end = "#soc ${_host} forward end\n";
		String nginxConfig = start +
				"    server {\n" +
				"        listen       ${_port} ssl;\n" +
				"        server_name  _;\n" +
				"        \n" +
				"        ssl_certificate     /etc/nginx/ssl/capaa.mchz.com.crt;\n" +
				"        ssl_certificate_key /etc/nginx/ssl/capaa.mchz.com.key;\n" +
				"\n" +
				"        ssl_prefer_server_ciphers on;\n" +
				"        ssl_session_cache shared:SSL:10m;\n" +
				"\n" +
				"        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;\n" +
				"        ssl_ciphers ECDH+AESGCM:DH+AESGCM:ECDH+AES256:DH+AES256:ECDH+AES128:DH+AES:RSA+AESGCM:RSA+AES:!aNULL:!MD5:!DSS;\n" +
				"\n" +
				"        location / {\n" +
				"            proxy_redirect off;\n" +
				"            proxy_set_header Host $http_host;\n" +
				"            proxy_set_header X-Real-IP $remote_addr;\n" +
				"            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n" +
				"            proxy_pass https://${_host}/;\n" +
				"        }\n" +
				"\n" +
				"        location /capaa {\n" +
				"            proxy_redirect off;\n" +
				"            proxy_set_header Host $http_host;\n" +
				"            proxy_set_header X-Real-IP $remote_addr;\n" +
				"            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n" +
				"            proxy_pass https://${_host}/capaa;\n" +
				"        }    \n" +
				"    }\n" +
				end;

		System.out.println(nginxConfig.replaceAll("\\$\\{_port}", "1201").replaceAll("\\$\\{_host}", "192.168.230.230"));


	}

	@Test
	public void reloadNginxConfig(){
		String reloadCmd = "sudo service nginx reload";
		ShellUtils.execShell(reloadCmd);
	}
}

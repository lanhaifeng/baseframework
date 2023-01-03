相关命令
````
keytool 

-genkey 

-alias tomcat(别名) 

-keypass 123456(别名密码) 

-keyalg RSA(生证书的算法名称，RSA是一种非对称加密算法) 

-keysize 1024(密钥长度,证书大小) 

-validity 365(证书有效期，天单位) 

-keystore W:/tomcat.keystore(指定生成证书的位置和证书名称) 

-storepass 123456(获取keystore信息的密码)

-storetype (指定密钥仓库类型) 

````

1.生成三个秘钥库(根秘钥库rootca123、服务端秘钥库server123、客户端秘钥库client123)

````
keytool -genkeypair -alias rootca -keyalg RSA -validity 3650 -keystore rootca.keystore -dname "CN=rootca,OU=feng,O=feng,L=hz,ST=zj,C=zg"

keytool -genkeypair -alias server -keyalg RSA -validity 3650 -keystore server.keystore -dname "CN=server,OU=feng,O=feng,L=hz,ST=zj,C=zg"

keytool -genkeypair -alias client -keyalg RSA -validity 3650 -keystore client.keystore -dname "CN=client,OU=feng,O=feng,L=hz,ST=zj,C=zg"
````


2.导出根证书
````
keytool -exportcert -alias rootca -file rootca.cer -keystore rootca.keystore
````


3.导出待签名证书文件
````
keytool -certreq -alias server -file server.csr -keystore server.keystore

keytool -certreq -alias client -file client.csr -keystore client.keystore
````


4.使用根证书为待签名证书文件签名
````
keytool -gencert -alias rootca -validity 365 -infile server.csr -outfile server.cer -keystore rootca.keystore

keytool -gencert -alias rootca -validity 365 -infile client.csr -outfile client.cer -keystore rootca.keystore
````


5.将根证书和签名后的服务端证书导入服务端秘钥库
````
keytool -import -keystore server.keystore -file rootca.cer

keytool -importcert -alias server -file server.cer -keystore server.keystore
````


6.将根证书和签名后的客户端证书导入客户端秘钥库
````
keytool -import -keystore client.keystore -file rootca.cer

keytool -importcert -alias client -file client.cer -keystore client.keystore
````


7.生成可信赖证书库
````
keytool -import -keystore trust_cacerts.keystore -file rootca.cer
````


8.安装根证书  
将根证书安装到受信任的根证书颁发机构
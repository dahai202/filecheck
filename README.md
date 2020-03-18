# filecheck
文件对比小工具
这个是自动验证网贷系统导出数据和数仓导出数据（核心、信贷）的对比工具
打包为jar用shell启动，验证文件行匹配和各个字段匹配
echo '开始校验'
java -jar check.jar /data/xx1.dat /data/wsd/xx2.dat

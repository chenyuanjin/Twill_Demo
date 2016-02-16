#Apache Twill的简单Demo
----

##简介
- hadoop 2.x包含两部分YARN和HDFS,而MR等计算框架则成为建立YARN上的应用,YARN则成为一款云操作系统,任何应用只要实现YARN的指定的接口,就可以在YARN上分布式运行,关于一款应用如何接入YARN,可以参照这篇[博客](http://my.oschina.net/u/1434348/blog/193374),我们可以看出,在YARN上开发应用还是很复杂的,因此Apache Twill就诞生了,它将接入YARN的接口进行了进一步的包装,我们只用几行简单的代码就可以将自己的应用接入YRAN了,使我们可以更好的关注与自己的业务,而不是如何接入YRAN;Apache Twill的[官网](http://twill.incubator.apache.org/),关于twill的详细介绍可以看Apache will Presentation in ApacheCon 2014.本项目展示如何在IDEA建立一个twill的maven开发项目以及一些注意事项;

----

##搭建开发环境
- 安装hadoop,版本2.2以上,并且要用源码在自己的运行环境下编译,不要使用网上编译好的,关于hadoop的编译和安装网上有很多教程;
- 安装zookeeper,这个网上也有教程,照着装就可以;
- 按照官网上的教程测试下运行环境,如果前两步的环境搭建都没问题话,这一步一般不会出问题;
- 在linux或者mac下进行安装IDEA,JDK版本1.7及以上,进行twill工程的开发,因为linux和Windows的目录结构不一样,所以用windows开发会出很多问题;

##构建工程
- 将本项目克隆到本地,使用IntelliJ打开;
- 将hadoop的安装文件拷贝一份到本地,配置文件也要一致;
- 将hadoop的配置文件和lib/native下的文件都加到工程classpath下;
- 运行项目的demo,可以查看YARN容器的打印日志;
jar -cf framework.jar -C ~/NetBeansProjects/framework/build/web/WEB-INF/classes .
mv framework.jar ~/frameworkNetbeans/framework/sprint5/testFramework/build/web/WEB-INF/lib
jar -cf newProject.war -C ~/NetBeansProjects/testFramework/build/web . 
mv newProject.war  ~/servers/apache-tomcat-8.5.81/webapps




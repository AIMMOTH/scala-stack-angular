# SCALA STACK with AngularJS

Scala Stack serve HTML, JavaScript and CSS from Scala code! No frontend build (gulp, grunt), no files (js, css, html, sass, less)! No SBT since a servlet is compiling.

## Description

In this project, a servlet serve a HTML file build with ScalaTags, and another servlet serve JavaScript hot compiled from Scala JS source. 

## Environment

**This will be the last version of Scala Stack since it is build with**

- Google Standard Environment (Java 8) https://cloud.google.com/appengine/docs/standard/long-term-support#java8
- Scala 2.11.12 https://github.com/scala/scala/releases/tag/v2.11.12 
- AngularJS 1.8.2 https://docs.angularjs.org/misc/version-support-status 
- Foundation 6.2.3 https://github.com/foundation/foundation-sites/releases/tag/v6.7.4

It's a Maven project with Jetty and Jersey.

## Live

Check out [live](https://scala-stack-angular.appspot.com/)

## Run and Deploy

This project is dependent on a second project [scalajs-compiler-servlet](https://github.com/AIMMOTH/scalajs-compiler-servlet/tree/v0.3). Either clone and install it with maven or use copy of JAR included in src/main/resources.

Use maven and run > mvn appengine:run or > mvn appengine:deploy.

## Details

The purpose of Scala Stack is to:

- Write a whole web application with Scala
- Compile Javascript on request
- Don't use build tool for ScalaJS
- Don't use static files

This means that a unique web application can be created on each request!

### Source

Short description

- Package src/main/scala/jvm, contains all Scala backend code
- Folder src/main/webapp/scalajs-source/scalajs, is read by WebServletContextListener and all Scala(JS) files are on request compiled to JavaScript. It's also added as source folder for your IDE
- Folder src/main/webapp/scalajs-source/scalajs/angular, contains ScalaJS code for a AngularJS app
- Folder src/main/webapp/scalajs-source/scalajs/shared, contains  ScalaJS code for shared logic, compiled into JavaScript but also used as a source folder for backend compilation

As much as possible should be shared, like logic, routes, DTO, i18n, validation etc.

### Web Application on Request

Using shared route logic, backend will respond with different web applications

- Request '/', will redirect to '/en-gb/index.min.html'
- Request '/en-gb/index.min.html', will respond with an HTML page using minimized versions of JavaScript and CSS. Backend will compile ScalaJS using 'FULL' optimizer option
- Request '/en-gb/index.html', will not minimize the web application
- Request '/sv-se/*', will return Swedish web application
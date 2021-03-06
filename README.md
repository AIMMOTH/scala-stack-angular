# SCALA STACK with Angular

Scala Stack serve HTML, JavaScript and CSS from Scala code! No frontend build (gulp, grunt), no files (js, css, html, sass, less)! No SBT since a servlet is compiling.

## Description

In this project, a servlet serve a HTML file build with ScalaTags, and another servlet serve JavaScript hot compiled from Scala JS source. 

## Environment

Maven project with Google Standard Environment with Java 8, Servlet 3.1 and Scala 2.11.

## Live

Check out [live](https://scala-stack-angular.appspot.com/)

## Run and Deploy

This project is dependent on a second project [scalajs-compiler-servlet](https://github.com/AIMMOTH/scalajs-compiler-servlet/tree/v0.3). Either clone and install it with maven or use copy of JAR included in src/main/resources.

Use maven and run $ mvn gcloud:run or $ mvn gcloud:deploy. Make sure to have GCloud installed and Python on path!

Another way is to compile (use IDE like eclipse) and copy dependencies into webapp, and use the python to run "dev_appserver.py" included in GCloud SDK.

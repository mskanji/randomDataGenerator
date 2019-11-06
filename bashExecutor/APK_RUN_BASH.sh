#!/usr/bin/env bash

NAME=randomDataHGenerator
JARNAME=randomdatagenerator_2.11-01

PACKAGE=fr.laposte.randomDataGenerator
PATHPACK=$(subst .,/,$(PACKAGE))

.DUMMY: default
default: $(NAME)

.DUMMY: help
help:
    @echo "make [$(NAME)]"
    @echo "make [jar|runJar]"
    @echo "make [clean|distClean|cleanAllJars|cleanScalaJar|cleanAppJar]"

.PRECIOUS: bin/$(PATHPACK)/%.class

bin/$(PATHPACK)/%.class: src/$(PATHPACK)/%.scala
    scalac -sourcepath src -d bin $<

scala-library.jar:
    cp $(SCALA_HOME)/lib/scala-library.jar .

.DUMMY: runjar
runJar: jar
    java -jar $(JARNAME).jar

.DUMMY: jar
jar: $(JARNAME).jar

MANIFEST.MF:
    @echo "Main-Class: $(PACKAGE).$(NAME)" > $@
    @echo "Class-Path: scala-library.jar" >> $@

$(JARNAME).jar: scala-library.jar bin/$(PATHPACK)/$(NAME).class \
                                MANIFEST.MF
    (cd bin && jar -cfm ../$(JARNAME).jar ../MANIFEST.MF *)

%: bin/$(PATHPACK)/%.class
    scala -cp bin $(PACKAGE).$@

.DUMMY: clean
clean:
    rm -R -f bin/* MANIFEST.MF

cleanAppJar:
    rm -f $(JARNAME).jar

cleanScalaJar:
    rm -f scala-library.jar

cleanAllJars: cleanAppJar cleanScalaJar

distClean cleanDist: clean cleanAllJarsh.scala
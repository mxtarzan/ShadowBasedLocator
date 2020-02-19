#!/bin/sh

THE_CLASSPATH=
PROGRAM_FILE=Main.java
FILE1=Picture.java 
FILE2=SolarMath.java
FILE3=Multithread.java

PROGRAM_CLASS=Main.class
CLASS1=Picture.class
CLASS2=SolarMath.class
CLASS3=Multithread.class

case $1 in
   -c)
      echo "building program"

      cd src/base

      javac $PROGRAM_FILE $FILE1 $FILE2 $FILE3

      mv *.class ../../bin/base/

      if [ $? -eq 0 ]
      then
         echo "compile worked!"
      fi
      shift;;

   -x)
      echo "running program"

      cd bin/base
   
      jar cvf ImageBasedLocator.jar $PROGRAM_CLASS $CLASS1 $CLASS2 $CLASS3

      java -jar ImageBasedLocator.jar

      cd ../../src

      exec gnuplot --persist gnuscript
      shift;;

   -cx)
      echo "building program"

      cd src/base

      javac $PROGRAM_FILE $FILE1 $FILE2 $FILE3

      mv *.class ../../bin/base/ 

      if [ $? -eq 0 ]
      then
         echo "compile worked!"
      fi

      echo "running program"
      
      cd ../../bin/base
   
      jar cvf ImageBasedLocator.jar $PROGRAM_CLASS $CLASS1 $CLASS2 $CLASS3

      java -jar ImageBasedLocator.jar

      cd ../../src

      exec gnuplot --persist gnuscript
      shift;;
      
esac



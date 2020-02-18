#!/bin/sh

THE_CLASSPATH=
PROGRAM_FILE=Main.java
FILE1=Picture.java 
FILE2=SolarMath.java
FILE3=Multithread.java
FILE4=LocationApprox.java
FILE5=Window.java

PROGRAM_CLASS=Main.class
CLASS1=Picture.class
CLASS2=SolarMath.class
CLASS3=Multithread.class
CLASS4=LocationApprox.class
CLASS5=Window.class
case $1 in
   -c)
      echo "building program"

      cd src/base

      javac $PROGRAM_FILE $FILE1 $FILE2 $FILE3 $FILE4 $FILE5

      mv *.class ../../bin/base/

      if [ $? -eq 0 ]
      then
         echo "compile worked!"
      fi
      shift;;

   -x)
      echo "running program"

      cd bin/base
   
      jar cvf ImageBasedLocator.jar $PROGRAM_CLASS $CLASS1 $CLASS2 $CLASS3 $CLASS4 $CLASS5

      java -jar ImageBasedLocator.jar

      cd ../../src

      exec gnuplot --persist gnuscript
      shift;;

   -cx)
      echo "building program"

      cd src/base

      javac $PROGRAM_FILE $FILE1 $FILE2 $FILE3 $FILE4 $FILE5

      mv *.class ../../bin/base/ 

      if [ $? -eq 0 ]
      then
         echo "compile worked!"
      fi

      echo "running program"
      
      cd ../../bin/base
   
      jar cvf ImageBasedLocator.jar $PROGRAM_CLASS $CLASS1 $CLASS2 $CLASS3 $CLASS4 $CLASS5

      java -jar ImageBasedLocator.jar

      cd ../../src

      exec gnuplot --persist gnuscript
      shift;;
      
esac



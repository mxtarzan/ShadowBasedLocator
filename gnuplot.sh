#!/bin/sh

case $1 in
   -x)
      echo "Running Gnuplot..."
      exec gnuplot --persist gnuscript
      shift;;
esac



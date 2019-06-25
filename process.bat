#!/bin/bash

echo Path of file \(optional\)?
read path
echo Start date?
read startDate
echo Duration?
read duration
echo Threshold \(optional\)??
read threshold

java -jar "./target/parser.jar" -XX:TieredStopAtLevel=1 -noverify -Dfile.encoding=UTF-8 -Dspring.output.ansi.enabled=always --accesslog=${path} --startDate=${startDate} --duration=${duration} --threshold=${threshold}


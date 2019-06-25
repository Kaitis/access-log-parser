@echo off

set /p path="Path of file \(optional\)? "
set /p startDate="Start date? "
set /p duration="Duration? "
set /p threshold="Threshold \(optional\)? "

java -jar "./target/parser.jar" -XX:TieredStopAtLevel=1 -noverify -Dfile.encoding=UTF-8 -Dspring.output.ansi.enabled=always --accesslog=%path% --startDate=%startDate% --duration=%duration% --threshold=%threshold%


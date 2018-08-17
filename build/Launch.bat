@echo off

set "CURRENT_DIR=%~dp0%"

set "lib=%~dp0%\lib\"


pushd %CURRENT_DIR%

set JDK_PATH=%CD%\jre\bin\javaw.exe

set cp=".;%lib%nddd.jar;"

"%JDK_PATH%" -Xmx1024M -cp %cp% cn.com.spbun.nddd.NDDDLaunch %1
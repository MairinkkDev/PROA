@echo off
chcp 65001 > nul
title Simulador de Animal de Estimacao Virtual - Java

cd /d "%~dp0"

where javac >nul 2>nul
if errorlevel 1 (
    echo [ERRO] O compilador Java ^(javac^) nao foi encontrado no PATH do sistema.
    pause
    exit /b 1
)

if not exist "out" mkdir "out"
for /d %%D in ("out\*") do rmdir /s /q "%%D"
for %%F in ("out\*.class") do del /q "%%F"

dir /s /b "src\*.java" > sources.txt 2>nul
if errorlevel 1 (
    echo [ERRO] Nenhum arquivo .java encontrado dentro de src.
    if exist "sources.txt" del "sources.txt"
    pause
    exit /b 1
)

echo Compilando o projeto...
javac -encoding UTF-8 -d out @sources.txt
set "COMPILE_ERR=%errorlevel%"
del "sources.txt"

if not "%COMPILE_ERR%"=="0" (
    echo.
    echo [ERRO] Falha na compilacao. Corrija os erros acima antes de executar.
    pause
    exit /b 1
)

set "MAIN_CLASS="
if exist "out\petvirtual\Main.class" set "MAIN_CLASS=petvirtual.Main"
if not defined MAIN_CLASS if exist "out\petvirtual\service\Main.class" set "MAIN_CLASS=petvirtual.service.Main"
if not defined MAIN_CLASS if exist "out\Main.class" set "MAIN_CLASS=Main"

if not defined MAIN_CLASS (
    echo [ERRO] Arquivo Main.class nao encontrado apos a compilacao.
    echo Conteudo gerado em out:
    dir /s /b out
    pause
    exit /b 1
)

cls
java -Dfile.encoding=UTF-8 -cp "out" %MAIN_CLASS%

echo.
pause
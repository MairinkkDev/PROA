@echo off
setlocal EnableExtensions

REM Compila o projeto, roda os testes das regras de negocio e executa cada
REM cenario de testes/entradas/*.txt, gravando a saida em testes/logs/*.log.

REM Vai para a pasta raiz do projeto (um nivel acima da pasta testes).
cd /d "%~dp0.."
if errorlevel 1 (
    echo ERRO: nao foi possivel acessar a pasta raiz do projeto.
    pause
    exit /b 1
)

set "JAVA_OPTS=-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8"

echo Limpando compilacoes e logs anteriores...
if exist out rmdir /s /q out
if exist out-test rmdir /s /q out-test
if exist testes\logs rmdir /s /q testes\logs

mkdir out
mkdir out-test
mkdir testes\logs

echo Compilando o projeto...
if not exist src (
    echo ERRO: pasta src nao encontrada.
    pause
    exit /b 1
)
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out @fontes.txt
if errorlevel 1 (
    echo ERRO: falha ao compilar o projeto.
    del fontes.txt
    pause
    exit /b 1
)
del fontes.txt

echo Compilando os testes...
if not exist test (
    echo ERRO: pasta test nao encontrada.
    pause
    exit /b 1
)
dir /s /b test\*.java > testes_fontes.txt
javac -encoding UTF-8 -cp out -d out-test @testes_fontes.txt
if errorlevel 1 (
    echo ERRO: falha ao compilar os testes.
    del testes_fontes.txt
    pause
    exit /b 1
)
del testes_fontes.txt

echo.
echo == Testes das regras de negocio ==
java %JAVA_OPTS% -cp "out;out-test" hotel.TestesRegras > testes\logs\00_testes_regras.log 2>&1
type testes\logs\00_testes_regras.log
if errorlevel 1 (
    echo ERRO: os testes das regras de negocio falharam.
    pause
    exit /b 1
)

echo.
echo == Cenarios ponta a ponta ==
if not exist testes\entradas\*.txt (
    echo AVISO: nenhum arquivo .txt encontrado em testes\entradas.
    goto :fim
)

for %%F in (testes\entradas\*.txt) do call :executar_cenario "%%~fF" "%%~nF"

:fim
echo.
echo Testes finalizados. Logs salvos em testes\logs\
pause
exit /b 0

:executar_cenario
set "NOME=%~2"
set "ARGS=--eco"
echo %NOME% | findstr /i "demo" >nul
if not errorlevel 1 set "ARGS=--eco --demo"

java %JAVA_OPTS% -cp out hotel.Main %ARGS% < "%~1" > "testes\logs\%NOME%.log" 2>&1
if errorlevel 1 (
    echo ERRO ao executar o cenario %NOME%. Consulte testes\logs\%NOME%.log
) else (
    echo gerado: testes\logs\%NOME%.log
)
exit /b 0

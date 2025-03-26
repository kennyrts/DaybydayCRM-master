@echo off

set "root=%~dp0"
set "bin=%root%\bin"
set "lib=%root%\lib"
set "temp=%root%\temp"
set "src=%root%\src"

:: copy all java files to temp directory
for /r "%src%" %%f in (*.java) do (
    xcopy "%%f" "%temp%"
)

:: move to temp to compile all java file
cd "%temp%"
javac -d "%bin%" -cp "%lib%\*" *.java

cd  "%bin%
java main.Main

pause

:: move back to root
cd %root%

:: remove temp
rmdir /s /q "%temp%"

:: remove bin content
del /s /q "%bin%\*"
for /d %%p in ("%bin%\*") do rmdir /s /q "%%p"

echo Test complete.
pause
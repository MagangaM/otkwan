@echo off
echo Compiling Library System...
javac -cp "lib/mysql-connector-java-9.5.0.jar" -d bin src/LibrarySystem/*.java
if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Running application...
    java -cp "bin;lib/mysql-connector-java-9.5.0.jar" LibrarySystem.LibrarySystemGUI
) else (
    echo Compilation failed!
    pause
)
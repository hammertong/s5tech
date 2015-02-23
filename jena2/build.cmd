@echo off
cd /d %0\..
call ant\bin\ant rebuild
call ant\bin\ant dist

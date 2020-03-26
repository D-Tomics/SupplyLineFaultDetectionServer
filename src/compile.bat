@echo off
cls
set /p classLoc=enter class location?

:start
    cls
    set /p file=enter file name?
    IF "%file%"=="/e" (EXIT)
    IF "%file%"=="/r" (
        set /p f=enter file to run?
        set /p ar=enter args?
        echo running...
        java -cp %classLoc% %f% %ar%
        echo exit status 0
        pause
        goto start
    )
    IF NOT EXIST %file%.java (
        echo %file%.java DOESNT EXIST
        pause
        goto start
    )

    echo compiling...
    javaC -d %classLoc% %file%.java
    echo compiled
    pause
goto start
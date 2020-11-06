call "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat" && cd ..
call mvnw clean package -Pnative -Dquarkus.profile=native_windows
pause
if "%cd:~-3%" == "bin" cd..
set "chatbot=%cd%"  
"%chatbot%\bin\%ARCH%\chatbot.exe" //IS//chatbot  --DisplayName="chatbot" --Install="%chatbot%\bin\chatbot.exe" --Classpath=".;%chatbot%/lib/*" --Jvm=auto --StartMode=jvm --StopMode=jvm  --StartClass=za.co.chris.Bootstrap --StartParams="start %chatbot%" --StopClass=za.co.chris.Bootstrap --StopParams=stop --StopTimeout=10 --LogPath="%chatbot%\log\deamon" --StdOutput="%chatbot%\log\chatbotSTDOUT.txt" --StdError="%chatbot%\log\chatbotSTDERR.txt" ++JvmOptions="-Dlogback.configurationFile=%chatbot%/config/logback.xml" ++JvmOptions="-Dconfigpath=%chatbot%/config/" ++JvmOptions="-Dlogpath=%chatbot%/"
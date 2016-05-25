set "chatbot=%cd%"
java -Dlogpath=%chatbot%/ -Dconfigpath=%chatbot%/config/ -Dlogback.configurationFile=%chatbot%/config/logback.xml  -cp ";lib/*" za.co.chris.Bootstrap
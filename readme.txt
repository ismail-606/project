C:\---\-----g-\-------\606 chat\
├── src\
│   ├── LoginFrame.java
│   ├── ChatClientGUI.java
│   ├── ChatClient.java
│   ├── ChatServer.java
├── bin\
│   ├── ChatServer.class
│   ├── ChatServer$ClientHandler.class
│   ├── ChatClient.class
│   ├── ChatClientGUI.class
│   ├── LoginFrame.class
│   ├── LoginFrame$1.class
│   ├── LoginFrame$2.class
│   ├── LoginFrame$3.class
│   ├── LoginFrame$4.class
├── manifest/
│   ├── server-manifest.mf
│   ├── client-manifest.mf
├── ChatServer.exe
│   ├── ChatClient.exe
│   ├── ChatServer.jar
│   ├── ChatClient.jar
│   ├── README.txt
---------
javac -d bin src/*.java
----------
dir src
---------
dir bin
-------------
java -cp bin ChatServer
---------------
netstat -an | findstr 5000
---------------
java -cp bin LoginFrame
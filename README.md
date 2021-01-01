## spl_project_3 - best server ever made  
This project was made to graduate from Ben Gurion University and actually works on a real programming project :)   
You can check the assignment details [here](https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_3)

### Read before running on windows  
1. Clone project
2. Download ubunto 18.04 LTS here: [link](https://www.microsoft.com/store/productId/9N9TNGVNDL3Q) 
3. Enable wsl on windows, run 'wsl' in cmd to check if enabled or [read here](https://www.windowscentral.com/install-windows-subsystem-linux-windows-10)
4. Open cmd run 'wsl', you will be required to choose password. (you can exit terminal with 'exit')  
5. Run 'sudo apt install build-essential' , 'sudo apt-get install libboost-all-dev'  
6. To run Server: open in intellij-> in Server folder right click pom.xml file -> make maven project->  
   #### Thread per client:  
   open Server/src/...imp/BGRSServer/TPCMain run main -> edit configuration -> arguments '7777'-> save configuration  
   #### Reactor:  
   open Server/src/...imp/BGRSServer/ReactorMain run main -> edit configuration -> arguments '7777 3'-> save configuration  
7. To run Client open terminat in project foldr-> 'cd Client' -> 'wsl' ->  'bash run.sh'  

### Read before running on linux
1. Server: 'cd Server' -> 'mvn clean' -> 'mvn compile' -> 'bash reactor.sh/tpc.sh' -> 'Ctrl+c to terminate'  
2. Client: 'cd Client' -> 'bash run.sh'  

![alt text](https://media.giphy.com/media/ZEobigiRBFc7S/giphy.gif)  
    
![alt text](https://media.giphy.com/media/8vIFoKU8s4m4CBqCao/giphy.gif)


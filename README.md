# PieLang
![pielang](https://i.imgur.com/o8pkCqz.png)

Introducing PieLang, a Java translator for pie language, which gives you an extended and more forgiving syntax for Python.
## Getting Started
These instructions will help you install and use the PieLang translator, on your machine.
### Prerequisites
Before we start, you will need to have an updated version of JRE installed. Which you can get here: [JRE 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
Also if you want to run the translated .py file, you got to have Python installed. Which you can get here:  [Python](https://www.python.org/downloads/)
### Installing
Download the JAR version of the program by clicking this [link](https://github.com/argaman123/pielang/releases). And you are ready to go!
## Usage
To translate your .pie file into a working Python file, you will need to type the following commands on the CMD/Terminal, and launch it where the JAR file is located.
```
java -jar pielang.jar path_to_file/filename.pie
python *path_to_file*/filename.py
```
## Debugging
In order to watch the process in which the pie program is translating into a Python file, just add noise=true to the program arguments as follows:
```
java -jar pielang.jar path_to_file/filename.pie noise=true
```
And if you want to help me improve the program, or just look at the source code and run the program from there, go to the *src* folder, and run:
```
javac Tokenizer.java
java Tokenizer
```
## Made by
Argaman Meir Gez
## Support
Any support will be much appreciated :) 

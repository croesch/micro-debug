Usage
=====

micro-debug is a debugger for the Mic-1 processor invented by *Andrew S. Tannenbaum*, [Structured Computer Organization, Fourth Edition](http://cw.prenhall.com/bookbind/pubbooks/tanenbaum2/) ([Prentice-Hall](http://www.prenhall.com/), 1998). You can debug the assembler code and the micro assembler code, too.

This debugger has been developed for a seminar paper. The seminar paper is stored in [croesch/micro-debug-doc](https://github.com/croesch/micro-debug-doc).

Requirements
------------

The debugger runs on Java, so you don't need to have a specific platform. You just need at least **Java 5** to run it.

Installation
------------

This section describes how to install the debugger from released .zip-files. For developer instructions see further sections.

1. Download ```micro-debug-version.zip``` from [downloads](https://github.com/croesch/micro-debug/downloads)
2. Extract .zip-file
3. The debugger doesn't need installation, it is now ready to be executed, for example on linux do the following steps:
  1. open command line
  2. change into directory ```$ cd micro-debug-version/```
  3. execute start script ```$ ./micro-debug.sh --help```

Basic usage
-----------

For any details you can run the debugger with the argument ```--help```. To simply debug an assembler file using a specific micro assembler implementation, run 
```
$ ./micro-debug.sh path-to-micro-assembler.mic1 path-to-assembler.ijvm
```
where the first argument is the relative or absolute path to the micro assembler **byte code** and the second argument is the relative or absolute path to the assembler **byte code**.

For specific syntax and available commands use the command ```HELP``` when the debugger is running and asking for your input (with ```micro-debug> ```).

Also the micro processor could need some input, to determine who is requesting input from you, the processor prints the line ```mic1> ```.

Configuration
-------------

In the directory ```config``` there are several files that can be used to configure behavior of the debugger.

You can use the file ```micro-debug.properties``` to change some default values like the size of the memory or the start values of the registers.

### Logger

The file ```logging.properties``` contains the configuration for the Java logger that is used when executing the debugger via start script. Please see documentation of ```java.util.logging``` for details.

### ijvm.conf

Since the directory ```config``` is placed in classpath upon the debuggers jar-file, you can override files by simply adding them into the ```config``` directory.
The debugger uses an ```ijvm.conf``` file to disassemble assembler code, this file is packaged in the jar-file.
So if you want to use your own ```ijvm.conf``` file then just put it into the ```config``` directory and restart the debugger.

Internationalisation
--------------------

The debugger uses xml-files to display any text to the user (except logging output). These files are located in the directory ```config/lang/```.

The debugger scans four files when you are running it:

1. ```text_lang_CT_var1.xml```, where ```lang``` is the language, ```CT``` the country code and ```var1``` the variant, all provided by the default locale of your JVM. This file is read first (if it exists). Any keys not found in this file will be searched in the following three files.
2. ```text_lang_CT.xml```
3. ```text_lang.xml```
4. ```text.xml```, the basic file for internationalisation. If any key is not found in a specific file or the files don't exist, than it will be searched in this file. So it is an important basis.

If you want to translate (or add translation to the debugger), just add the files named as shown above to the directory ```config/lang/``` and restart the debugger. It should automatically use the new files, if they are more specific (regarding to the list above) than the existing ones and contain language, country and variation of the used locale.

If you find a mistake in existing translation or translate the existing files to your language, please let us know and it would be a pleasure to add your contribution to the project!

Contribution
============

Although this project has been developed in a seminar paper it is now open for your contributions. There are several ways how you can contribute:

* you can translate the translation files to your language (as described above)
* you can test the debugger and open some bugs and/or feature requests in the issue tracking system
* you can fix a bug and contribute your changes
* you can just let us know that you're using the debugger and describe your experience using it

Issue tracking
--------------

You have found a bug? Or have any suggestions how to improve the debugger? Then please create an issue here on GitHub!

https://github.com/croesch/micro-debug/issues

Development
-----------

Once you forked the repository and checked out a local copy you can use **maven** to build the project and run the tests:

* ```$ mvn clean package``` to compile, test and package the project
* ```$ mvn test``` to compile and run the tests

You can now change behavior add tests and test your changes. After you successfully developed on a feature, you can push your changes to your public repository and make a **pull request**.

**Please develop in feature branches!** Please see [help on GitHub](http://help.github.com/fork-a-repo/) for how to do that.

Copyright and licensing
=======================

Copyright © 2011-2012 Christian Rösch; Copyright © 1999 Prentice-Hall, Inc.

Authors
-------

**Christian Rösch**

* https://github.com/croesch
* ```christianroesch:: at ::gmx.net```

**Ray Ontko**

* http://www.ontko.com/~rayo/
* ```rayo:: at ::ontko.com```

License
-------

License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>.
This is free software: you are free to change and redistribute it.  There is NO WARRANTY, to the extent permitted by law.

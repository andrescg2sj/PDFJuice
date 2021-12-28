
Presentation
====

This project provides some tools that help the user to extract structured information from PDF documents. Currently, the program is able to export them to HTML.

PDFJuice depends on [Apache PDFBox](https://pdfbox.apache.org/) to read PDF documents.

There are two functionalities available so far:

- Extract tables.
- Extract slides.

This project is a spin-off of [Courseminer](https://github.com/andrescg2sj/Courseminer).

Compile
===

Compile with dependencies:

```
mvn compile package assembly:single
```


Usage
===

Generate examples:
---

Output files are already available in the repository. They will be overwritten.

```
java -cp target/PDFJuice-1.3-SNAPSHOT-jar-with-dependencies.jar org.sj.tools.pdfjuice.ExampleGenerator
```


Use graphic user interface
-----

See [this tutorial](https://www.youtube.com/watch?v=prMFdpaBLr4).


Run from command line 
-----


```
java -cp target/PDFJuice-1.3-SNAPSHOT-jar-with-dependencies.jar org.sj.tools.pdfjuice.PDFJuice -m [mode] -i [input-filename] -o [output-filename]
```

The `mode` option may be `slide` or `table`, depending on which kind of information you want to extract (`text` and `poster` modes are under development).

More information (command line help):

```
Missing required options: i, o, m
usage: utility-name
 -c,--clip <arg>        format: x,y,width,height
 -g,--gui               Launches graphic user interface.
 -h,--help              Shows this help message.
 -i,--input <arg>       input file
 -l,--lines <arg>       line filtering: <color_name> | 0x<rrggbb> | all
 -m,--mode <arg>        extraction mode: slide|table|text
 -o,--output <arg>      output file
 -p,--proximity <arg>   minimum distance between tables
 -t,--thickness <arg>   máximum line thickness
```

Logging
------

Set `java.util.logging.config.file` property to `./logging.properties`.

```
java -cp target/PDFJuice-1.3-SNAPSHOT-jar-with-dependencies.jar -Djava.util.logging.config.file=./logging.properties org.sj.tools.pdfjuice.PDFJuice -m [mode] -i [input-filename] -o [output-filename]
```


Examples
===


You can see some [examples](https://github.com/andrescg2sj/PDFJuice/tree/master/examples) of what can be done with PDFJuice so far.





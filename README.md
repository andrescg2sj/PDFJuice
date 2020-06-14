
Presentation
====

This project provides some tools that help the user to extract structured information form PDF documents. Currently, the program is able to export them to HTML.

There are two funtionalities avalable so far:

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
java -cp target/PDFJuice-1.0-SNAPSHOT-jar-with-dependencies.jar org.sj.tools.pdfjuice.ExampleGenerator
```


Use on specific files
-----

```
java -cp target/PDFJuice-1.0-SNAPSHOT-jar-with-dependencies.jar org.sj.tools.pdfjuice.PDFJuice -m [mode] -i [input-filename] -o [output-filename]
```

The `mode` option may be `slide` or `table`, depending on which kind of information you want to extract.






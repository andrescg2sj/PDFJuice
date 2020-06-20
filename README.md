
Presentation
====

This project provides some tools that help the user to extract structured information from PDF documents. Currently, the program is able to export them to HTML.

PDFJuice depends on [Apache PDFBox](https://pdfbox.apache.org/) to read PDF documents.

There are two funtionalities available so far:

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

Examples
===


You can see some [examples](https://github.com/andrescg2sj/PDFJuice/tree/master/examples) of what can be done with PDFJuice so far.





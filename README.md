# JDupes Helper
=========================

Helper for iteratively processing JDupes output files

[JDupes](https://github.com/jbruchon/jdupes) is a great tool for detecting duplicate files. JDupes produces text output listing groups of duplicate files, each group separated by a new line.

I have a NAS with 10's of 1000's of photos/videos with way too many duplicates.
JDupes gave me output of text files listing groups of duplicate files, each group separated by a new line.

I needed to parse these groups and drill down on these groups and iteratively build
the lists of files I wanted to delete.

Sample command line: (mode: preserve|delete|deleteAll)

```bash
java -jar jdupes-helper-1.0.jar \
"[\
        'inFile':'volume1_photos',\
        'pattern':'/volume1/Photos/Photos.org/',\
        'mode':'preserve',\
        'patternMode': 'startsWith',\
        'deleteFile':'to_delete',\
        'appendDeleteFile':'true',\
        'verbose':'false'\
``]"
```


### Output

```bash
'preserve' files matched by '/volume1/Photos/Photos.org/' using 'startsWith' in file 'volume1_photos'
30505 groups processed, 7089 candidates for deletion, 23743 remaining groups (51945 remaining files)
/Users/me/jdupes-play/to_delete
/Users/me/jdupes-play/dupes.20190210.230741
```

##Build instructions

gradle 4.x required
'gradle'
to build

## TODO

Add more processors:
1. Regex
1. "del pattern1 iff dupe in pattern2"
import hitsandmisses.HitsAndMissesSerializer
import hitsandmisses.Options
import hitsandmisses.ProcessorFactory

class JDupesInspector {

  public static void main(String[] args) {
    if (!args) {
      println Options.usage()
      System.exit(1)
    }

    File replayFile = new File(args[0])
    if (replayFile.exists()) {
      println "Replaying: $replayFile"
      replayFile.text.eachLine { String line ->
        if (!line.isEmpty())
          (new JDupesInspector(Eval.me(line))).process()
      }
    } else {
      (new JDupesInspector(Eval.me(args[0]))).process()
    }

  }

  Options options
  File forDeletion
  File forPreservation

  int totalGroups = 0;
  int remainingGroups = 0;
  int totalHits = 0;
  int totalMisses = 0;
  String ts

  private final String HIST_FILE = '.jdi.hist'
  private final String REPLAY_FILE = '.jdi.replay'

  JDupesInspector(Map<String, String> argMap) {
    options = new Options(argMap)
    String msg = "'${options.mode}' files matched by '${options.pattern}' using '${options.patternMode}' in file '${options.inFile}'"
    println msg

    ts = (new Date()).format("yyyyMMdd.HHmmss", TimeZone.getTimeZone('UTC'))
    new File(HIST_FILE).append "\n$ts: $msg\n$ts: ${argMap.inspect()}"
    new File(REPLAY_FILE).append "\n${argMap.inspect()}"

    forDeletion = options.deleteFile ?: new File("${options.inFile}.${patt}.to-delete")

    if (forDeletion.exists() && !options.appendDeleteFile) {
      forDeletion.delete()
      forDeletion.createNewFile()
    }
    forDeletion.append("\n")

    forPreservation = new File("${options.inFile}.jdi")
    if (forPreservation.exists()) {
      forPreservation.delete()
      forPreservation.createNewFile()
    }
  }

  void printSummary() {
    String msg = "${totalGroups} groups processed, ${totalHits} candidates for deletion, ${remainingGroups} remaining groups (${totalMisses} remaining files)"
    println msg
    new File(HIST_FILE).append "\n$ts: $msg ${forPreservation.name}\n"
    println forDeletion.absoluteFile
    println forPreservation.absoluteFile

    // For convenience, copy the forPreservation filespec to the clipboard
    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(forPreservation.absoluteFile.toString()), null)
  }

  void processGroup(Set<String> group) {
    def (Integer countHits, Integer countMisses) = (new HitsAndMissesSerializer(
      ProcessorFactory."${options.mode}Processor"(group, options).processGroup(),
      forDeletion,
      forPreservation
    )).serialize();

    if (countMisses) remainingGroups++

    totalHits += countHits
    totalMisses += countMisses
    totalGroups++
  }

  void process() {
    Set<String> group = null;
    (new FileInputStream(options.inFile)).eachLine { String line ->

      if (!group) {
        // ignore initial blank lines
        if (line.isEmpty()) return

        // we have a line, create a new group
        group = new HashSet<String>(20);
      }

      if (!line.isEmpty()) {
        group << line
        return
      }

      // line is blank, we have a group => group complete
      processGroup(group)
      group = null;
    }

    // no blank line at end of file, last group was not processed
    if (group) processGroup(group)

    printSummary()
  }

}






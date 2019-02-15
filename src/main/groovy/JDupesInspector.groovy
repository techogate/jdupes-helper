import hitsandmisses.HitsAndMissesSerializer
import hitsandmisses.Options
import hitsandmisses.ProcessorFactory

import java.util.regex.Matcher

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
        if (!line.isEmpty()) {
          if (line.startsWith('"') && line.endsWith('"')) {
            line = line.substring(1, line.length() - 1)
          }
          (new JDupesInspector(Eval.me(line))).process()
        }
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
    String msg = "'${options.mode}' files matched by '${options.pattern}' using '${options.patternMode}' in file '${options.inFile}' ${options.dryRun ? '(dryRun)' : ''}"
    println msg

    ts = (new Date()).format("yyyyMMdd.HHmmss", TimeZone.getTimeZone('UTC'))
    new File(HIST_FILE).append "\n$ts: $msg\n$ts: \"${argMap.inspect()}\""
    new File(REPLAY_FILE).append "\n\"${argMap.inspect()}\""

    forDeletion = options.deleteFile ?: new File("${options.inFile}.${patt}.to-delete")

    if (forDeletion.exists() && !options.appendDeleteFile) {
      forDeletion.delete()
      forDeletion.createNewFile()
    }
    forDeletion.append("echo =========================\n")

    forPreservation = this.nextOutputFile
    if (forPreservation.exists()) {
      forPreservation.delete()
      forPreservation.createNewFile()
    }
  }

  private File getNextOutputFile(boolean overwritable = true) {

    Matcher matcher = (options.inFile.name =~ /^(\w+)\.([0-9]{4})\.jdi$/)
    File dir = options.inFile.parentFile ?: (new File('.')).absoluteFile.parentFile
    File retVal = new File(dir, "${options.inFile.name}.0000.jdi")

    if (overwritable) {
      if (matcher.count == 1 && options.inFile.name.startsWith(matcher[0][1])) {
        return new File(dir, matcher[0][1] + '.' + String.format("%04d", (matcher[0][2]).toInteger() + 1) + '.jdi')
      }
      return retVal
    }

    int maxExist = -1
    dir.eachFile { File file ->
      matcher = (file.name =~ /^(\w+)\.([0-9]{4})\.jdi$/)
      if (
        matcher.count == 1 &&
        file.name.startsWith(matcher[0][1]) &&
        ((matcher[0][2] as Integer) > maxExist)
      ) {
        maxExist = (matcher[0][2]).toInteger()
        retVal = new File(dir, matcher[0][1] + String.format(".%04d", maxExist + 1) + '.jdi')
      }
    }
    return retVal
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
      forPreservation,
      options.dryRun
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






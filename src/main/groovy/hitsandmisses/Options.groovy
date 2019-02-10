package hitsandmisses

class Options {
  File inFile
  String pattern
  Mode mode
  PatternMode patternMode
  boolean verbose
  File deleteFile
  File dupesFile
  boolean appendDeleteFile

  Options(Map<String, String> argMap) {
    try {
      this.inFile = new File(argMap['inFile'])
      this.deleteFile = argMap['deleteFile'] ? new File(argMap['deleteFile']) : null
      this.appendDeleteFile = (argMap['appendDeleteFile'] ?: 'false').toBoolean()
      this.dupesFile = new File(argMap['dupesFile'] ?: 'dupes')
      this.pattern = argMap['pattern']
      this.patternMode = argMap['patternMode'] ?: PatternMode.startsWith
      this.mode = argMap['mode'] as Mode
      this.verbose =  (argMap['verbose'] ?: 'false').toBoolean()

      if (!inFile.exists()) {
        throw new Exception('inFile file does not exit')
      }

      if (!pattern) {
        throw new Exception('no pattern')
      }
    } catch (exception) {
      println exception.message
      usage()
      System.exit(1);
    }
  }

  public static void usage() {
    println("Usage: Sample param: \"['inFile':'test-preserve','pattern':'/aaa/', patternMode:'startsWith', 'mode':'preserve', verbose:'true']\"")
  }
}

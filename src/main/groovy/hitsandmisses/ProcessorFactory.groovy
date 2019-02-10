package hitsandmisses

class ProcessorFactory {
  private ProcessorFactory() {}

  static HitsAndMisses deleteProcessor(Set<String> group, Options options) {
    new DeleteProcessor(group, options);
  }

  static HitsAndMisses preserveProcessor(Set<String> group, Options options) {
    new PreserveProcessor(group, options);
  }

  static HitsAndMisses deleteAllProcessor(Set<String> group, Options options) {
    new DeleteAllProcessor(group, options);
  }


}

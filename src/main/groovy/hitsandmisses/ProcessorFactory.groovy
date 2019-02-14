package hitsandmisses

import hitsandmisses.processors.DeleteAllProcessor
import hitsandmisses.processors.DeleteProcessor
import hitsandmisses.processors.PreserveProcessor

class ProcessorFactory {
  private ProcessorFactory() {}

  static HitsAndMisses deleteProcessor(Set<String> group, Options options) {
    new DeleteProcessor(group, options, MatcherFactory.createMatcher(options.patternMode, options.pattern));
  }

  static HitsAndMisses preserveProcessor(Set<String> group, Options options) {
    new PreserveProcessor(group, options, MatcherFactory.createMatcher(options.patternMode, options.pattern));
  }

  static HitsAndMisses deleteAllProcessor(Set<String> group, Options options) {
    new DeleteAllProcessor(group, options, MatcherFactory.createMatcher(options.patternMode, options.pattern));
  }
}

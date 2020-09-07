package matchesandmisses

import matchesandmisses.processors.*

class ProcessorFactory {

    def methodMissing(String name, Object args) {

        Set<String> group = args[0] as Set<String>
        Options options = args[1] as Options

        this.class.classLoader.loadClass("matchesandmisses.processors.${name}")?.newInstance(
                group,
                options,
                MatcherFactory.createMatcher(options.patternMode, options.pattern),
                MatcherFactory.createMatcher(options.pairPatternMode, options.pairPattern)
        )
    }
}


package hitsandmisses

interface HitsAndMisses {
  Set<String> getHits()
  Set<String> getMisses()
  HitsAndMisses processGroup()
}


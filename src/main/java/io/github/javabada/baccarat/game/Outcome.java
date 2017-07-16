package io.github.javabada.baccarat.game;

import java.math.BigDecimal;

public enum Outcome {

  PLAYER ("1"),
  BANKER (".95"),
  TIE    ("8");

  private final BigDecimal odds;

  Outcome(String odds) {
    this.odds = new BigDecimal(odds);
  }

  public BigDecimal getOdds() {
    return odds;
  }

}
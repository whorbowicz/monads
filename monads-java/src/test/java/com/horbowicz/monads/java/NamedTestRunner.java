package com.horbowicz.monads.java;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.*;

import java.util.Optional;

public class NamedTestRunner extends BlockJUnit4ClassRunner {

  public NamedTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  private boolean notEmpty(String s) {
    return !s.isEmpty();
  }

  @Override
  protected String testName(FrameworkMethod method) {
    return Optional.ofNullable(method.getAnnotation(Name.class))
                   .map(Name::value)
                   .filter(this::notEmpty)
                   .orElseGet(() -> super.testName(method));
  }
}

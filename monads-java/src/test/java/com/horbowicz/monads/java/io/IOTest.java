package com.horbowicz.monads.java.io;

import com.horbowicz.monads.java.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.*;

import java.util.function.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(NamedTestRunner.class)
public class IOTest {
  private static final String INPUT = "TestInput";
  private static final Integer INTEGER_RESULT = 42;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private Supplier<String> inputOperation;
  @Mock
  private Supplier<Integer> secondInputOperation;
  @Mock
  private Function<String, Integer> function;
  @Mock
  private Function<String, IO<Integer>> toIoFunction;

  @Name("When IO is created passed operation should not be executed")
  @Test
  public void shouldNotExecutePassedOperation() {
    IO.of(inputOperation::get);
    verifyZeroInteractions(inputOperation);
  }

  @Name("When run is called IO should perform it's operation")
  @Test
  public void shouldPerformOperationWhenRunIsCalled() {
    IO<String> io = IO.of(inputOperation::get);

    io.run();

    verify(inputOperation).get();
  }

  @Name("When run is called IO should return operation's result")
  @Test
  public void shouldReturnOperationsResultWhenRunIsCalled() {
    IO<String> io = IO.of(inputOperation::get);
    when(inputOperation.get()).thenReturn(INPUT);

    String actual = io.run();

    assertEquals(INPUT, actual);
  }

  @Name("When map is called IO should not execute passed function")
  @Test
  public void shouldNotExecutePassedFunctionWhenMapIsCalled() {
    IO.of(inputOperation::get).map(function);

    verifyZeroInteractions(inputOperation, function);
  }

  @Name("When run is called after map IO should " +
            "execute operation followed by passed function")
  @Test
  public void shouldExecuteOperationFollowedByPassedFunctionWhenRunIsCalledAfterMap() {
    IO<Integer> io = IO.of(inputOperation::get).map(function);

    io.run();

    InOrder inOrder = inOrder(inputOperation, function);
    inOrder.verify(inputOperation).get();
    inOrder.verify(function).apply(anyString());
    inOrder.verifyNoMoreInteractions();
  }

  @Name("When run is called after map IO should " +
            "pass operation's result as input to map's function")
  @Test
  public void shouldPassOperationsResultAsInputToMapsFunctionWhenRunIsCalledAfterMap() {
    IO<Integer> io = IO.of(inputOperation::get).map(function);
    when(inputOperation.get()).thenReturn(INPUT);

    io.run();

    verify(function).apply(INPUT);
  }

  @Name("When run is called after map IO should " +
            "return map's argument function result")
  @Test
  public void shouldReturnResultOfMapsArgumentWhenRunIsCalledAfterMap() {
    IO<Integer> io = IO.of(inputOperation::get).map(function);
    when(inputOperation.get()).thenReturn(INPUT);
    when(function.apply(anyString())).thenReturn(INTEGER_RESULT);

    Integer actual = io.run();

    assertEquals(INTEGER_RESULT, actual);
  }

  @Name("When flatMap is called IO should not execute passed function")
  @Test
  public void shouldNotExecutePassedFunctionWhenFlatMapIsCalled() {
    IO.of(inputOperation::get).flatMap(toIoFunction);

    verifyZeroInteractions(inputOperation, toIoFunction);
  }

  @Name("When run is called after flatMap IO should " +
            "execute in order" +
            "initial operation" +
            "flatMap's argument function and " +
            "operation of flatMap's argument function result")
  @Test
  public void shouldExecuteOperationFollowedByPassedFunctionAndItsResultOperationWhenRunIsCalledAfterFlatMap() {
    IO<Integer> io = IO.of(inputOperation::get).flatMap(toIoFunction);
    when(toIoFunction.apply(anyString())).thenReturn(IO.of(secondInputOperation::get));

    io.run();

    InOrder inOrder = inOrder(
        inputOperation,
        toIoFunction,
        secondInputOperation);
    inOrder.verify(inputOperation).get();
    inOrder.verify(toIoFunction).apply(anyString());
    inOrder.verify(secondInputOperation).get();
    inOrder.verifyNoMoreInteractions();
  }

  @Name("When run is called after flatMap IO should " +
            "pass result of first operation as input to flatMap's function")
  @Test
  public void shouldPassOperationsResultAsInputToFlatMapsFunctionWhenRunIsCalledAfterFlatMap() {
    IO<Integer> io = IO.of(inputOperation::get).flatMap(toIoFunction);
    when(inputOperation.get()).thenReturn(INPUT);
    when(toIoFunction.apply(anyString())).thenReturn(IO.of(secondInputOperation::get));

    io.run();

    verify(toIoFunction).apply(INPUT);
  }

  @Name("When run is called after flatMap IO should " +
            "return result of operation " +
            "that was returned from flatMap's argument function")
  @Test
  public void shouldReturnResultOfOperationReturnedFromFlatMapsArgumentFunctionWhenRunIsCalledAfterFlatMap() {
    IO<Integer> io = IO.of(inputOperation::get).flatMap(toIoFunction);
    when(inputOperation.get()).thenReturn(INPUT);
    when(toIoFunction.apply(anyString())).thenReturn(IO.of(secondInputOperation::get));
    when(secondInputOperation.get()).thenReturn(INTEGER_RESULT);

    Integer actual = io.run();

    assertEquals(INTEGER_RESULT, actual);
  }
}
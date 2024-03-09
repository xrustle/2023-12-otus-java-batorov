package ru.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.DateTimeProvider;
import ru.otus.processor.homework.EvenSecondExceptionProcessor;
import ru.otus.processor.homework.ProcessorSwapFields;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ComplexProcessorTest {

  @Test
  @DisplayName("Тестируем вызовы процессоров")
  void handleProcessorsTest() {
    // given
    var field11Initial = "field11";
    var field12Initial = "field12";
    var message =
        new Message.Builder(1L).field11(field11Initial).field12(field12Initial).build();
    var oddSecondsDate = LocalDateTime.of(2000, 1, 1, 1, 1, 1);

    var oddSeconds = mock(DateTimeProvider.class);
    when(oddSeconds.getDate()).thenReturn(oddSecondsDate);

    List<Processor> processors =
        List.of(new ProcessorSwapFields(), new EvenSecondExceptionProcessor(oddSeconds));

    var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

    // when
    var result = complexProcessor.handle(message);

    // then
    assertThat(result.getField11()).isEqualTo(field12Initial);
    assertThat(result.getField12()).isEqualTo(field11Initial);
  }

  @Test
  @DisplayName("Тестируем обработку исключения")
  void handleExceptionTest() {
    // given
    var message = new Message.Builder(1L).field8("field8").build();
    var evenSecondsDate = LocalDateTime.of(2000, 1, 1, 1, 1, 0);

    var evenSeconds = mock(DateTimeProvider.class);
    when(evenSeconds.getDate()).thenReturn(evenSecondsDate);

    var processor1 = new EvenSecondExceptionProcessor(evenSeconds);

    List<Processor> processors = List.of(processor1);

    var complexProcessor = new ComplexProcessor(processors, (ex) -> {
      throw new TestException(ex.getMessage());
    });

    // when
    assertThatExceptionOfType(TestException.class)
        .isThrownBy(() -> complexProcessor.handle(message));
  }

  private static class TestException extends RuntimeException {
    public TestException(String message) {
      super(message);
    }
  }
}

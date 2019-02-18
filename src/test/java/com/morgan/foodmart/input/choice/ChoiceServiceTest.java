package com.morgan.foodmart.input.choice;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ChoiceServiceTest {

  private static ChoiceService choiceService;
  private static final List<String> choices =
      Arrays.asList("first choice", "another choice", "and another");
  private static final String helpfulMessage = "a useful help message";

  @Test
  public void testValidInputReturnsCorrectChoice() {
    final String choice = "1";
    System.setIn(new ByteArrayInputStream(choice.getBytes()));

    choiceService = new ChoiceServiceImpl();

    final String userChoice = choiceService.getChoice(choices, helpfulMessage);

    assertEquals(choices.get(0), userChoice);
  }

  @Test
  public void testInvalidInputIgnoredUntilValidChoiceMade() {
    final String choice = "0\n4\nsome horrible string\n2";
    System.setIn(new ByteArrayInputStream(choice.getBytes()));

    choiceService = new ChoiceServiceImpl();

    final String userChoice = choiceService.getChoice(choices, helpfulMessage);

    assertEquals(choices.get(1), userChoice);
  }
}

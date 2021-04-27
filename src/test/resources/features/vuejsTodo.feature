@ToDoMVCUserScenario
Feature: Explore How A User Can Manage Todo List

  Scenario Outline: Navigate to Vue.js todos and manage list
    Given Browser is Open
    When Website content is loaded
    Then Verify Todo list textbox is enabled
    And Enter "<Todo-List-One>" text and press enter
    Then Verify new todo text "<Todo-List-One>" note created and is in active state
    And Enter "<Todo-List-Two>" text and press enter
    And Enter "<Todo-List-Three>" text and press enter
    Then Verify that three todo text notes are created and all are in active state
    Then Verify that on selecting any todo text note it moves to completed label
    And Verify that with click on Click Completed label the completed todo note is removed from completed note list
    Then Verify that under All label only <2> todo notes exists
    Then Verify that under All label only <2> todo notes exists
    And Verify todo-count text displays number of items left
    And Verify todo note can be destroyed and under All label only <1> todo notes exists
    Then Verify that todo note can be edited through double click
    And Close the browser

    Examples:
      | Todo-List-One   | Todo-List-Two   | Todo-List-Three |
      | Text-Reminder-1 | Text-Reminder-2 | Text-Reminder-3 |
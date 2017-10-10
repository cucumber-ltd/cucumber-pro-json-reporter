Feature: Eating cucumbers
  Scenario: Many cucumbers
    Given I have already eaten 51 cucumbers
    When I eat 48 cucumbers
    Then I should have 99 cucumbers in my belly
Feature: Create an order

  Background:

    Given [DB] table customer has rows
      | id | email                |
      | 1  | petromir@example.com |

    Given [DB] table product has rows
      | id | name           | description            | price |
      | 1  | Football ball  | Original Football ball | 35.60 |
      | 2  | Cotton T-shirt | White Slim Fit T-shirt | 13.61 |

  Scenario: Book created successfully
    Given [HTTP] client adds JSON body
    """json
    {
      "customerId": 1,
      "totalPrice": 42.31,
      "currency": "BGN",
      "productIds": []
    }
    """

    When [HTTP] client sends the request

    Then [HTTP] client response status code must be 201
    Then [HTTP] client response body must be empty

    Then [DB] table order must have rows
      | id | current_value | hidden_value | state   |
      | 1  | 17000.50      | 0.00         | CREATED |
# code-challenge-5
https://coding-challenges.jl-engineering.net/challenges/challenge-5/

#### Challenge description

<details><summary>click here</summary>
<p>

Everybody loves connect 4, which is apparently also known as Captain’s Mistress, Four Up, Plot Four, 
Find Four, Four in a Row, Four in a Line, Drop Four, and Gravitrips. This is a two player game were 
each player takes turns adding one of their coloured tokens to a grid. The winner is the player who 
gets four of their tokens in a line which can be horizontal, vertical or diagonal. Unlike noughts and 
crosses, gravity is at play so any token added to a column falls to the bottom of the column. Only one 
token can occupy a position in the grid so tokens added to the same column stack up. The traditional 
grid is 7 columns wide by 6 rows deep and tokens are red and yellow. Red always plays first.

The main objective of this challenge is determine the status of the grid. You need to create a function 
called getGridStatus which takes an array of strings as input and return one of the following strings as output:

```
"Red plays next"
"Yellow plays next"
"Red wins"
"Yellow wins"
"Draw" // this means there are no empty spaces left on the grid.`
``` 

The array of strings used as the input is structured so that each string represents a row on the grid, 
with elements at position zero representing the top of the grid and the element with the highest position 
representing the bottom of the grid. Each character in the string represents a position in each 
column of the grid as follows:

```
"." - Empty position
"r" - Position contains a red token
"y" - Position contains a yellow token
"R" - Position contains a red token that was the last token added to the grid
"Y" - Position contains a yellow token that was the last token added to the grid
```

If a grid contains tokens then one of the tokens should be either “R” or “Y” but the grid 
can’t contain both “R” and “Y”.

Here are some sample scenarios:

```kotlin
getGridStatus( [".......",
                ".......",
                ".R.....",
                ".r.....",
                ".ry....",
                ".ryyy.."]) // "Red wins"


getGridStatus( [".......",
                ".......",
                ".......",
                ".yy....",
                ".rrRr..",
                ".ryyy.."]) // "Red wins"

getGridStatus( [".......",
                ".......",
                "....r..",
                "...ry..",
                "..Ryr..",
                ".ryyyr."] ) //"Red wins"

getGridStatus( [".......",
                ".......",
                "...y...",
                "...ry..",
                "...ryy.",
                "...rrrY"]) //"Yellow wins"

getGridStatus( [".......",
                ".......",
                ".......",
                "...ry..",
                "...ryy.",
                "..yrrrY"]) //"Red plays next"

getGridStatus( [".......",
                ".......",
                ".......",
                "...ry..",
                "..Rryy.",
                "..yrrry"]) //"Yellow plays next"
```

You don’t need to add any validation to the input data. 

</p>
</details>

#### Run build:

> ./gradlew clean build

#### Run tests:

> ./gradlew :cleanTest :test

#### Default test report folder:

> build/reports/tests/test/index.html
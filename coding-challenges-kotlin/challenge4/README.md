# code-challenge-4
https://coding-challenges.jl-engineering.net/challenges/challenge-4/

#### Challenge description

<details><summary>click here</summary>
<p>

As a rewards service provider,
I want to return the list of vouchers in a predefined sequence.

Write a function `sortVouchers` that takes in a String with format …

`"endDate:status:id,endDate:status:id,endDate:status:id..."`

… sort it and return a sorted string in the same format.

- `endDate is yymmdd`
- `status is in the definition below`
- `id is a 4 character string`

Example:

`String = "190112:Activated:aaaa,190205:Redeemed:bbbb,..."`


#####For ‘current’ rewards (that is where status = `Available` or `Activated`)

Sort by endDate ascending, then by status = Activated, then by status = Available, then by id ascending

#####For redeemed/expired rewards (that is where status = `Redeemed` or `Expired`)

Sort by endDate descending, then by status = Redeemed, then by status = Expired, then by id ascending

The ‘current’ rewards should be ordered before all redeemed/expired rewards.

For example, given the initial string: 

`190112:Available:aaaa,190112:Activated:bbbb,190111:Available:cccc,190110:Redeemed:dddd,190110:Expired:eeee,190111:Activated:ffff` 

Return:

`190111:Activated:ffff,190111:Available:cccc,190112:Activated:bbbb,190112:Available:aaaa,190110:Redeemed:dddd,190110:Expired:eeee`

</p>
</details>

#### Run build:

> ./gradlew clean build

#### Run tests:

> ./gradlew :cleanTest :test

#### Default test report folder:

> build/reports/tests/test/index.html
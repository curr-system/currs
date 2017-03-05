# API

## GET
### /currencies
Returns available currencies.

At the moment the following currencies are available:

  - EUR
  - USD
  - CHF

#### /currencies/`<currency name>`
Returns given currency data.
By default data from the last week are returned with frequency 60/sec (1/min).

Examples:

    /currencies/EUR - EUR data from last week with 1/min frequency
    /currencies/USD - USD data from last week with 1/min frequency

### Filtering
By adding add additional parameters data from different time intervals
and with different frequencies can be obtain.

#### ?frequency (default 60)
Allows to specify data frequency.
Possible values are from 60 to 10 sec.

Examples:

    /currencies/EUR?frequency=10 - EUR data from last week with 10/sec (1/min) frequency
    /currencies/USD?frequency=30 - EUR data from last week with 30/sec (0.5/min) frequency

#### ?day ?month ?year (default current day)
Allows to specify date.
When all parameters are given the whole date is used as end date.
If any of the parameters is not given the current day/month/year is used instead.

By default the current day, month and year are used.

Examples if today is: 15.02.2017 then:

    /currencies/EUR?day=10            - data from 10.02.2017 till 03.02.2017 (one week)
    /currencies/EUR?month=1           - data from 15.01.2017 till 03.01.2017 (one week)
    /currencies/EUR?year=2016         - data from 15.02.2016 till 03.01.2016 (one week)
    /currencies/EUR?month=3&year=2016 - data from 15.03.2016 till 03.03.2016 (one week)

#### ?days (default 7)
Allows to specify from how many days the data has to be given.
By default it is set to 7 days (one week).
Maximum value is 90 (three months).

Examples if today is: 15.02.2017 then:

    /currencies/EUR?days=10      - data from 15.02.2017 till 05.02.2017 (10 days)
    /currencies/EUR?day=8&days=1 - data from 08.02.2017 till 07.02.2017 (one day)


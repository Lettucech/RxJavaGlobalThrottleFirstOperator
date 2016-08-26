# RxJavaGlobalThrottleFirstOperator
This project is to demonstrate how to make 
[ThrottleFirst](http://reactivex.io/documentation/operators/sample.html) 
globally.

The custom operator 
[OperatorGlobalThrottleFirst](https://github.com/brianbridge/RxJavaGlobalThrottleFirstOperator/blob/master/app/src/main/java/com/gmail/brianbridge/rxjavaglobalthrottlefirstoperator/OperatorGlobalThrottleFirst.java) 
is implemented based on the 
[ThrottleFirst](http://reactivex.io/documentation/operators/sample.html) 
class. The main changes is making the variable **lastOnNext** static.

## When To Use
* Prevent rapid interact on the UI component(s) when develop mobile app

## Get Started
#### Use Global ThrottleFirst Operator in Your Stream
```java
Observable.just(obj)
    .lift(new OperatorGlobalThrottleFirst<Integer>())
  	.subscribe(new Action1<Integer>() {
        @Override
    		public void call(Integer integer) {
    		    // do your stuff here
    		}
    });
```

#### Set Your Default Skip Duration
```java
OperatorGlobalThrottleFirst.setDefaultSkipDuration(time, timeUnit);
```

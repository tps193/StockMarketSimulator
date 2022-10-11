# Stock Market Simulator

## Description
This repository contains Stock Market Simulator and Command Line Client for it.

### Stock Market Simulator
A web application written with Java and Spring.
Allows to create orders to buy and sell stocks. The application sends notifications about matching orders via websocket.

### Stock Market Client
Command line interface allowing to do basic operations with Simulator using REST API and listening for matching orders
on a websocket.

### Requirements
Java 17

### How to build
./gradlew clean build 

##### To run Stock Market Simulator
./gradlew :StockMarketSimulator:bootRun

It will run your server on http://localhost:8080
Please modify spring boot configuration if you need to configure a custom port.
The logging is implemented via commons-logging, so any implementation might be added. However, for simplicity
the application now writes logs to console and generates and log file under ./simulator-log/spring.log

##### To run Stock Market Client
./gradlew :StockMarketClient:installDist
cd ./StockMarketClient/build/install/StockMarketClient/bin
./StockMarketClient http://localhost:8080

It will run the client and connect it to the websocket of the simulator for retrieving info about new trades.

### Stock Market Simulator API

| Endpoint             | Type |                                   Body                                    |                                                Description |
|----------------------|:----:|:-------------------------------------------------------------------------:|-----------------------------------------------------------:|
| /ping                | GET  |                                                                           |                                      Check server is alive |
| /order/add           | POST | { "symbol": string, "type": "BUY" or "SELL", "count": int, "price": int } |     Creates new BUY or SELL order for specified Order Book |      
| /order/cancel/{id}   | POST |                                                                           |                        Cancels the order with specified id |
| /symbol/add/{symbol} | POST |                                                                           | Creates new Order Book with specified symbol as identifier |

Websocket connection for listening for new trades is available at /trades-ws endpoint.

### Stock Market Client interface

Stock Market Client requires Simulator URL to be specified as a command line option like
./StockMarketClient http://localhost:8080
It supports following commands:

| Actions   |                                                Syntax                                                |                                                                    Description |
|-----------|:----------------------------------------------------------------------------------------------------:|-------------------------------------------------------------------------------:|
| addSymbol |                                          addSymbol <symbol>                                          | Makes a request to create a new Order Book with specified symbol as identifier |
| add       | add <symbol> <order_type> <count> <price><br/>Examples:<br/>add APPL BUY 5 10<br/>add APPL SELL 5 10 |       Makes a request to create new BUY or SELL order for specified Order Book |      
| cancel    |                                          cancel <order_id>                                           |                          Makes a request to cancel the order with specified id |
| quit      |                                                                                                      |                                                                Exit the client |

Client doesn't print anything into log file. Just command line output is implemented.

### Notes
1. No fancy error handling is implemented for Simulator. It means that it doesn't return some handled error output to
client, but just returns whatever is generated by Spring. So in case of server error client just complains and print
the error code to the console.
2. The Simulator is implemented with primitive H2 database. Currently, it is configured to work with in memory database
which means that after restarting the simulator all order books, orders, trades will be lost.
3. Some places require additional validation and error handling.

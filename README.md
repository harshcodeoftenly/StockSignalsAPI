StockSignalsAPI
StockSignalsAPI is a Spring Boot REST API for fetching live stock data, historical OHLCV prices, company fundamentals, and calculating popular trading indicators like SMA, EMA, RSI, and MACD. This API is designed to help developers, traders, and analysts access stock information programmatically and make decisions based on technical indicators.

Table of Contents
Features

Technologies

Getting Started

API Endpoints

Project Structure

Usage Examples

Example Responses

Future Enhancements

License

Features
Fetch live stock prices

Retrieve daily high/low and 52-week high/low

Access historical price data for custom ranges

Retrieve company fundamentals (Market Cap, Debt, PE Ratio, etc.)

Compute popular technical indicators:

SMA (Simple Moving Average)

EMA (Exponential Moving Average)

RSI (Relative Strength Index)

MACD (Moving Average Convergence Divergence)

Return both latest indicator values and full historical series

Technologies
Java 17

Spring Boot 3

Spring Web

Jackson (JSON parsing)

Maven (build tool)

Concurrent HashMap for caching API responses

Getting Started
Prerequisites
Java 17+

Maven

Internet connection (for Yahoo Finance API calls)

Steps
Clone the repository:

bash
git clone https://github.com/harshcodeoftenly/StockSignalsAPI.git
cd StockSignalsAPI
Build the project:

bash
mvn clean install
Run the API:

bash
mvn spring-boot:run
Test endpoints in Postman or browser at:

text
http://localhost:8080
API Endpoints
Price APIs
Endpoint	Description	Example
/api/price/{symbol}/live	Get latest price	/api/price/INFY.NS/live
/api/price/{symbol}/52week	52-week high/low	/api/price/INFY.NS/52week
/api/price/{symbol}/today	Today's high/low	/api/price/INFY.NS/today
/api/price/{symbol}/range?range={range}	Custom OHLCV data (range: 1mo, 3mo, 6mo, 1y)	/api/price/INFY.NS/range?range=6mo
Fundamentals APIs
Endpoint	Description	Example
/api/fundamentals/{symbol}	Market Cap, PE ratio, Debt, etc.	/api/fundamentals/INFY.NS
Indicator APIs
Supported indicators: sma20, sma50, ema20, ema50, rsi14, macd

Endpoint	Description	Example
/api/indicators/{symbol}/{indicator}	Latest indicator value	/api/indicators/INFY.NS/sma20
/api/indicators/{symbol}/{indicator}/series	Full historical series	/api/indicators/INFY.NS/sma20/series
Project Structure
text
src/main/java/com/stockapp/
├── controller/       # REST controllers (Price, Fundamental, Indicator)
├── service/          # Business logic & Yahoo Finance API calls
├── model/            # Stock data and OHLCV entities
├── indicators/       # Indicator calculators (SMA, EMA, RSI, MACD)
└── StockAppApplication.java  # Spring Boot entry point
Usage Examples
Get live price:

bash
curl http://localhost:8080/api/price/INFY.NS/live
Get 52-week range:

bash
curl http://localhost:8080/api/price/INFY.NS/52week
Get SMA(20) latest value:

bash
curl http://localhost:8080/api/indicators/INFY.NS/sma20
Get SMA(20) historical series:

bash
curl http://localhost:8080/api/indicators/INFY.NS/sma20/series

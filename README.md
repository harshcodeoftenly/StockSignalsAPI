# 🚀 StockSignalsAPI

**StockSignalsAPI** is a Spring Boot REST API for fetching **live stock data**, historical OHLCV prices, company fundamentals, and calculating popular trading indicators like SMA, EMA, RSI, and MACD. This API helps developers, traders, and analysts access stock info programmatically and make decisions based on technical indicators.

---

## 📋 Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Usage Examples](#usage-examples)
- [Example Responses](#example-responses)
- [Future Enhancements](#future-enhancements)
- [License](#license)

---

## ✨ Features

- Fetch **live stock prices**
- Retrieve **daily high/low** and **52-week high/low**
- Access **historical price data** for custom ranges
- Retrieve **company fundamentals** (Market Cap, Debt, PE Ratio, etc.)
- Compute popular technical indicators:
  - `SMA` (Simple Moving Average)
  - `EMA` (Exponential Moving Average)
  - `RSI` (Relative Strength Index)
  - `MACD` (Moving Average Convergence Divergence)
- Return both **latest indicator values** and **full historical series**

---

## 🛠 Technologies

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Jackson** (JSON parsing)
- **Maven** (build tool)
- **Concurrent HashMap** for caching API responses

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- Internet connection (for Yahoo Finance API calls)

### Steps

1. Clone the repository:

```bash
git clone https://github.com/harshvardhansingh7/StockDetailsAPI.git
cd StockDetailsAPI
```

2. Build the project:

```bash
mvn clean install
```

```bash
3. Run the API

mvn spring-boot:run
```


### API Documentation
Base URL (Local)
text
http://localhost:8080
Price APIs
Endpoint (path)	Description	Example URL
/api/price/{symbol}/live	Get latest price	http://localhost:8080/api/price/INFY.NS/live
/api/price/{symbol}/52week	52-week high/low	http://localhost:8080/api/price/INFY.NS/52week
/api/price/{symbol}/today	Today’s high/low	http://localhost:8080/api/price/INFY.NS/today
/api/price/{symbol}/range?range={range}	Custom OHLCV data
(range: 1mo, 3mo, 6mo, 1y)	http://localhost:8080/api/price/INFY.NS/range?range=6mo
Example external source used: Yahoo Finance chart API
Example:
https://query1.finance.yahoo.com/v8/finance/chart/INFY.NS?range=1y&interval=1d

Fundamentals APIs
Endpoint (path)	Description	Example URL
/api/fundamentals/{symbol}	Market Cap, PE ratio, Debt, EPS, etc.	http://localhost:8080/api/fundamentals/INFY.NS
Indicator APIs
Supported indicators: sma20, sma50, ema20, ema50, rsi14, macd

Endpoint (path)	Description	Example URL
/api/indicators/{symbol}/{indicator}	Latest indicator value	http://localhost:8080/api/indicators/INFY.NS/sma20
/api/indicators/{symbol}/{indicator}/series	Full historical series	http://localhost:8080/api/indicators/INFY.NS/sma20/series



4. Test endpoints

Open in Postman or browser:

http://localhost:8080

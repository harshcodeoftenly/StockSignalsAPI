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
git clone https://github.com/harshcodeoftenly/StockSignalsAPI.git
cd StockSignalsAPI

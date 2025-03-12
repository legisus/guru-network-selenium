# Guru Network - Selenium Test Automation

This repository contains automated UI tests for the Guru Network application using Selenium WebDriver, Cucumber BDD, and Java.

## Overview

The test automation framework follows the Page Object Model (POM) design pattern and uses BDD approach with Cucumber to create human-readable test specifications.

### Core Features

- **BDD Testing**: Uses Cucumber for behavior-driven development testing
- **Page Object Model**: Maintains separation between test logic and page-specific code
- **Visual Comparison**: Includes utility for visual comparison between app versions
- **Detailed Logging**: Comprehensive logging using SLF4J and Logback
- **Cucumber Reporting**: Generate detailed HTML reports after test execution
- **Configuration Management**: Centralized configuration handling

## Test Scenarios

The framework includes smoke tests that verify:

1. **User Authentication**
    - Login functionality with Telegram
    - Profile verification

2. **Tokens Page**
    - Navigation to tokens page
    - Visual comparison with dex.guru tokens page

3. **Analytics Page**
    - Navigation to analytics
    - Guru AI assistant interaction

4. **Guru AI Functionality**
    - Testing AI assistant responses
    - Verifying prompt button functionality

## Project Structure

```
src/
├── main/java/com/guru/selenium/
│   ├── config/           # Configuration classes
│   │   └── Configuration.java
│   ├── pages/            # Page object classes
│   │   ├── AnalyticsPage.java
│   │   ├── BasePage.java
│   │   ├── GuruAIPage.java
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   └── TokensPage.java
│   ├── utils/            # Utility classes
│   │   ├── DriverFactory.java
│   │   └── VisualComparisonUtil.java
│   └── resources/
│       ├── config.properties  # Application configuration
│       └── logback.xml        # Logging configuration
└── test/
    ├── java/com/guru/selenium/
    │   ├── runners/     # Cucumber test runners
    │   │   └── TestRunner.java
    │   └── steps/       # Step definitions
    │       ├── AnalyticsSteps.java
    │       ├── GuruAISteps.java
    │       ├── HomeSteps.java
    │       ├── Hooks.java
    │       ├── LoginSteps.java
    │       └── TokensSteps.java
    └── resources/
        └── features/    # Cucumber feature files
            └── smoke.feature
```

## Configuration

The framework uses a centralized configuration approach:

- **config.properties**: Contains environment-specific settings, URLs, timeouts, and test data
- **logback.xml**: Configures logging levels, patterns, and output destinations
- **Configuration.java**: Provides programmatic access to configuration properties

## Setup Instructions

### Prerequisites

- Java JDK 21
- Maven
- Chrome browser

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/legisus/guru-network-selenium.git
   cd guru-network-selenium
   ```

2. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

## Running Tests

### Run all tests

```bash
mvn clean test
```

### Run specific tags

```bash
mvn clean test -Dcucumber.filter.tags="@YourTag"
```

## Test Reports

After test execution, reports are generated in:
- HTML Report: `target/cucumber-reports/cucumber-pretty.html`
- JSON Report: `target/cucumber-reports/CucumberTestReport.json`
- XML Report: `target/cucumber-reports/CucumberTestReport.xml`

## Driver Management

The framework uses WebDriverManager to handle driver binaries automatically. Configuration for browsers and timeouts can be adjusted in the `DriverFactory` class and `config.properties` file.

## Logging

Logging is configured via logback.xml and provides:
- Console output for immediate feedback
- File output for detailed test logs
- Different log levels (INFO, DEBUG, ERROR)

## Continuous Integration

This test suite can be integrated into CI/CD pipelines like Jenkins, GitHub Actions, or GitLab CI. Example configuration files for CI integration can be provided upon request.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
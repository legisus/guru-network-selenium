# Guru Network - Selenium Test Framework

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.18.1-green.svg)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.15.0-brightgreen.svg)](https://cucumber.io/)

A comprehensive test automation framework for Guru Network UI testing, built with Selenium WebDriver, Cucumber BDD, and Java.

## Overview

This framework follows the Page Object Model (POM) design pattern and Behavior-Driven Development approach with Cucumber to create human-readable test specifications.

### Key Features

- **BDD Testing**: Human-readable tests with Cucumber
- **Page Object Model**: Clean separation between test logic and page implementation
- **Visual Comparison**: Utilities for comparing UI elements across pages
- **Comprehensive Logging**: Detailed logging with SLF4J and Logback
- **Reporting**: Detailed HTML, JSON, and XML reports
- **Centralized Configuration**: Property-based configuration system

## Test Coverage

### Smoke Tests

- **Navigation**: Verifies all main pages load correctly
- **User Authentication**: Telegram login and profile verification
- **Tokens Page**: Ensures component parity with dex.guru
- **Analytics**: Verifies page loads and analytics features
- **Guru AI**: Tests AI assistant functionality and responses

## Technology Stack

- **Java 21**: Core programming language
- **Selenium WebDriver 4.18.1**: Browser automation
- **Cucumber 7.15.0**: BDD test framework
- **JUnit 4.13.2**: Test runner and assertions
- **WebDriverManager 5.6.3**: Automated driver management
- **Lombok**: Reduces boilerplate code
- **SLF4J & Logback**: Comprehensive logging

## Project Structure

```
src/
├── main/java/com/guru/selenium/
│   ├── config/           # Configuration utilities
│   ├── pages/            # Page object classes
│   │   ├── BasePage.java # Common page functionality
│   │   └── ...           # Individual page implementations
│   ├── utils/            # Utility classes
│   └── resources/        # Configuration files
│       ├── config.properties
│       └── logback.xml
└── test/
    ├── java/com/guru/selenium/
    │   ├── runners/      # Cucumber test runners
    │   └── steps/        # Step definitions
    └── resources/
        └── features/     # Cucumber feature files
```

## Setup & Installation

### Prerequisites

- Java JDK 21
- Maven
- Chrome browser (for running tests)

### Steps

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

### Run All Tests

```bash
mvn clean test
```

### Run by Tags

```bash
mvn clean test -Dcucumber.filter.tags="@YourTag"
```

### Test Reports

Reports are generated in:
- HTML Report: `target/cucumber-reports/cucumber-pretty.html`
- JSON Report: `target/cucumber-reports/CucumberTestReport.json`
- XML Report: `target/cucumber-reports/CucumberTestReport.xml`

## Driver Configuration

The framework uses WebDriverManager to handle driver binaries automatically. Browser settings can be adjusted in:

- `DriverFactory.java` for code-level settings
- `config.properties` for environment-specific settings

## Logging

- **Console Output**: Immediate feedback during test execution
- **File Logging**: Detailed logs in `target/test-logs/`
- **HTML Reports**: Visual logs with `test-report.html`

## CI/CD Integration

This framework can be integrated into CI/CD pipelines like Jenkins, GitHub Actions, or GitLab CI. Configuration files available upon request.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
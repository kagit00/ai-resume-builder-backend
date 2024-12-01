# Project Title & Live Link
**AI Resume Builder** | Domain Link: https://ai-resume-builder-443308.uc.r.appspot.com



## Table of Contents
- [Description](#description)
- [Installation](#install--run)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Features](#features)
- [Contributing](#contributing)
- [Testing](#testing)



## Description
The Al Resume Builder is a cutting-edge tool that uses advanced artificial intelligence algorithms to help job seekers create powerful
and effective resumes. It analyzes user input and provides personalized recommendations for improving resume content, formatting,
and keyword optimization, increasing the likelihood of landing job interviews.



## Install & Run
1. **Pre-requisites**: List the tools required (e.g., Java17 , Spring Boot, Spring Security, Log4j, XML, Hibernate, GROQ AI, OCR Space,
   Caffeine Caching, Postgres, Braintree, Git, GitHub CI/CD, GCP).
2. **Clone the repository**: 
   ```bash
   git clone https://github.com/kagit00/ai-resume-builder-backend.git
   cd ai-resume-builder-backend
   mvn spring-boot:run




### Configuration
- Currently, this project has been deployed using Google App Engine (GAE). The potential passkeys come from GCP Secret Manager. 
- These Passkeys mostly consist of Groq API Key, OCR API Key, oAuth2 Regn Client Id, oAuth2 Regn. Client Secret, Postgres Database creds etc.
- Few of the passkeys (e.g. Oauth Redirect Uri, GCP secret key, Project Id, etc.) comes from GitHub Secret Manager.
- Below is the application.properties sample for production environment. where the values for (secrets, passkeys or password or key) get resolved from app.yml using CI/CD workflow.
- And values in app.yaml get resolved from google secret manager.

   ```properties
    #DB Config
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update
    
    ui.domain.uri=${UI_DOMAIN_URI}
    
    # Google OAuth2 Configuration
    spring.security.oauth2.client.registration.google.client-id=${OAUTH2_CLIENT_ID}
    spring.security.oauth2.client.registration.google.client-secret=${OAUTH2_CLIENT_SECRET}
    spring.security.oauth2.client.registration.google.redirect-uri=${SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI}
    spring.security.oauth2.client.registration.google.scope=profile,email

    #Mail Config
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    spring.mail.properties.mail.smtp.starttls.required=true
    spring.mail.properties.mail.smtp.connectiontimeout=5000
    spring.mail.properties.mail.smtp.timeout=5000
    spring.mail.properties.mail.smtp.writetimeout=5000


    spring.datasource.url=${SPRING_DATASOURCE_URL}
    spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
    spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
    
    # Hibernate properties
    spring.jpa.show-sql=false
    spring.jpa.properties.hibernate.default_schema=public
    
    # SMTP Server Configuration for Gmail
    spring.mail.username=${SPRING_MAIL_USERNAME}
    spring.mail.password=${SPRING_MAIL_PASSWORD}

    #braintree creds
    braintree.sandbox.merchant-id=${BRAINTREE_MERCHANT_ID}
    braintree.sandbox.public-key=${BRAINTREE_PUBLIC_KEY}
    braintree.sandbox.private-key=${BRAINTREE_PRIVATE_KEY}
    
    #OCR Space
    ocr.api.url=https://api.ocr.space/parse/image
    ocr.api.key="${OCR_API_KEY}"
    
    #groq
    groq.api.url=https://api.groq.com/openai/v1/chat/completions
    groq.api.key=${GROQ_API_KEY}
    
    springdoc.api-docs.enabled=true
    springdoc.swagger-ui.enabled=true
    springdoc.info.title=My API Documentation
    springdoc.info.description=A detailed description of my API.
    springdoc.info.version=1.0.0
    
    spring.servlet.multipart.max-file-size=1MB
    spring.servlet.multipart.max-request-size=1MB


- Below is the application.properties sample for dev environment. where the values for (secrets, passkeys or password or key) get resolved from either environmental variables or hardcoded values.

   ```properties
    # DataSource properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/resumebuilder
    spring.datasource.username=sample_username
    spring.datasource.password=sample_password
    
    # Hibernate properties
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true


    # Google OAuth2 Configuration
    spring.security.oauth2.client.registration.google.client-id=sample_id
    spring.security.oauth2.client.registration.google.client-secret=sample_secret
    spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
    
    ui.domain.uri=http://localhost:5173
    
    # SMTP Server Configuration for Gmail
    spring.mail.port=587
    spring.mail.username=kaustavofficial09@gmail.com
    spring.mail.password=sample_app_passkey
    
    #braintree creds
    braintree.sandbox.merchant-id=sample_braintree_merchant_id
    braintree.sandbox.public-key=sample_public_key
    braintree.sandbox.private-key=sample_private_key
    
    #OCR Space
    ocr.api.url=http://api.ocr.space/parse/image
    ocr.api.key=ocr_api_key

    #groq
    groq.api.key=groq_api_key
    groq.api.url=https://api.groq.com/openai/v1/chat/completions



## Testing

This project includes unit and integration tests to ensure the functionality and reliability of the application.

### Prerequisites

Ensure that you have the following tools installed to run the tests:

- **JUnit** (for unit tests)
- **Mockito** (for mocking dependencies in unit tests)
- **Spring Boot Test** (for integration tests)
- **Maven** (for running tests and building the project)

### Running Unit Tests

Unit tests are located in the `src/test/java` directory and are executed using JUnit.

To run the unit tests using Maven:
      ```bash
      mvn test




## Features

- **User Authentication**: Supports login and registration with JWT-based authentication and oauth2 based authentication (as of now google only). As future scope, I will be integrating github and facebook client as well for ease of access.
- **User Authorization**: As of now, users' activities are categorised into two roles (e.g. Free User, Premium User). Free User can perform basic operations only like resume creation, profile viewing, etc but they can't download their resumes as well as are not allowed to analyse resumes against job description. On the other hand, Premium User can access all the features including the basic ones.
- **Payment Integration**: In order to be premium user, user needs to pay 20 rupees. And it's a lifetime access to the resources. For Payment integration, braintree has been used.
- **AI Completion**: During resume creation process, AI tool like GROQ AI has been put in place to assist user in writing content for their resumes. Currently, they can take leverage of it to write projec, experience and summary sections. 
- **Download & Share Resume**: Only Premium users can download and share resumes in PDF format.
- **Resume Analysis**: Only Premium users can analyse their resume against job description. For now, it just tells how many matching and un-matching keywords are there in the resume considering the JD. In future enhancement, ATS score, nlp etc will be used for more granular analysis.




## Contributing

I welcome contributions from the community. Please follow the steps below to contribute to this project:

### Steps to Contribute

1. **Fork the Repository**: 
   - Fork this repository to your own GitHub account.
   
2. **Clone the Forked Repository**:
   - Clone your fork to your local machine using:
     ```bash
     git clone https://github.com/kagit00/ai-resume-builder-backend.git
     ```
   
3. **Create a New Branch**:
   - Create a new branch for your feature or bugfix:
     ```bash
     git checkout -b feature-name
     ```

4. **Make Changes**:
   - Make your changes in your local repository. Ensure the code follows the project's style guide and passes all tests.

5. **Commit Changes**:
   - Commit your changes with a descriptive message:
     ```bash
     git commit -m "Add feature or fix bug"
     ```

6. **Push Changes**:
   - Push your changes to your GitHub repository:
     ```bash
     git push origin feature-name
     ```

7. **Create a Pull Request**:
   - Go to the original repository and create a pull request (PR) with your changes.

### Guidelines

- Please ensure that your changes do not break existing functionality. Write tests for new features or bugfixes if possible.
- Provide a clear description of the problem you are solving and the approach you have taken in the pull request.
- Follow the **code style** used in the project (e.g., naming conventions, indentation).

### Code of Conduct

By participating in this project, you agree to abide by the project's [Code of Conduct](./CODE_OF_CONDUCT.md) and adhere to respectful and professional behavior.



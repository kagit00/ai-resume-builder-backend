# AI-Powered Resume Builder - Backend

## Overview

The **AI-Powered Resume Builder Backend** is the backbone of the application, designed to provide APIs for AI-driven resume creation, user authentication, and subscription management. Developed using **Spring Boot**, it ensures a secure, scalable, and efficient architecture.

---

## Key Features  

1. **Authentication & Authorization**  
   - Supports **JWT** for token-based authentication.  
   - Enables **OAuth2** for social login (Google, GitHub).  

2. **AI-Powered Resume Generation**  
   - Integrated with **GROQ AI** and **OCR Space** for accurate resume creation.  

3. **Premium Subscription Management**  
   - Payment processing via **Braintree** for seamless subscription upgrades.  

4. **Performance Optimization**  
   - Uses **Caffeine Caching** to enhance API response times.  

5. **Database Management**  
   - Stores user data, resumes, and subscription details in **PostgreSQL**.  

---

## Technologies Used  

- **Java 8**: Core backend language.  
- **Spring Boot**: Framework for building APIs.  
- **Hibernate**: ORM for database operations.  
- **PostgreSQL**: Database for persistent storage.  
- **Caffeine Caching**: For optimizing performance.  
- **Braintree**: Payment gateway integration.  
- **Google Cloud Platform (GCP)**: Cloud deployment.  

---

## Prerequisites  

1. **Java**: Version 8 or higher.  
2. **Maven**: Dependency and build management.  
3. **PostgreSQL**: Database setup.  
4. **API Keys**: Required for external integrations:  
   - **GROQ AI**  
   - **OCR Space**  
   - **Braintree**  

---

## Setup Instructions  

### Step 1: Clone the Repository  
```bash
git clone https://github.com/your-repo/ai-resume-builder-backend.git
cd ai-resume-builder-backend

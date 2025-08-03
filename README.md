# GrowTrack – Unified Child Health & Behavior Intelligence Platform

**GrowTrack** is an AI-powered platform that monitors child development, detects neurodevelopmental and mental health patterns, and provides personalized care recommendations to parents and educators — especially in low-resource school and community settings.

---

## Problem Statement

Despite increasing awareness around child well-being:

-  **Schools and communities lack structured tools** to monitor and follow up on children's behavioral or emotional issues.
-  **Early signs of developmental or psychological challenges** often go unnoticed or unaddressed.
-  **Preventable issues escalate** into chronic problems due to late or no intervention.
-  **Communication gaps between parents and faculty** make coordinated care difficult.

---

##  Solution Overview

**GrowTrack** enables:

-  Centralized behavior tracking from parents, faculty.
-  GPT-based pattern recognition for early detection of behavioral risks
-  Intelligent recommendations tailored to the child’s issues
-  Resolution tracking and re-escalation if problems persist
-  A shared interface for parents, faculty, and administrators

---
##  AI + Behavior Engine

- Behavior logs (emotion-rich events) are submitted by faculty, parents, or AI(future scope: AI companion).
- Logs are batch analyzed periodically using GPT-4 (via Azure OpenAI).
- AI infers high-confidence **behavioral patterns** (e.g., anxiety, avoidance).
- Based on recent unresolved patterns, AI generates **recommendations** for:
    - Parental care
    - School-based intervention
    - Escalation (e.g., counselor or pediatric referral)

## Tech Stack

| Layer         | Technology                |
|---------------|---------------------------|
| Frontend      | React + Tailwind          |
| Backend       | Spring Boot (Java)        |
| Database      | PostgreSQL                |
| AI/NLP        | Azure OpenAI (GPT-4)      |
| Auth & Roles  | Spring Security           |
| Scheduler     | Spring `@Scheduled`       |
| API Comm      | REST (JSON)               |

## Limitations

- AI-generated insights may need human verification
- Needs data privacy compliance (GDPR, etc.)

## Getting Started

### Backend (Spring Boot)

```bash
backend:
cd GrowSmart
./mvnw spring-boot:run

frontend:
cd client/Client
npm install
npm start

A small model we tried out for future scope: AI Companion
drive link:
https://drive.google.com/drive/folders/1PJ_SAm_-gPck8M7XrNF_ceilm-TtLFm5?usp=sharing

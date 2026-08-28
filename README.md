# Camunda Order Processing Demo Application

An automated order management system using Camunda BPM workflow engine integrated with Spring Boot. This project demonstrates how to orchestrate complex business processes involving order validation, inventory management, payment processing, and shipment coordination.

## Overview

This application automates the complete order lifecycle from order placement through delivery. Camunda orchestrates each step, ensuring proper sequencing and handling issues like payment failures or insufficient inventory.

### Process Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                     ORDER PROCESSING WORKFLOW                       │
└─────────────────────────────────────────────────────────────────────┘

Order Created
    ↓
Validate Order
    ↓
Check Stock Available?
    ├─ NO  → Cancel Order → End
    └─ YES → Continue
    ↓
Reserve Inventory
    ├─ FAIL → Cancel Order → End
    └─ SUCCESS → Continue
    ↓
Evaluate Order Value (High-value = > $5,000)
    ├─ HIGH-VALUE → Finance Approval (Manual) → Approved?
    │               ├─ NO → Cancel → End
    │               └─ YES → Continue
    └─ NORMAL-VALUE → Continue
    ↓
Prepare Payment
    ↓
Process Payment (External Gateway)
    ├─ SUCCESS → Continue
    └─ FAILED → Payment Review (Manual) → Approved?
                 ├─ NO → Cancel → End
                 └─ YES → Continue
    ↓
Prepare Shipment
    ↓
Ship Order (External Carrier)
    ↓
Wait for Delivery Confirmation (Webhook)
    ↓
Complete Order
    ↓
Order Complete/Cancelled ← End
```

## Prerequisites

| Requirement | Version | Details |
|---|---|---|
| Java | 21+ | JDK 21 LTS or newer |
| PostgreSQL | 13+ | Database server |
| Gradle | 8.12+ | Included via wrapper (./gradlew) |
| Git | Latest | Version control |

## Camunda vs Traditional Approach

| Aspect | Traditional (if-else code) | Camunda BPM |
|---|---|---|
| Process Definition | Scattered in multiple files | Visual BPMN diagram, easy to modify |
| State Tracking | Manual logging, custom queries | Built-in process instance tracking |
| Error Handling | If-else blocks, manual retries | Automatic retry configuration |
| Manual Steps | Custom forms and pages | Built-in Camunda Task UI |
| Approval Logic | Hard-coded conditions | Visual gateways, easy to change thresholds |
| Audit Trail | Application logs | Complete history in Camunda |
| External System Delays | Blocks main thread | Async workers, non-blocking |
| Monitoring | Custom dashboards | Camunda Cockpit built-in |
| Changing Logic | Code change, recompile, redeploy | BPMN edit, reload (no code) |

## What Was Built

### REST API
Endpoints to create orders, check status, and list orders. Order creation automatically starts the Camunda workflow and passes order details into the process.

### Synchronous Operations
Immediate tasks executed as the order flows through the workflow:
- Order validation (checks for required data)
- Stock availability checking
- Inventory reservation
- Payment and shipment preparation
- Order status updates

### Asynchronous Workers
Background tasks that process independently:
- Payment processing with automatic 2x retry on failure (5-second delay)
- Shipment coordination and tracking

### Business Rules
Automatic determination of approval requirements. Orders exceeding $1,000 are flagged for Finance approval; smaller orders skip approval and proceed to payment.

### Manual Approval Tasks
Finance team logs into Camunda to approve high-value orders and failed payments through a task management interface.

### Event Integration
Webhook endpoint that receives delivery confirmations from shipping providers, triggering workflow continuation and order completion.

## Tech Stack

- Camunda BPM 7.24.0 - Workflow and process automation platform
- Spring Boot 3.5.5 - REST API and Camunda integration
- PostgreSQL - Persistent database for orders and process state
- Java 21 - Programming language
- Gradle 8.12 - Build automation

## Quick Start

### 1. Prerequisites

```bash
# Check Java version
java -version

# Create PostgreSQL database
createdb camunda_demo
```

### 2. Configure Database

Edit `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/camunda_demo
    username: postgres
    password: your_password
```

### 3. Build and Run

```bash
# Build
./gradlew clean build

# Run
./gradlew bootRun
```

Application starts at http://localhost:8080

### 4. Test

```bash
# Create an order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId": 1, "items": [{"productId": 1, "quantity": 2}]}'

# View order
curl http://localhost:8080/api/orders/1

# Monitor workflow
# Browser: http://localhost:8080/camunda (admin / admin)
```

## Using the System

**Camunda Cockpit** - Process Monitoring
- URL: http://localhost:8080/camunda
- Login: admin / admin
- View all orders, track progress, inspect process variables

**Task List** - Manual Approvals
- URL: http://localhost:8080/camunda/app/tasklist/
- Finance team approves high-value orders and payment reviews

**API Documentation**
- URL: http://localhost:8080/swagger-ui.html

## How It Works

1. Customer submits order via API
2. Order saved to database, Camunda workflow starts with order ID
3. Validation checks order has items
4. Stock check determines if inventory available
5. If stock available, inventory reserved
6. Order value evaluated: High-value orders require Finance approval
7. If approved (or not high-value), payment is prepared
8. Payment gateway charges customer with automatic retry on failure
9. If payment succeeds, shipment is prepared
10. Shipping carrier notified
11. Workflow waits for delivery webhook
12. Delivery confirmation received via webhook
13. Order marked complete

If any step fails (insufficient stock, payment error), the order is cancelled or paused for manual review depending on the issue.

## Project Structure

```
src/main/java/az/company/demo/
├── controller/          REST endpoints for orders, products, webhooks
├── service/             Business logic (orders, inventory, payments, shipping)
├── delegate/            Synchronous workflow tasks (validation, reservations)
├── worker/              Asynchronous background workers (payment, shipping)
├── dao/                 Database entities and repositories
├── model/               Data transfer objects
├── exception/           Custom exceptions
├── config/              Spring and Camunda configuration
├── process/             Process variable constants
└── client/              External service clients

src/main/resources/
├── application.yaml     Configuration file
├── order-system.bpmn    Process definition
└── high-value-order.dmn Business rules
```

## Key Design Patterns

**Delegates** - Synchronous tasks executing immediately in the workflow
**External Workers** - Asynchronous tasks with retry logic for external services
**Process Variables** - Context data flowing through the workflow
**Business Rules** - DMN for declaring approval thresholds without code
**Error Boundaries** - Graceful failure handling with alternative paths
**Message Events** - Webhook integration for external system communication

## Common Use Cases

This order processing pattern applies to:
- Loan approval (validate → check credit → approve → disburse)
- Leave requests (request → manager approval → HR process → confirm)
- Support tickets (create → assign → resolve → close)
- Document processing (upload → scan → validate → store)
- Expense claims (submit → manager review → approve → reimburse)
- Onboarding (register → email → verify → create account)

---

**Author:** Aqsin211
**Repository:** https://github.com/Aqsin211/camunda-demo-app

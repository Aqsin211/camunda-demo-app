# Camunda Order Processing Demo Application

A demonstration of how to build an **automated order management system** using Camunda BPM workflow engine integrated with Spring Boot. This project shows a real-world example of orchestrating complex business processes involving order validation, inventory management, payment processing, and shipment coordination.

## What This Project Does

This application automates the complete order lifecycle - from the moment a customer places an order until it's delivered. Instead of having different systems handle each step independently, Camunda orchestrates the entire process, making sure each step happens in the right order and handling any issues that come up.

When an order is created, the system:
1. Validates the order data
2. Checks if products are in stock
3. Reserves the inventory
4. Evaluates if the order needs management approval (for high-value orders)
5. Processes the payment through a payment gateway
6. Prepares shipment details
7. Ships the order
8. Waits for delivery confirmation
9. Marks the order as complete

If anything goes wrong (like insufficient stock or payment failure), the order gets cancelled automatically.

## Why Use Camunda?

Without a workflow engine, handling this process would require:
- Writing lots of conditional logic in code
- Managing state across multiple services
- Manually implementing retry logic for failures
- Creating complex if-else chains
- Tracking where each order is in the process

Camunda lets you define this process visually as a workflow diagram, then automatically executes it. The workflow engine handles:
- **Process state management** - knows exactly where each order is
- **Error recovery** - automatically retries failed tasks
- **Multi-step orchestration** - manages the sequence of operations
- **Human tasks** - Finance team can approve orders through a UI
- **Business rules** - Apply logic like "orders over $5,000 need approval"
- **Audit trail** - Complete history of every order

## Technologies Used

- **Camunda BPM** - Open source workflow engine that executes BPMN process diagrams
- **Spring Boot** - Framework for building the REST API and integrating with Camunda
- **PostgreSQL** - Database storing orders, inventory, and payment records
- **Java 21** - Programming language
- **Gradle** - Build tool

## What I Built

### REST API for Order Management

Created endpoints that allow clients to:
- Submit new orders to the system
- Check order status
- List all orders

The order creation endpoint doesn't just save to the database - it also kicks off the Camunda workflow automatically, passing in key information like the order ID and total amount.

### Database Models

Designed data structures to store:
- **Orders** - Customer orders with items and totals
- **Order Items** - Individual products in each order with quantities
- **Products** - Available products with pricing
- **Payments** - Record of payment attempts and results
- **Shipments** - Tracking shipment status

### Synchronous Operations (Immediate Execution)

Built operations that run right away as the order flows through the workflow:

**Order Validation** - When order enters the process, validates it has items and valid data.

**Stock Checking** - Checks if we have enough inventory for all items in the order. If stock is available, the process continues; if not, the order gets cancelled.

**Stock Reservation** - Once approved, actually reserves the inventory so it can't be sold to another customer. If reservation fails, the order is cancelled.

**Payment Preparation** - Sets up the payment information before charging the customer.

**Shipment Preparation** - Prepares shipping details and labels.

**Order Completion** - Updates the order status to completed when delivery confirmation arrives.

### Asynchronous Operations (Background Processing)

Built background workers that process tasks independently:

**Payment Processing Worker** - Handles the actual charge to the payment gateway. If the payment fails, it automatically retries up to 2 times with a 5-second delay between attempts. This is separate from the main workflow, so if the payment gateway is slow, it doesn't block other orders.

**Shipping Worker** - Coordinates with the shipping system. Creates shipment records and monitors their progress.

### Business Rules Engine

Set up business rules to automatically determine if an order needs management approval. The rule is simple: any order over $5,000 gets flagged as high-value and requires Finance approval before payment. Orders under that amount skip the approval step and go straight to payment.

### Manual Approval Workflow

For high-value orders and failed payments, Finance team members can log into the system and approve or reject them through a task management interface. The workflow pauses at these steps waiting for human decision.

### Event-Based Communication

Implemented a webhook endpoint that receives shipment delivery confirmations from the shipping provider. When a shipment is delivered, the system sends a message into the workflow to continue processing and mark the order as complete.

## How It Works - The Flow

1. **Customer places order** via API → OrderController saves it to database and starts Camunda workflow

2. **Validation step** → ValidateOrderDelegate checks order has items

3. **Stock check** → CheckStockDelegate queries inventory to see if products are available
   - If stock available → Continue to next step
   - If stock unavailable → Skip ahead to cancellation

4. **Stock reservation** → ReserveStockDelegate marks inventory as reserved
   - If reservation succeeds → Continue
   - If reservation fails (error boundary) → Cancel order

5. **Evaluate order value** → Business rule checks order total
   - If over $5,000 (high-value) → Route to Finance approval
   - If under $5,000 → Skip approval

6. **Finance approval** (for high-value orders only) → Pauses workflow, Finance team reviews and approves/rejects via Camunda UI
   - If approved → Continue to payment
   - If rejected → Cancel order

7. **Prepare payment** → PreparePaymentDelegate sets up payment details

8. **Process payment** → PaymentExternalTaskWorker calls payment gateway
   - If successful → Continue
   - If failed → Go to payment review

9. **Payment review** (if payment failed) → Finance reviews failed payment, can manually approve proceeding or cancel

10. **Prepare shipment** → PrepareShipmentDelegate gets shipment info ready

11. **Ship order** → ShippingExternalTaskWorker coordinates with shipping provider

12. **Wait for delivery** → Workflow pauses, waiting for webhook notification

13. **Delivery confirmation arrives** → Webhook endpoint receives notification and resumes workflow

14. **Complete order** → CompleteOrderDelegate marks order as completed

15. **Order is done** → End event, order fully processed

Throughout this entire flow, if anything goes wrong, the system either retries automatically (for payment) or pauses for human review (for approvals).

## Key Features

**Automated workflow** - No code needed to manage the process flow; it's defined visually in BPMN diagram

**Error recovery** - Payment failures automatically retry; other errors pause for manual review

**Human approvals** - Finance team reviews high-value orders and payment issues through UI

**Business rules** - Simple rules like approval thresholds defined separately from code

**Audit trail** - Complete history of every order and every step it went through

**Decoupled services** - Payment and shipping are handled asynchronously, so slow external services don't block other orders

**Webhook integration** - Receive updates from external systems (like shipping providers) and continue processing

## How to Run It

### Setup

1. Install PostgreSQL and create a database called `camunda_demo`

2. Update the database credentials in `application.yaml` with your PostgreSQL username and password

3. Build the project:
   ```bash
   ./gradlew clean build
   ```

4. Run it:
   ```bash
   ./gradlew bootRun
   ```

### Using the System

Once running on http://localhost:8080:

**Create an order:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {"productId": 1, "quantity": 2}
    ]
  }'
```

**View order status:**
```bash
curl http://localhost:8080/api/orders/1
```

**Monitor in Camunda Cockpit** - Open browser to http://localhost:8080/camunda
- Login: admin / admin
- See all running orders
- View which step each order is at
- See process variables and history

**Approve high-value orders** - Finance team logs into http://localhost:8080/camunda/app/tasklist/
- See tasks awaiting approval
- Review order details
- Click approve or reject

**See API documentation** - http://localhost:8080/swagger-ui.html
- All available endpoints
- Request/response examples
- Try endpoints directly

## Project Structure

The code is organized by responsibility:

**Controllers** - Handle incoming HTTP requests for orders, products, and webhooks

**Services** - Contains business logic for orders, inventory, payments, and shipping

**Delegates** - Run immediately when the workflow reaches them (validation, stock checks, order updates)

**Workers** - Run in background for long-running operations (payment processing, shipping tracking)

**Database** - Entities and repositories for storing and retrieving data

**Models** - Data transfer objects for API requests and responses

**Configuration** - Camunda and Spring Boot settings

## What Makes This a Good Example

**Real-world scenario** - Order processing is something most e-commerce systems need

**Covers core patterns** - Shows synchronous tasks, asynchronous workers, manual approval, business rules, error handling, webhooks

**Scalable approach** - The pattern can be adapted for other workflows (loan processing, support tickets, leave requests, etc.)

**Production-ready** - Error handling, retry logic, database transactions, and audit trails are all implemented

**Clean architecture** - Code is organized by responsibility, not by technical layer

**Demonstrates integration** - Shows how Camunda fits into a Spring Boot application with real databases and external services

## Common Use Cases for This Pattern

Once you understand this pattern, you can adapt it for:

- **Loan approval** - Validate application → Check credit → Get approval → Disburse funds
- **Support tickets** - Create ticket → Assign to team → Track progress → Close
- **Leave requests** - Employee requests → Manager approves → HR processes → Send confirmation
- **Document processing** - Upload → Scan → Extract data → Validate → Store
- **Onboarding** - Register user → Send email → Verify → Create account → Send welcome
- **Claims processing** - Submit claim → Validate → Investigate → Approve/deny → Pay

The structure and concepts stay the same; just the specific business logic changes.

## What You Learn From This Project

- How to model business processes as workflows
- How to integrate Camunda with Spring Boot
- When to use synchronous vs asynchronous task execution
- How to implement error recovery and retry logic
- How to add manual approval steps to automated processes
- How to apply business rules dynamically
- How to track and audit complex processes
- How to handle webhooks and external events
- Database design for process-oriented applications
- Building REST APIs that start workflows

---

**Author:** Aqsin211  
**Purpose:** Learning and demonstration of Camunda BPM patterns

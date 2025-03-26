<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invoice Payments</title>

    <link rel="stylesheet" crossorigin href="assets/compiled/css/app.css">
    <link rel="stylesheet" crossorigin href="assets/compiled/css/app-dark.css">
    <link rel="stylesheet" crossorigin href="assets/compiled/css/iconly.css">        
</head>

<body>
    <div id="app">
        <div id="sidebar">
            <div class="sidebar-wrapper active">
                <div class="sidebar-menu">
                    <ul class="menu">
                        <li class="sidebar-title">Menu</li>
                        <li class="sidebar-item active">
                            <a href="dashboard" class='sidebar-link'>
                                <i class="bi bi-grid-fill"></i>
                                <span>Dashboard</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a href="discounts" class='sidebar-link'>
                                <i class="bi bi-percent"></i>
                                <span>Discounts</span>
                            </a>
                        </li>
                        <li class="sidebar-item">
                            <a href="logout" class='sidebar-link'>
                                <i class="bi bi-box-arrow-right"></i>
                                <span>Logout</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div id="main">
            <header class="mb-3">
                <a href="#" class="burger-btn d-block d-xl-none">
                    <i class="bi bi-justify fs-3"></i>
                </a>
            </header>

            <div class="page-heading">
                <div class="d-flex justify-content-between align-items-center">
                    <h3>Invoice Payments</h3>
                    <a href="javascript:history.back()" class="btn btn-primary">
                        <i class="bi bi-arrow-left"></i> Back
                    </a>
                </div>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success" role="alert">
                        ${success}
                    </div>
                </c:if>
            </div>

            <div class="page-content">
                <section class="row">
                    <div class="col-12">
                        <!-- Invoice Details Card -->
                        <div class="card">
                            <div class="card-header">
                                <h4>Invoice Details</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-3">
                                        <p class="text-muted mb-1">Invoice Number</p>
                                        <p class="font-weight-bold">${paymentData.invoice.number != null ? paymentData.invoice.number : 'N/A'}</p>
                                    </div>
                                    <div class="col-md-3">
                                        <p class="text-muted mb-1">Client</p>
                                        <p class="font-weight-bold">${paymentData.invoice.client_name}</p>
                                    </div>
                                    <div class="col-md-3">
                                        <p class="text-muted mb-1">Amount</p>
                                        <p class="font-weight-bold">
                                            <fmt:formatNumber value="${paymentData.invoice.amount}" type="currency" currencySymbol="$" />
                                        </p>
                                    </div>
                                    <div class="col-md-3">
                                        <p class="text-muted mb-1">Due Date</p>
                                        <p class="font-weight-bold">${paymentData.invoice.due_date != null ? paymentData.invoice.due_date : 'N/A'}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Payments List Card -->
                        <div class="card">
                            <div class="card-header">
                                <h4>Payment History</h4>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Amount</th>
                                                <th>Date</th>
                                                <th>Source</th>
                                                <th>Description</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${paymentData.payments}" var="payment">
                                                <tr>
                                                    <td>${payment.id}</td>
                                                    <td>
                                                        <fmt:formatNumber value="${payment.amount}" type="currency" currencySymbol="$" />
                                                    </td>
                                                    <td>${payment.date}</td>
                                                    <td>${payment.source}</td>
                                                    <td>${payment.description}</td>
                                                    <td>
                                                        <button type="button" class="btn btn-warning btn-sm" onclick="openEditModal(${payment.id}, ${payment.amount}, ${paymentData.invoice.id})">
                                                            <i class="bi bi-pencil"></i> Edit
                                                        </button>
                                                        <a href="payment_action?paymentId=${payment.id}&invoice=${paymentData.invoice.id}" 
                                                           class="btn btn-danger btn-sm"
                                                           onclick="return confirm('Are you sure you want to delete this payment?')">
                                                            <i class="bi bi-trash"></i> Delete
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            <!-- Edit Payment Modal -->
            <div class="modal fade" id="editPaymentModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Edit Payment</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form id="editPaymentForm" method="post" action="payment_action">
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label for="amount" class="form-label">Amount</label>
                                    <input type="number" step="0.01" class="form-control" id="amount" name="amount" required>
                                </div>
                                <input type="hidden" name="invoice" id="invoiceId">
                                <input type="hidden" name="paymentId" id="paymentId">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Save changes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <script src="assets/compiled/js/app.js"></script>
            <!-- Bootstrap Modal JS -->
            
            
            <script>
                function openEditModal(paymentId, currentAmount, invoiceId) {
                    const modal = new bootstrap.Modal(document.getElementById('editPaymentModal'));
                    document.getElementById('paymentId').value = paymentId;
                    document.getElementById('amount').value = currentAmount;
                    document.getElementById('invoiceId').value = invoiceId;
                    modal.show();
                }
            </script>
        </div>
    </div>
</body>

</html> 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invoice List - ${invoiceData.status_display}</title>

    <link rel="stylesheet" crossorigin href="./assets/compiled/css/app.css">
    <link rel="stylesheet" crossorigin href="./assets/compiled/css/app-dark.css">
    <link rel="stylesheet" crossorigin href="./assets/compiled/css/iconly.css">
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
                    <h3>${invoiceData.status_display} Invoices</h3>
                    <a href="dashboard" class="btn btn-primary">
                        <i class="bi bi-arrow-left"></i> Back to Dashboard
                    </a>
                </div>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
            </div>

            <div class="page-content">
                <section class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h4>Invoice List</h4>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Number</th>
                                                <th>Client</th>
                                                <th>Amount</th>
                                                <th>Total Paid</th>
                                                <th>Remaining</th>
                                                <th>Due Date</th>
                                                <th>Created At</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${invoiceData.invoices}" var="invoice">
                                                <tr>
                                                    <td>${invoice.id}</td>
                                                    <td>${invoice.number != null ? invoice.number : 'N/A'}</td>
                                                    <td>${invoice.client_name}</td>
                                                    <td>
                                                        <fmt:formatNumber value="${invoice.amount}" type="currency" currencySymbol="$" />
                                                    </td>
                                                    <td>
                                                        <fmt:formatNumber value="${invoice.total_paid}" type="currency" currencySymbol="$" />
                                                    </td>
                                                    <td>
                                                        <fmt:formatNumber value="${invoice.remaining}" type="currency" currencySymbol="$" />
                                                    </td>
                                                    <td>${invoice.due_date != null ? invoice.due_date : 'N/A'}</td>
                                                    <td>${invoice.created_at}</td>
                                                    <td>
                                                        <a href="payments?invoice=${invoice.id.longValue()}" class="btn btn-primary btn-sm">
                                                            <i class="bi bi-credit-card"></i> View Payments
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

            <script src="assets/compiled/js/app.js"></script>
        </div>
    </div>
</body>

</html> 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Discount Management</title>

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
                        <li class="sidebar-item">
                            <a href="dashboard" class='sidebar-link'>
                                <i class="bi bi-grid-fill"></i>
                                <span>Dashboard</span>
                            </a>
                        </li>
                        <li class="sidebar-item active">
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
                <h3>Discount Management</h3>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success" role="alert">
                        ${sessionScope.success}
                        <% session.removeAttribute("success"); %>
                    </div>
                </c:if>
            </div>

            <div class="page-content">
                <section class="row">
                    <div class="col-12 col-lg-6">
                        <!-- Current Discount Rate Card -->
                        <div class="card">
                            <div class="card-header">
                                <h4>Current Discount Rate</h4>
                            </div>
                            <div class="card-body">
                                <div class="d-flex align-items-center">
                                    <div class="stats-icon purple mb-2">
                                        <i class="bi bi-percent"></i>
                                    </div>
                                    <div class="ms-3">
                                        <h6 class="text-muted font-semibold">Rate</h6>
                                        <h6 class="font-extrabold mb-0">${currentRate}%</h6>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Add New Discount Rate Card -->
                        <div class="card">
                            <div class="card-header">
                                <h4>Add New Discount Rate</h4>
                            </div>
                            <div class="card-body">
                                <form action="discounts" method="post">
                                    <div class="form-group">
                                        <label for="rate" class="form-label">Rate (%)</label>
                                        <input type="number" class="form-control" id="rate" name="rate" 
                                               step="0.01" min="0" max="100" required>
                                        <div class="form-text">Enter a value between 0 and 100</div>
                                    </div>
                                    <button type="submit" class="btn btn-primary mt-3">Add New Rate</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>

    <script src="assets/compiled/js/app.js"></script>
</body>

</html> 
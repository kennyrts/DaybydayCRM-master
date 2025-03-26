<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invoice Dashboard</title>            

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
                <h3>Total Statistics</h3>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
</div> 
<div class="page-content"> 
    <section class="row">
                    <div class="col-12">
                        <div class="row">
                            <div class="col-6 col-lg-4 col-md-6">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="d-flex align-items-center">
                                            <div class="avatar avatar-xl bg-success">
                                                <i class="bi bi-credit-card fs-3"></i>
                                            </div>
                                            <div class="ms-3 name">
                                                <h5 class="font-bold">Total Paid</h5>
                                                <h6 class="text-muted mb-0">${paymentStats.total_paid} $</h6>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card-footer">
                                        <a href="invoices?section=paid" class="btn btn-block btn-light">View Details</a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-6 col-lg-4 col-md-6">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="d-flex align-items-center">
                                            <div class="avatar avatar-xl bg-danger">
                                                <i class="bi bi-exclamation-triangle fs-3"></i>
                                            </div>
                                            <div class="ms-3 name">
                                                <h5 class="font-bold">Total Unpaid</h5>
                                                <h6 class="text-muted mb-0">${paymentStats.total_unpaid} $</h6>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card-footer">
                                        <a href="invoices?section=unpaid" class="btn btn-block btn-light">View Details</a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-6 col-lg-4 col-md-6">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="d-flex align-items-center">
                                            <div class="avatar avatar-xl bg-primary">
                                                <i class="bi bi-cash-stack fs-3"></i>
                                            </div>
                                            <div class="ms-3 name">
                                                <h5 class="font-bold">Total</h5>
                                                <h6 class="text-muted mb-0">${paymentStats.total} $</h6>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card-footer">
                                        <a href="invoices?section=total" class="btn btn-block btn-light">View Details</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                    <!-- Task Statistics Chart -->
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h4>Task Statistics</h4>
                            </div>
                            <div class="card-body">
                                <canvas id="taskChart"></canvas>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <!-- Invoice Statistics Chart -->
                        <div class="col-6">
                            <div class="card">
                                <div class="card-header">
                                    <h4>Invoices Statistics</h4>
                                </div>
                                <div class="card-body">
                                    <canvas id="invoiceChart"></canvas>
                                </div>
                            </div>
                        </div>

                        <!-- Offer Statistics Chart -->
                        <div class="col-6">
                            <div class="card">
                                <div class="card-header">
                                    <h4>Offer Statistics</h4>
                                </div>
                                <div class="card-body">
                                    <canvas id="offerChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>            
    </section>
</div>
    
    <script src="assets/compiled/js/app.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
            <script>
                // Task Statistics Chart
                const taskCtx = document.getElementById('taskChart').getContext('2d');
                const taskData = JSON.parse('<c:out value="${taskStats}" escapeXml="false"/>');
                const taskLabels = Object.keys(taskData).filter(key => key !== 'total');
                const taskValues = taskLabels.map(label => taskData[label]);
                
                new Chart(taskCtx, {
                    type: 'bar',
                    data: {
                        labels: taskLabels,
                        datasets: [{
                            label: 'Number of Tasks',
                            data: taskValues,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.7)',
                                'rgba(54, 162, 235, 0.7)',
                                'rgba(255, 206, 86, 0.7)',
                                'rgba(75, 192, 192, 0.7)',
                                'rgba(153, 102, 255, 0.7)'
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)'
                            ],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    stepSize: 1
                                }
                            }
                        },
                        plugins: {
                            legend: {
                                position: 'top',
                            },
                            title: {
                                display: true,
                                text: 'Tasks by Status'
                            }
                        }
                    }
                });

                // Invoice Statistics Chart
                const invoiceCtx = document.getElementById('invoiceChart').getContext('2d');
                const invoiceData = JSON.parse('<c:out value="${invoiceStats}" escapeXml="false"/>');
                const invoiceLabels = Object.keys(invoiceData).filter(key => key !== 'total');
                const invoiceValues = invoiceLabels.map(label => invoiceData[label]);                
                
                new Chart(invoiceCtx, {
                    type: 'pie',
                    data: {
                        labels: invoiceLabels,
                        datasets: [{
                            data: invoiceValues,
                            backgroundColor: [
                                'rgba(54, 162, 235, 0.7)',   // Bleu pour brouillon
                                'rgba(75, 192, 192, 0.7)',   // Vert pour payé
                                'rgba(255, 206, 86, 0.7)',   // Jaune pour partiellement payé
                                'rgba(255, 99, 132, 0.7)',   // Rouge pour impayé
                                'rgba(128, 128, 128, 0.7)',  // Gris pour fermé
                                'rgba(153, 102, 255, 0.7)',  // Violet pour envoyé
                                'rgba(255, 159, 64, 0.7)'    // Orange pour trop-payé
                            ],
                            borderColor: [
                                'rgba(54, 162, 235, 1)',     // Bleu
                                'rgba(75, 192, 192, 1)',     // Vert
                                'rgba(255, 206, 86, 1)',     // Jaune
                                'rgba(255, 99, 132, 1)',     // Rouge
                                'rgba(128, 128, 128, 1)',    // Gris
                                'rgba(153, 102, 255, 1)',    // Violet
                                'rgba(255, 159, 64, 1)'      // Orange
                            ],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'top',
                            },
                            title: {
                                display: true,
                                text: 'Invoices by Status'
                            }
                        }
                    }
                });

                // Offer Statistics Chart
                const offerCtx = document.getElementById('offerChart').getContext('2d');
                const offerData = JSON.parse('<c:out value="${offerStats}" escapeXml="false"/>');
                const offerLabels = Object.keys(offerData).filter(key => key !== 'Total');
                const offerValues = offerLabels.map(label => offerData[label]);
                
                new Chart(offerCtx, {
                    type: 'pie',
                    data: {
                        labels: offerLabels,
                        datasets: [{
                            data: offerValues,
                            backgroundColor: [
                                'rgba(255, 206, 86, 0.7)',  // In-progress - Jaune
                                'rgba(255, 99, 132, 0.7)',  // Lost - Rouge
                                'rgba(75, 192, 192, 0.7)'   // Won - Vert
                            ],
                            borderColor: [
                                'rgba(255, 206, 86, 1)',
                                'rgba(255, 99, 132, 1)',
                                'rgba(75, 192, 192, 1)'
                            ],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'top',
                            },
                            title: {
                                display: true,
                                text: 'Offers by Status'
                            }
                        }
                    }
                });
            </script>
        </div>
    </div>
    
</body>
</html>
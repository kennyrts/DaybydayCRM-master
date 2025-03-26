<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Finance</title>        
        <link rel="stylesheet" crossorigin href="./assets/compiled/css/app.css">
        <link rel="stylesheet" crossorigin href="./assets/compiled/css/app-dark.css">
        <link rel="stylesheet" crossorigin href="./assets/compiled/css/auth.css">
    </head>

    <body>
        <div id="auth">
            
            <div class="row h-100">
                <div class="col-lg-5 col-12">
                    <div id="auth-left">                    
                        <h1 class="auth-title">Log in.</h1>                    
                        <form action="login" method="post">
                            <div class="form-group position-relative has-icon-left mb-4">
                                <input type="email" name="email" class="form-control form-control-xl" placeholder="Email" value="admin@admin.com">
                                <div class="form-control-icon">
                                    <i class="bi bi-person"></i>
                                </div>
                            </div>
                            <div class="form-group position-relative has-icon-left mb-4">
                                <input type="password" name="password" class="form-control form-control-xl" placeholder="Password" value="admin123">
                                <div class="form-control-icon">
                                    <i class="bi bi-shield-lock"></i>
                                </div>
                            </div>                        
                            <button class="btn btn-primary btn-block btn-lg shadow-lg mt-5">Log in</button>
                        </form>                    
                    </div>
                </div>
                <div class="col-lg-7 d-none d-lg-block">
                    <div id="auth-right">
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>
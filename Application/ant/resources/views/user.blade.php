<!DOCTYPE html>
<html>
    <head>

        <link href="https://fonts.googleapis.com/css?family=Lato:100" rel="stylesheet" type="text/css">
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="Chinmay Lad">

        <title>ANT</title>

        <!-- Bootstrap Core CSS -->
        <link rel="stylesheet" href="{{ URL::asset('assets/css/bootstrap.min.css') }}" />

        <!-- Custom Fonts -->
        <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>

        <link rel="stylesheet" href="{{ URL::asset('font-awesome/css/font-awesome.min.css') }}" type="text/css">

        <!-- Plugin CSS -->
        <link rel="stylesheet" href="{{ URL::asset('assets/css/animate.min.css') }}" type="text/css">

        <!-- Custom CSS -->
        <link rel="stylesheet" href="{{ URL::asset('assets/css/creative.css') }}" type="text/css">
    </head>
    <body id="page-top">
        <nav id="mainNav" class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand page-scroll" href="#page-top">ANT</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <!--<li>
                        <a class="page-scroll" href="{{URL::route('/')}}">Home</a>
                    </li>-->
                    <li>
                        <a class="page-scroll" href="#devices">Devices</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#services">Services</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#developers">Developers</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#contact">Contact</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="logout">Logout</a>
                    </li>
                    
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>

    <header>
        <div class="header-content">
            <div class="header-content-inner"> 
                <h1>Welcome {{ $name }} !!</h1>
                <hr>
                <p>Here in the dashboard you can manage the devices that are lost and get the update for the location for the device which are lost.</p>
            </div>
        </div>
    </header>

    <section id = "devices">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2 class="section-heading">Devices</h2>
                <hr class="primary">
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row">
                @if(Session::has('devices'))
                    <table class="table table-striped table-bordered">
                        <thead>
                          <tr>
                            <th>Sr No.</th>
                            <th>Device Name</th>
                            <th>Device UUID</th>
                            <th>Tag it!!</th>
                          </tr>
                        </thead>
                        <tbody>
                            @foreach(Session::get('devices') as $i=>$device)
                                <tr>
                                <td>{{$i+1}}</td>
                                <td>{{ $device->name }}</td>
                                <td>{{ $device->uuid }}</td>
                                <td>
                                <a href="#" class="btn btn-info">Tag it!</a>
                                </td>
                                </tr>   
                            @endforeach
                            </tbody>
                    </table>
                @else
                    <div class="col-lg-12 col-md-6 text-center">
                        <p class="wow bounceIn text-center"> No Devices added!!<br/>
                        Go to app and add devices.</p>
                    </div>
                @endif  
            
        </div>
    </div>
    </section>
    <section id="services">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2 class="section-heading">At Your Service</h2>
                    <hr class="primary">
                </div>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-diamond wow bounceIn text-primary"></i>
                        <h3>Sturdy Mechanisms</h3>
                        <p class="text-muted">Our templates are updated regularly so they don't break.</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-paper-plane wow bounceIn text-primary" data-wow-delay=".1s"></i>
                        <h3>Ready to Ship</h3>
                        <p class="text-muted">You can use this theme as is, or you can make changes!</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-newspaper-o wow bounceIn text-primary" data-wow-delay=".2s"></i>
                        <h3>Up to Date</h3>
                        <p class="text-muted">We update dependencies to keep things fresh.</p>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-thumbs-o-up wow bounceIn text-primary" data-wow-delay=".3s"></i>
                        <h3>Made with Perfection</h3>
                        <p class="text-muted">You have to make your websites with love these days!</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
<section id="developers" class="bg-secondary">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2 class="section-heading">Developers</h2>
                    <hr class="primary">
                </div>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-lg-4 col-md-4 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-android wow bounceIn text-primary"></i>
                        <h3>Chinmay Lad</h3>
                        <p class="text-muted">Fr. Agnel, Vashi</p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-4 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-male wow bounceIn text-primary" data-wow-delay=".1s"></i>
                        <h3>Aditya Landge</h3>
                        <p class="text-muted">Fr. Agnel, Vashi</p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-4 text-center">
                    <div class="service-box">
                        <i class="fa fa-4x fa-user wow bounceIn text-primary" data-wow-delay=".2s"></i>
                        <h3>Rohan Manudhane</h3>
                        <p class="text-muted">Fr.Agnel Vashi</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="contact">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 text-center">
                    <h2 class="section-heading">Let's Get In Touch!</h2>
                    <hr class="primary">
                    <p>Ready to start organizing your valuables? That's great! Give us a call or send us an email and we will get back to you as soon as possible!</p>
                </div>
                <div class="col-lg-4 col-lg-offset-2 text-center">
                    <i class="fa fa-phone fa-3x wow bounceIn"></i>
                    <p>123-456-6789</p>
                </div>
                <div class="col-lg-4 text-center">
                    <i class="fa fa-envelope-o fa-3x wow bounceIn" data-wow-delay=".1s"></i>
                    <p><a href="mailto:feedbackant@gmail.com">feedbackant@gmail.com</a></p>
                </div>
            </div>
        </div>
    </section>
    
    <!-- jQuery -->
    <script src="{{URL::asset('assets/js/jquery.js')}}"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="{{ URL::asset('assets/js/bootstrap.min.js') }}"></script>

    <!-- Plugin JavaScript -->
    <script src="{{URL::asset('assets/js/jquery.easing.min.js')}}"></script>
    <script src="{{URL::asset('assets/js/jquery.fittext.js')}}"></script>
    <script src="{{URL::asset('assets/js/wow.min.js')}}"></script>

    <!-- Custom Theme JavaScript -->
    <script src="{{URL::asset('assets/js/creative.js')}}"></script>
    </body>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</html>

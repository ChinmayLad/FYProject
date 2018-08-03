<!DOCTYPE html>
<html>
    <head>
        <title>ANT</title>

        <link href="https://fonts.googleapis.com/css?family=Lato:100" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="{{ URL::asset('font-awesome/css/font-awesome.min.css') }}" type="text/css">
        <style>
            html, body {
                height: 100%;
            }

            body {
                margin: 0;
                padding: 0;
                width: 100%;
                display: table;
                font-weight: 100;
                font-family: 'Lato';
                background-image: {{URL::asset('asset/img/cover_bg.jpg')}};
            }

            .container {
                text-align: center;
                display: table-cell;
                vertical-align: middle;
            }

            .content {
                text-align: center;
                display: inline-block;
            }

            .title {
                font-size: 96px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class = "content">
                @if(Session::has('error'))     
                    <strong class="alert alert-danger">{{ Session::get('error') }}</strong>
                @endif
            </div>
            <div class="content">
                <div class="title">Anti-Theft Tracking and Location Detection</div>
            </div>
            <br/>
            <div class="content">
                <a class="btn btn-danger" href="{!!URL::to('google')!!}"><i class="fa fa-2x fa-google-plus">Login</i></a>
            </div>
        </div>
    </body>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</html>

@extends('layouts.master')

@section('heading')
{{ __('Import CSV Data') }}
@endsection

@section('content')
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">{{ __('Import CSV Data') }}</div>
                <div class="panel-body">
                    

                    <form method="POST" action="{{ route('database.import') }}" enctype="multipart/form-data">
                        {{ csrf_field() }}                                                
                        <div class="form-group">
                            <label for="csv_file1">{{ __('CSV File 1') }}</label>
                            <input type="file" name="csv_file1" id="csv_file1" class="form-control" accept=".csv,.txt">
                        </div>

                        <div class="form-group">
                            <label for="csv_file2">{{ __('CSV File 2') }}</label>
                            <input type="file" name="csv_file2" id="csv_file2" class="form-control" accept=".csv,.txt">
                        </div>

                        <div class="form-group">
                            <label for="csv_file3">{{ __('CSV File 3') }}</label>
                            <input type="file" name="csv_file3" id="csv_file3" class="form-control" accept=".csv,.txt">
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">
                                {{ __('Import Data') }}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
@endsection 
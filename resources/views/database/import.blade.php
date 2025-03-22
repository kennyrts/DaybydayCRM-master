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
                    <div class="alert alert-info">
                        <strong>{{ __('Note:') }}</strong> {{ __('The CSV file must have headers that match the column names in the database table.') }}
                    </div>

                    <form method="POST" action="{{ route('database.import') }}" enctype="multipart/form-data">
                        {{ csrf_field() }}

                        <div class="form-group">
                            <label for="table">{{ __('Select Target Table') }}</label>
                            <select name="table" id="table" class="form-control">
                                @foreach($tables as $table)
                                    <option value="{{ $table }}">{{ ucfirst($table) }}</option>
                                @endforeach
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="csv_file">{{ __('CSV File') }}</label>
                            <input type="file" name="csv_file" id="csv_file" class="form-control" accept=".csv,.txt">
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